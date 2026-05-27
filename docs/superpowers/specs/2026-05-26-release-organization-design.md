# Release Organization — Design

**Date:** 2026-05-26
**Status:** approved (brainstorming)
**Next step:** writing-plans → implementation plan

## Goal

Set up a release process for The Lion King mod that handles two independent version axes (Minecraft version and mod version), supports keeping more than one Minecraft version alive simultaneously, lets a single-MC-version bug get fixed and released without touching the other branches, and stays cheap to operate for a hobby project.

## Steady-state model — see `docs/RELEASES.md`

The durable description of how releases, branches, and tags work lives in [`docs/RELEASES.md`](../../RELEASES.md). That doc is the source of truth and is what gets read at release time. This spec captures **the one-off migration work** needed to move the repo from its current state into that model.

Key decisions, summarised so this spec is self-contained:

- **No `main` branch.** Branches are named `mc/<mcver>` (e.g. `mc/1.20.1`). The branch name is the Minecraft version it targets.
- **Loose SemVer** for mod version (`MAJOR.MINOR.PATCH`), **counted independently on each branch**.
- **Tag shape** `v<modver>-mc<mcver>` (e.g. `v1.2.0-mc1.20.1`). The `-mc<mcver>` suffix is required because git tags share a single namespace across the repo — without it, the second branch to reach `v1.2.0` would collide with the first.
- **JAR filename** `circleofcraft-<modver>-mc<mcver>.jar`. Mod version comes from the tag (via a `MOD_VERSION` env var); Minecraft version comes from the branch's `gradle.properties`.
- **Tag-triggered release.** Pushing a `v*-mc*` tag fires a GitHub Actions workflow that builds, then publishes to GitHub Releases + CurseForge + Modrinth in one run.
- **Changelog** auto-generated from PR titles merged into the branch since the previous tag on the same branch. No `CHANGELOG.md`.
- **Cross-branch fixes** propagate by cherry-pick. Branches are not required to stay in sync.

Rejected alternatives (recorded for future re-evaluation):

- A multi-loader shared codebase (Architectury-style common module). Rejected because Forge → NeoForge crosses major vanilla Minecraft API breaks, and this mod is platform-heavy (events, Mixins, capabilities, world data, criteria triggers, custom block entities). The "shared common" surface after abstraction would be thin and every new feature would pay a per-platform tax. The reversal cost if we change our mind later is small (merge histories).
- Globally monotonic mod versions across branches. Rejected because it re-introduces the "synced versioning" mental model and forces a version bump on every branch for fixes touching only one.
- Maintaining a `CHANGELOG.md` file. Rejected to minimise per-release friction; PR titles serve as the changelog feed.
- Pre-release / beta channel. Out of scope for now; tag pattern leaves room to add `-rc<n>` later.

## Migration deliverables

These are the discrete pieces of work needed to land this. The plan that follows this spec should sequence them with checkpoints.

### 1. Branch rename: `main` → `mc/1.20.1`

- Rename the local branch and push the new name.
- Set GitHub default branch to `mc/1.20.1`.
- Delete the old `main` branch on the remote.
- Verify open PRs (if any) re-target correctly. Verify any branch-protection rules transfer.

### 2. Gradle: version comes from env, JAR is renamed by branch

- `build.gradle` reads `project.version` from a `MOD_VERSION` env var with a sensible local-dev default (e.g. `0.0.0-dev`).
- Minecraft version stays a single source of truth on the branch. Currently it's inlined in `build.gradle`'s `processResources` block — move it (and `forge_version` / version ranges) into `gradle.properties` so the workflow can read it without parsing Groovy.
- JAR base name set to `circleofcraft-<modver>-mc<mcver>.jar`. Verify with a local `./gradlew build` that the produced artifact has the expected filename when `MOD_VERSION` is set.
- `mods.toml` keeps reading `mod_version` from the existing token replacement; no functional change for users.

### 3. Release workflow `.github/workflows/release.yml`

Triggered on tag push matching `v*-mc*`. Job steps:

1. Checkout the tag's commit.
2. Parse the tag → `MOD_VERSION` (e.g. `1.2.0`) and `EXPECTED_MC_VERSION` (e.g. `1.20.1`).
3. Read `minecraft_version` from `gradle.properties`. Fail fast if it doesn't match `EXPECTED_MC_VERSION` — this guards against tagging from the wrong branch.
4. Set up Java 17 (matches `CLAUDE.md`), run `./gradlew build` with `MOD_VERSION` exported.
5. Generate release notes from PR titles merged into the branch since the previous tag on that branch. GitHub's auto-generated-release-notes API is the simplest source; the workflow passes the previous tag as `previous_tag_name`.
6. Create a GitHub Release for the tag and attach the JAR.
7. Upload to CurseForge with Minecraft version + loader (Forge or NeoForge, derived from branch) + release notes.
8. Upload to Modrinth via `modrinth/minotaur` Gradle plugin (or its raw API) with the same metadata.

The workflow must work unchanged on a future `mc/1.21.x` branch — Minecraft version and loader come from the branch's `gradle.properties`, not from the workflow YAML.

### 4. Repository secrets and configuration

- `CURSEFORGE_TOKEN` and `MODRINTH_TOKEN` — GitHub Actions secrets.
- CurseForge project ID and Modrinth project ID — committed to `build.gradle` or the workflow YAML (not secrets; they're public).

Prerequisite: the CurseForge and Modrinth projects must exist. If they don't yet, that's a one-time setup step performed manually before this workflow can succeed. The workflow should not block local builds or PR CI — only the tag-triggered job needs the tokens.

### 5. Documentation

- `docs/RELEASES.md` — reference doc.
- `CLAUDE.md` References section — entry pointing at `docs/RELEASES.md`.

## Out of scope

- Cutting an actual release. This spec sets up the machinery; the first real `v*-mc*` tag is a separate decision.
- Starting the 1.21 / NeoForge port. The model supports it; the port itself is its own project.
- Pre-release / beta channel. Add when there's a concrete need.
- Migrating any non-release CI (the existing `.github/workflows/ci.yml` for PR builds and tests stays as-is).

## Acceptance criteria

The spec is fulfilled when **all** of the following hold:

1. `git branch --show-current` on a clean clone returns `mc/1.20.1`. There is no `main` branch on the remote.
2. `./gradlew build` with `MOD_VERSION=1.0.0` produces a `circleofcraft-1.0.0-mc1.20.1.jar`.
3. Pushing a tag `vX.Y.Z-mc1.20.1` from `mc/1.20.1` triggers the release workflow, which publishes to GitHub Releases + CurseForge + Modrinth, all with matching metadata and identical release notes.
4. Pushing a tag with a mismatched `-mc<mcver>` suffix (e.g. `vX.Y.Z-mc1.21.1` from `mc/1.20.1`) causes the workflow to fail at the validation step without uploading anything.
5. `docs/RELEASES.md` and the `CLAUDE.md` References entry are committed.

## Open questions for the plan phase

- **CurseForge and Modrinth projects** — do they exist already, or does this project need to create them first? If creating, this is a manual prerequisite step that should appear in the plan.
- **Which CurseForge upload mechanism** — the official CurseForge upload API via a custom step, or a community Gradle plugin? `modrinth/minotaur` is the de-facto pick for Modrinth.
- **Release notes source** — GitHub's auto-generated-release-notes API (`previous_tag_name` field) versus a `gh` CLI script that lists merged PRs. Same output in most cases; the API is less code.
- **First release tag** — does the migration land with an initial `v1.0.0-mc1.20.1` cut, or just the machinery with the first real release left to the user's discretion?

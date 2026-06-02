# Releases — Branches, Tags, and Publishing

How this mod is versioned, branched, tagged, and shipped to GitHub Releases, CurseForge, and Modrinth.

The mod runs on two independent version axes — **Minecraft version** and **mod version** — and may be maintained on more than one Minecraft version at the same time (e.g. Forge 1.20.1 alongside NeoForge 1.21.x). The model below keeps those axes from contaminating each other, while staying easy and fast for a hobby project.

---

## TL;DR

| Thing | Shape | Example |
|---|---|---|
| Branch | `mc/<mcver>` — one per supported Minecraft version | `mc/1.20.1` |
| Mod version | Loose SemVer, **counted independently on each branch** | `1.2.0` |
| Git tag | `v<modver>-mc<mcver>` | `v1.2.0-mc1.20.1` |
| JAR filename | `circleofcraft-<modver>-mc<mcver>.jar` | `circleofcraft-1.2.0-mc1.20.1.jar` |
| Release trigger | Push a tag matching `v*-mc*` | `git push --tags` |
| Distribution | GitHub Releases + CurseForge + Modrinth (auto from CI) | — |
| Changelog | GitHub auto-generated from PR titles since the previous tag on the branch | — |

There is **no `main` branch.** The branch name *is* the Minecraft version it targets.

---

## Branch model

### One long-lived branch per Minecraft version

- `mc/1.20.1` — Forge 1.20.1.
- `mc/1.21.1` — NeoForge 1.21.1 (when it exists).
- Etc.

The branch name removes the ambiguity that a single moving `main` would create. Open a worktree, look at the prompt or at GitHub's branch selector — the Minecraft version is right there.

Each branch carries its own `gradle.properties` Minecraft/loader values. That file is the single source of truth on the branch for which Minecraft version it builds against.

### Why branch-per-version (not a shared multi-loader codebase)

Forge → NeoForge is not just a loader swap. Vanilla Minecraft itself changes between 1.20.1 and 1.21+ (data components replace NBT in 1.20.5+, recipe format, registry API). This mod is platform-heavy: Forge events, Mixins, custom block entities, criteria triggers, world saved data. The "shared common" surface after abstraction would be thin, and every new feature would pay a per-platform tax.

For a hobby project where iterations should be cheap, branch-per-version is by far the simplest. The downside — that a feature wanted on two MC versions has to be ported manually — is acceptable, and in fact often desirable (you might not want both branches in lockstep anyway).

If a fix or feature applies to multiple branches, **cherry-pick** between them. No requirement to keep branches in sync.

### Bumping to a new Minecraft version

Don't rename branches and don't reuse them. Fork a new one:

```bash
git checkout mc/1.20.1
git checkout -b mc/1.21.1
# do the port work on the new branch
git push -u origin mc/1.21.1
```

The old branch (`mc/1.20.1`) stays exactly where it is. You can keep shipping patches to it for as long as you care to support that Minecraft version. When you stop caring, you stop pushing — no ceremony.

Optionally switch GitHub's default branch (Settings → Branches) to the new one once it's where active work happens, so PR targets default sensibly.

---

## Versioning

### Mod version: loose SemVer

`MAJOR.MINOR.PATCH`, applied loosely. This is a content mod, not an API, so the contract is informal:

- **MAJOR** — large content drop (new dimension, new questline, world-format-affecting change).
- **MINOR** — new features, mobs, blocks, mechanics.
- **PATCH** — bug fixes, balance tweaks, asset corrections.

Don't agonise over the line. Pick the one that best signals to a user "is this worth updating for?".

### Independent counters per branch

Each `mc/<mcver>` branch has its own version counter. `v1.3.0-mc1.20.1` and `v1.3.0-mc1.21.1` are **unrelated tags**; matching numbers do not imply matching content. Patch-bumping a fix on the 1.20.1 branch has zero effect on what the 1.21 branch is allowed to call its next release.

This directly enables: "a bug only exists on one Minecraft version → fix and release that branch only, do nothing on the others."

---

## Tags

### Shape

```
v<modver>-mc<mcver>
```

Example: `v1.2.0-mc1.20.1`.

### Why the `-mc...` suffix is required (not redundant with the branch)

Git tags share a **single namespace across the whole repo**. You can have two branches named `mc/1.20.1` and `mc/1.21.1`, but you cannot have two tags both named `v1.2.0` pointing at different commits. The moment both branches independently reach `v1.2.0`, the second `git tag v1.2.0` collides with the first. The `-mc1.20.1` / `-mc1.21.1` suffix is what makes the version counters per-branch.

Bonus: tags are self-describing. `git tag --list` tells you what each release is for at a glance.

### Tag lifecycle

- Tags are created **only on `mc/<mcver>` branches** (never on a feature branch).
- Tag names always match `v<modver>-mc<mcver>`. The CI release workflow validates that the `-mc<mcver>` portion matches the branch's `gradle.properties` Minecraft version, and refuses to publish if not — so tagging from the wrong branch fails fast.
- Don't delete or move released tags. If a release goes wrong, fix forward with a patch bump.

---

## JAR filename

```
circleofcraft-<modver>-mc<mcver>.jar
```

Examples:

- `circleofcraft-1.2.0-mc1.20.1.jar`
- `circleofcraft-1.5.3-mc1.21.1.jar`

Computed by Gradle from `mod_version` (set by CI from the tag via the `MOD_VERSION` env var; falls back to a sensible local-dev default) and the branch's `gradle.properties` Minecraft version. A user who downloads the JAR standalone can still tell what it's for, without re-checking the page they got it from.

---

## Distribution

Releases publish to three channels in a single CI run:

- **GitHub Releases** — the JAR is attached to the release for the pushed tag.
- **CurseForge** — uploaded via the CurseForge upload API. Marked with the correct Minecraft version + loader (Forge / NeoForge).
- **Modrinth** — uploaded via the `modrinth/minotaur` Gradle plugin (or its API). Same Minecraft + loader metadata.

CurseForge and Modrinth attach Minecraft-version metadata to each uploaded file — that metadata is what their version filters use, and it comes from the branch's `gradle.properties`, not from the tag.

---

## Release flow

The whole process from your side, on a `mc/<mcver>` branch:

```bash
git tag v1.2.0-mc1.20.1
git push --tags
```

That's it. On a tag push matching `v*-mc*`, GitHub Actions:

1. Parses the tag to extract `MOD_VERSION` (`1.2.0`) and the expected MC version (`1.20.1`).
2. Validates the tag's MC suffix matches the branch's `gradle.properties` Minecraft version; aborts if not.
3. Runs `./gradlew build` with `MOD_VERSION` set, producing `circleofcraft-<modver>-mc<mcver>.jar`.
4. Generates release notes from PR titles merged into the branch since the previous tag on that branch.
5. Creates a GitHub Release for the tag and attaches the JAR.
6. Uploads to CurseForge with MC version + loader + release notes.
7. Uploads to Modrinth with the same metadata + release notes.

One-time setup required:

- `CURSEFORGE_TOKEN` and `MODRINTH_TOKEN` stored as GitHub repository secrets.
- CurseForge and Modrinth project IDs configured in the release workflow / `build.gradle`.
- The release workflow itself at `.github/workflows/release.yml`.

---

## Changelog

GitHub's auto-generated release notes (PR titles merged into the branch since the previous tag on the same branch) are the changelog. The CI workflow pipes the same text into the CurseForge and Modrinth release descriptions.

No `CHANGELOG.md` to maintain. **PR titles become the changelog**, so write them like you'd want them to appear in a release-notes list.

---

## Scenarios

### Normal release

You're on `mc/1.20.1`. Last release was `v1.2.0-mc1.20.1`. You've merged a handful of PRs since then. Tag, push, done:

```bash
git checkout mc/1.20.1
git pull
git tag v1.3.0-mc1.20.1
git push --tags
```

### Hotfix on one Minecraft version only

A bug only repros on 1.20.1. Fix it on `mc/1.20.1`, merge, then:

```bash
git tag v1.3.1-mc1.20.1
git push --tags
```

The 1.21 branch is untouched. No release goes out for it. No version bump there.

### Bug that affects both supported Minecraft versions

Fix on one branch first (typically the one where you reproduced it), merge, cherry-pick to the other:

```bash
git checkout mc/1.21.1
git cherry-pick <fix-commit-sha>
# resolve any conflicts (they tell you what diverged), commit
```

Then patch-bump and tag each branch independently. The two tags may have different version numbers — that's fine.

### Pre-release / beta

A SemVer pre-release suffix on the mod version drives the release channel across all three platforms. The **tag is the single source of truth** — nothing else feeds the channel decision, so a "clean" tag can never end up flagged as a pre-release.

| Tag | Channel | GitHub | CurseForge | Modrinth |
|---|---|---|---|---|
| `v1.3.0-alpha.1-mc1.20.1` | alpha | pre-release | Alpha | `alpha` |
| `v1.3.0-beta.2-mc1.20.1` | beta | pre-release | Beta | `beta` |
| `v1.3.0-mc1.20.1` | release | normal release | Release | `release` |

- Only `-alpha` and `-beta` are recognised (each with an optional dot-number: `-beta`, `-beta.1`, `-beta.2`, …). Use the number to iterate pre-release builds — CurseForge/Modrinth reject duplicate version uploads and tags can't be reused, so `-beta.1` → `-beta.2` is how you ship a second beta before the stable `1.3.0`.
- GitHub has only a single `prerelease` boolean (no alpha-vs-beta distinction), so both alpha and beta set it to `true`. The finer split lives on CurseForge/Modrinth.
- Any other pre-release suffix (e.g. `-rc.1`) **fails the build** rather than silently publishing as a full release. If you ever want `rc`, map it to Beta in the workflow's channel `case`.

Flow is otherwise unchanged — tag and push:

```bash
git tag v1.3.0-beta.1-mc1.20.1
git push --tags
```

### Re-running a failed publish

The publish workflow is idempotent on the GitHub Release (it won't double-create). CurseForge and Modrinth reject duplicate version uploads — that's the safety net. If the workflow failed partway through:

1. Look at the workflow log to see which channel(s) succeeded.
2. If you need to retry from scratch: delete the GitHub Release (via UI or `gh release delete`), delete the tag locally and on the remote, retag the same commit, push.
3. If only CF or Modrinth failed and you want to keep the GitHub Release: re-run the failed job from the Actions UI.

Avoid moving an already-published tag to a different commit. Fix forward with a patch bump if real damage is done.

### Adding a third Minecraft version

Identical to the first port. Branch off the version you want to start from (`mc/1.21.1`, usually), do the port, push as `mc/1.22.1` (or whatever), CI picks up the branch's `gradle.properties` and publishes correctly without any workflow changes.

---

## Notes for future maintainers

- The release workflow knows nothing project-specific beyond "build a Forge/NeoForge mod and upload the resulting JAR." Branch and tag conventions encode everything else.
- `gradle.properties` on each branch is the source of truth for that branch's Minecraft version. Never hard-code Minecraft versions into the workflow.
- If you ever feel the urge to make a "shared common" multi-loader project: re-read the "Why branch-per-version" section. The decision was deliberate and the cost of reversing it later is small (just merge histories), so wait for clear evidence rather than acting on intuition.

---

## First-time publisher setup

The release workflow publishes to GitHub Releases out of the box. Enable CurseForge and Modrinth by completing these one-time steps (in any order). Until they're done, the workflow simply skips the missing channels.

### CurseForge

1. Create the project at https://www.curseforge.com/minecraft/mc-mods (or claim ownership of the existing one).
2. Note the numeric project ID (visible on the project page under "About Project").
3. Generate an API token: https://legacy.curseforge.com/account/api-tokens → "Generate New Token". Copy the token immediately.
4. In the GitHub repo: Settings → Secrets and variables → Actions →
   - **Variables** tab: add `CURSEFORGE_PROJECT_ID` = the numeric ID.
   - **Secrets** tab: add `CURSEFORGE_TOKEN` = the API token.

### Modrinth

1. Create the project at https://modrinth.com/dashboard/projects → "Create a project". Choose `Mod` and the supported game versions/loaders.
2. Note the project's slug or ID (visible in the URL: `https://modrinth.com/mod/<slug>`).
3. Generate a Personal Access Token at https://modrinth.com/settings/pats with at least the `Create versions` scope.
4. In the GitHub repo: Settings → Secrets and variables → Actions →
   - **Variables** tab: add `MODRINTH_PROJECT_ID` = the slug or ID.
   - **Secrets** tab: add `MODRINTH_TOKEN` = the PAT.

### Verify

After both are configured, push a throwaway pre-release tag (e.g. `v0.0.1-mc1.20.1` if no real release has happened yet) and watch the workflow. All three publish steps should succeed. If you need to retry, delete the tag and the GitHub Release, then retag and repush — CurseForge and Modrinth reject duplicate version uploads, which is the safety net against accidents.

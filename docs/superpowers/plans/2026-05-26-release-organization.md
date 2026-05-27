# Release Organization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Spec:** `docs/superpowers/specs/2026-05-26-release-organization-design.md`
**Reference (steady-state model):** `docs/RELEASES.md`
**Status:** COMPLETED 2026-05-27 (see Execution Notes at the end).

**Goal:** Migrate the repo from a single `main` branch with hard-coded mod/Minecraft versions to a branch-per-MC-version model with tag-triggered GitHub Actions publishing to GitHub Releases, CurseForge, and Modrinth.

**Architecture:** Gradle reads `mod_version` from a `MOD_VERSION` env var (with a dev fallback); Minecraft/Forge versions live in `gradle.properties` as the per-branch source of truth; the shippable JAR (the JarInJar `jarJar` artifact, which bundles MixinExtras) is named `circleofcraft-<modver>-mc<mcver>.jar` via Gradle's `archiveClassifier`. A new `release.yml` workflow fires on tag push matching `v*-mc*`, validates the tag's MC suffix against `gradle.properties`, builds, publishes a GitHub Release with auto-generated notes, and conditionally uploads to CurseForge + Modrinth via their HTTP APIs (gated on repository variables so the workflow works before those projects exist). Finally, `main` is renamed to `mc/1.20.1` and the CI filter is updated to `mc/**`.

**Tech Stack:** Forge 1.20.1 (47.4.18), Java 17, Gradle (Forge plugin 6.0–6.2), GitHub Actions, Palantir Java Format via Spotless. CurseForge upload API + Modrinth `/v2/version` API via `curl`.

**Important conventions (from `CLAUDE.md`):**
- `JAVA_HOME` must point to JDK 17: `export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home`.
- Never commit secrets.

---

## File Structure

**Modify:**
- `gradle.properties` — gain `minecraft_version`, `minecraft_version_range`, `forge_version`, `forge_version_range`, `loader_version_range`.
- `build.gradle` — read those properties; read `project.version` from `MOD_VERSION` env var; set `archiveClassifier` on the `jarJar` task so the shippable JAR name includes the MC version.
- `.github/workflows/ci.yml` — update `branches:` filters from `main` to `mc/**`.

**Create:**
- `.github/workflows/release.yml` — tag-triggered publish workflow.

**Git operations:**
- Rename local branch `main` → `mc/1.20.1`; push; set GitHub default branch; delete remote `main`.

**Manual prerequisites (documented, not coded — Task 6):**
- Create CurseForge + Modrinth projects; add `CURSEFORGE_TOKEN`/`MODRINTH_TOKEN` secrets and `CURSEFORGE_PROJECT_ID`/`MODRINTH_PROJECT_ID` variables.

---

## Task 1: Move Minecraft/Forge constants into `gradle.properties`

Pure refactor — resulting JAR byte-identical.

- [x] Append `minecraft_version=1.20.1`, `minecraft_version_range=[1.20.1,1.21)`, `forge_version=47.4.18`, `forge_version_range=[47,)`, `loader_version_range=[47,)` to `gradle.properties`.
- [x] In `build.gradle`: `mappings channel: 'official', version: minecraft_version`.
- [x] In `build.gradle`: `minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"`.
- [x] In `build.gradle` `replaceProperties`: replace the five literal values with the property references.
- [x] Verify `./gradlew build` succeeds and `mods.toml` still has `loaderVersion="[47,)"` and `versionRange="[1.20.1,1.21)"`.
- [x] Commit.

## Task 2: Read `project.version` from a `MOD_VERSION` env var

- [x] Replace `version = '1.0.0'` with `version = System.getenv('MOD_VERSION') ?: '0.0.0-dev'`.
- [x] Verify `MOD_VERSION=9.9.9 ./gradlew properties | grep '^version:'` → `9.9.9`; unset → `0.0.0-dev`.
- [x] Commit.

## Task 3: Add `-mc<mcver>` classifier to the shippable JAR

**Correction made during execution:** the classifier must go on the **`jarJar`** task, not the plain `jar` task. This mod bundles `mixinextras-forge` into production via Forge JarInJar (it's `compileOnly` otherwise). The bundled dependency lands in the `jarJar` task's output, not the plain `jar`. Naming the plain `jar` with the classifier produced a MixinExtras-less JAR that would crash at runtime on the `@WrapOperation` mixins (e.g. `OutlandsFluidMixin`). The release workflow ships `circleofcraft-<modver>-mc<mcver>.jar`, so that filename must be the complete `jarJar` artifact.

- [x] In `build.gradle`, after the `base { archivesName = 'circleofcraft' }` block:
  ```groovy
  tasks.named('jarJar').configure {
      archiveClassifier = "mc${minecraft_version}"
  }
  ```
- [x] Verify `MOD_VERSION=1.0.0 ./gradlew clean build` produces `build/libs/circleofcraft-1.0.0-mc1.20.1.jar` AND that this JAR contains `META-INF/jarjar/mixinextras-forge-0.3.5.jar`, all 408 mod classes, and `META-INF/mods.toml`. The plain `jar` stays `circleofcraft-1.0.0.jar` (no collision).
- [x] Verify dev fallback name: `circleofcraft-0.0.0-dev-mc1.20.1.jar`.
- [x] Commit.

## Task 4: Add the release workflow

Create `.github/workflows/release.yml` (tag-triggered on `v*-mc*`). Injection-safe (`${GITHUB_REF_NAME}` env form, not `${{ github.ref_name }}` template injection). Steps: checkout (fetch-depth 0) → parse tag (regex extracting mod version + MC version, accepting optional `-rc<n>` pre-release segment) → validate MC suffix against `gradle.properties` and infer loader (forge for 1.20, neoforge otherwise) → setup Java 17 + Gradle → build with `MOD_VERSION` → locate `circleofcraft-<modver>-mc<mcver>.jar` → find previous tag on this MC line → `gh release create --generate-notes` (with `--notes-start-tag` when a previous tag exists) → conditional CurseForge upload (gated on `vars.CURSEFORGE_PROJECT_ID` + `secrets.CURSEFORGE_TOKEN`, resolves gameVersion IDs, multipart upload) → conditional Modrinth upload (gated on `vars.MODRINTH_PROJECT_ID` + `secrets.MODRINTH_TOKEN`, `/v2/version` multipart with matching `file_parts`).

- [x] Create the workflow file verbatim (the full YAML is in `.github/workflows/release.yml`).
- [x] Verify YAML parses; tag-parse regex matches `v1.2.0-mc1.20.1` and `v1.2.0-rc1-mc1.20.1`, rejects `v1.2.0`; `grep '^minecraft_version=' gradle.properties` → `1.20.1`.
- [x] Commit.

## Task 5: Rename `main` → `mc/1.20.1`, update CI filter

- [x] Update `.github/workflows/ci.yml` `branches:` from `[ main ]` to `[ 'mc/**' ]` (push + pull_request). Commit.
- [x] `git branch -m main mc/1.20.1`.
- [x] `git push -u origin mc/1.20.1`.
- [x] Change GitHub default branch to `mc/1.20.1` (see Execution Notes — required a manual UI action by the maintainer because available credentials lacked Administration scope).
- [x] `git push origin --delete main`.
- [x] Verify: local on `mc/1.20.1` tracking `origin/mc/1.20.1`; remote heads show `mc/1.20.1` and no `main`; default branch is `mc/1.20.1`.

## Task 6: Document manual prerequisites for full publishing

- [x] Append a "First-time publisher setup" section to `docs/RELEASES.md` (CurseForge + Modrinth project creation, token generation, repo secrets/variables, verify step). Commit.

---

## Verification — end-to-end smoke

The first **real** release is the maintainer's call; this plan stops at "machinery is ready."

- `MOD_VERSION=0.0.1 ./gradlew clean build` → `build/libs/circleofcraft-0.0.1-mc1.20.1.jar` exists and contains the JarInJar'd MixinExtras.
- `grep -E '^(minecraft_version|forge_version)' gradle.properties` → `1.20.1` / `47.4.18`.
- Optional: push a throwaway `v0.0.1-mc1.20.1` tag, confirm the GitHub Release is created with the JAR attached (CF/Modrinth steps skip until secrets are configured), then delete the test release + tag.

---

## What's NOT in this plan

- Cutting the first real release.
- The NeoForge 1.21 port (the workflow auto-infers `loader=neoforge` on a future `mc/1.21.x` branch).
- Pre-release / beta promotion (tag regex accepts `-rc<n>` but the workflow always marks releases as `release` type).

---

## Execution Notes (2026-05-27)

Recorded for the historical record; these are things discovered during implementation that the original plan didn't anticipate.

1. **Environment: corporate TLS interception broke all Gradle downloads.** Builds failed with `PKIX path building failed` reaching `libraries.minecraft.net`. Cause: a Cato Networks SASE proxy presents a re-signed cert (`CN=Cato Networks Root CA`) that wasn't in the JDK truststore. Fix: exported the Cato root CA from the macOS System keychain and imported it into the homebrew openjdk@17 `cacerts` (a `cacerts.bak-<timestamp>` backup was left next to it). After that, `./gradlew build` succeeded. This is a per-machine environment fix, not a repo change.

2. **Task 3 mechanism corrected** from the plain `jar` task to the `jarJar` task — see the Task 3 section above. Without this, the shipped JAR would crash at runtime.

3. **Task 5 default-branch change required a manual UI step.** The `gh` CLI was authenticated under a different account (`ronm-cye`) that can't resolve `ron1196/TheLionKing`, and the PAT embedded in the `origin` remote URL has Contents (push) permission but not Administration, so the REST `PATCH .../repos/...{default_branch}` returned 403. The maintainer switched the default branch in the GitHub UI; the branch creation and `main` deletion were done via git (push access suffices for those). Deleting `main` was non-destructive: `mc/1.20.1` contains the entire `main` history.

4. **Security observation (not fixed):** the `origin` remote URL stores a GitHub PAT in plaintext (`https://ron1196:github_pat_...@github.com/...`). Flagged to the maintainer to rotate the token and switch to SSH or a credential helper.

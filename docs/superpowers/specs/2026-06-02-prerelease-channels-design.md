# Auto alpha/beta release channels — design

## Goal

Let a single pushed git tag decide whether a release is a full **Release**, a
**Beta**, or an **Alpha**, and propagate that decision consistently to all three
publish targets (GitHub Releases, CurseForge, Modrinth) — with no extra commands
or manual UI steps.

## Signal: the tag suffix is the single source of truth

The channel is derived **only** from a SemVer pre-release suffix on the mod
version portion of the tag. Nothing else feeds the decision, so contradictory
states (e.g. a "clean" tag flagged as pre-release) are impossible.

| Tag | Mod version | Channel | GitHub | CurseForge | Modrinth |
|---|---|---|---|---|---|
| `v1.3.0-alpha.1-mc1.20.1` | `1.3.0-alpha.1` | alpha | pre-release | Alpha | `alpha` |
| `v1.3.0-beta.2-mc1.20.1` | `1.3.0-beta.2` | beta | pre-release | Beta | `beta` |
| `v1.3.0-mc1.20.1` | `1.3.0` | release | normal release | Release | `release` |

- Suffixes accept an optional dot-number: `-alpha`, `-alpha.1`, `-beta`,
  `-beta.3`. The number lets pre-releases iterate (`-beta.1` → `-beta.2`)
  without colliding — CurseForge/Modrinth reject duplicate version uploads and
  git tags can't be reused, so a bare-only scheme would dead-end after one beta
  build.
- Any non-`release` channel sets GitHub's single `prerelease` boolean to `true`.
  GitHub has no alpha-vs-beta distinction; that finer split lives only on
  CurseForge/Modrinth, which have a real three-tier channel.
- Only `alpha` and `beta` are modelled. `rc` is intentionally **not** a
  recognised suffix; if needed later it would map to Beta.

## Channel derivation rule

From the already-parsed `MOD_VERSION`:

- ends with `-alpha` or `-alpha.<n>` → `alpha`
- ends with `-beta` or `-beta.<n>` → `beta`
- no recognised pre-release suffix → `release`
- a pre-release suffix that is neither `alpha` nor `beta` (e.g. `-rc.1`,
  `-foo`) → **fail the build** with a clear message, rather than silently
  publishing as a full release. This keeps the "tag is the source of truth"
  contract honest.

## Changes

### `.github/workflows/release.yml`

1. In the **Parse tag** step, after extracting `MOD_VERSION`, derive a
   `channel` output (`alpha` | `beta` | `release`) using the rule above, and a
   `prerelease` boolean (`true` for alpha/beta, `false` for release). Reject an
   unrecognised pre-release suffix.
2. **Create GitHub Release** step: add `--prerelease` to the `gh release create`
   args when `channel != release`.
3. **Upload to CurseForge** step: replace the hard-coded
   `releaseType: "release"` with the channel value. CurseForge's API accepts
   exactly `release` / `beta` / `alpha`, so the channel string maps 1:1.
4. **Upload to Modrinth** step: replace the hard-coded `version_type: "release"`
   with the channel value. Modrinth's API accepts exactly `release` / `beta` /
   `alpha`, so the channel string maps 1:1.

The tag regex already permits the suffix
(`^v([0-9]+\.[0-9]+\.[0-9]+(-[A-Za-z0-9.]+)?)-mc...$`), so no regex change is
required.

### `docs/RELEASES.md`

Replace the "Pre-release / beta" scenario (currently "Not currently set up")
with the documented convention: the suffix table above, example tags, the
iterate-with-numbers note, and the "channel is the single source of truth"
guarantee.

## Out of scope (deliberate)

- `workflow_dispatch` manual channel override — deferred until a concrete need
  (e.g. wanting a bare `1.3.0` on the beta channel) appears.
- `rc` channel.
- Auto-inferring channel from the version number (e.g. `0.x` ⇒ alpha).

## Verification

- Unit-test the channel derivation by running the bash rule over the three
  example tags plus an `-rc.1` tag (expect failure) locally before pushing.
- First real exercise: push a throwaway `v0.0.1-beta.1-mcX` style tag on a test
  and confirm GitHub shows the pre-release badge and CF/Modrinth (if configured)
  land on the Beta channel.

# The Lion King Mod — Forge 1.20.1

## Quick Reference

- **Mod ID:** `circleofcraft`
- **Package:** `io.github.ron1196.circleofcraft`
- **Minecraft:** 1.20.1 | **Forge:** 47.4.18 | **Java:** 17
- **Mappings:** Official (Mojang)
- **Future:** plan is to migrate to NeoForge when upgrading to 1.21.x

## Build & Run

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew build          # Build the mod JAR
./gradlew compileJava    # Compile only (fast check)
./gradlew runClient      # Launch Minecraft with the mod
./gradlew runServer      # Launch dedicated server
./gradlew runData        # Run data generators
./gradlew compileJava --offline  # Compile with cached deps (no network)
```

## Formatting

Java is auto-formatted by Spotless (Palantir). Run `./gradlew spotlessApply` after Java edits.

## Code Conventions

### Data Files Per Block
Each block needs: blockstate JSON, block model JSON, item model JSON, loot table JSON, lang entry. Recipes go in `data/circleofcraft/recipes/`.

### Nullability Annotations
**Always add `@NotNull` to every parameter and return type of an `@Override` method** unless the value can genuinely be null (use `@Nullable` then). Use `org.jetbrains.annotations.NotNull` / `org.jetbrains.annotations.Nullable` exclusively.

### Method Signature Formatting
When a method signature is too long for one line, put each parameter on its own line with 8-space indent, closing `) {` on its own line.

### WorldData Access
**Always call `WorldData.get(anyServerLevel)`** — the method internally routes to the overworld's data storage. Never bypass this by accessing `level.getDataStorage()` directly. Quest state and world flags must be shared across all dimensions.

### Quest State
**Never store boolean flags for quest state that can be derived from the quest stage.** Use `QuestlineManager.getStage()` as the single source of truth. Use `EnumSet` for stage range checks — never compare `ordinal()`.

**Quest action helpers** (`quest/actions/`): Each questline has one helper class with an idempotent `ensureWorldState(level, stage)` method — a switch that maps stage → world state (entity spawn/despawn, block mutation). Each sub-method checks before acting (idempotent). This is the **single source of truth** for stage → world state.

Called from three places (zero duplication):
1. **`customTransition`** — calls `ensureWorldState()` + adds presentation (particles, sounds, chat). Immediate.
2. **`/coc quest set` command** — calls `ensureWorldState()` directly. Instant.
3. **Entity tick fallback** — calls `ensureWorldState()` every ~100 ticks. Handles chunk reload edge cases only.

Use `/quest-skip-check` to verify all stages follow this pattern.

### Adding Quest Stages
When inserting new stages into `OutlandsQuestline.Stage`, check if `TREE_OCCUPATION_STAGES` in `OutlandsQuestActions.java` needs updating. Missing a stage causes Rafiki to spawn prematurely or Zira's tree corruption to toggle incorrectly.

### NPC Chat
Use `ChatHelper.sendNpcMessage(player, name, message)` for all NPC dialogue — never inline `§e<Name> §f` formatting. For broadcasts use `ChatHelper.broadcastNpcMessage(level, name, message)`. Direction utilities are in `DirectionHelper`.

### Testing Commands
Use `/coc quest` for quest testing:
- `/coc quest info <questId>` — show current stageKey
- `/coc quest advance <questId>` — skip to next stageKey (bypasses triggers/items)
- `/coc quest set <questId> <stageKey>` — jump to specific stageKey
- `/coc quest reset <questId>` — reset to first stageKey

Quest IDs: `rafiki`, `outlands`. Stage names match the enum values (e.g., `FIND_RAFIKI`, `COLLECT_BONES`).

### Workaround Policy
**Never use temporary workarounds without tracking them.** Every "for now" substitution must be filed as a GitHub issue.

## Key Files

| File                          | Purpose                                  |
|-------------------------------|------------------------------------------|
| `event/CommonEvents.java`    | Entity attributes + spawn placement rules |
| `event/ModForgeEvents.java` | Forge bus events (combat, NPC interaction, breeding, ticks) |
| `data/WorldData.java`        | World-level saved data (overworld storage), quest-derived state |
| `data/ModCriteriaTriggers.java` | Custom advancement triggers       |

## References

Long-form context lives in `docs/*.md` and is listed below as **summary + "read before X" trigger** — only the linked file is loaded when the trigger fires.

**Bar:** architecturally significant subsystems, frameworks, or cross-cutting flows. Not feature TODOs, per-block scaffolding, or single-content checklists — those live in `docs/` but don't surface here.

- **Automated testing** (JUnit for pure-Java, Mojang Game Tests for in-world behaviour, runners, markers, the `@PrefixGameTestTemplate(false)` / structure NBT / `assertTrue` vs `fail` gotchas): [`docs/AUTOMATED_TESTING.md`](docs/AUTOMATED_TESTING.md). Read before writing or moving a test, or changing CI test jobs.
- **Releases, branches, and tags** (branch-per-MC-version model `mc/<mcver>`, loose SemVer per branch, tag scheme `v<modver>-mc<mcver>`, JAR filename, GitHub/CurseForge/Modrinth publishing flow, cross-branch cherry-picks): [`docs/RELEASES.md`](docs/RELEASES.md). Read before cutting a release, starting a new MC version port, or changing the release workflow.
- **Workaround / TODO backlog**: [GitHub Issues](https://github.com/ron1196/circle-of-craft/issues). Read before adding a "for now" substitution — every workaround must be filed here.

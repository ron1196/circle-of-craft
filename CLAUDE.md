# The Lion King Mod — NeoForge 1.20.1

## Quick Reference

- **Mod ID:** `thelionking`
- **Package:** `io.github.ron1196.thelionking`
- **Minecraft:** 1.20.1 | **NeoForge:** 47.1.x | **Java:** 17
- **Mappings:** Official (Mojang)

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

## Code Conventions

### Data Files Per Block
Each block needs: blockstate JSON, block model JSON, item model JSON, loot table JSON, lang entry. Recipes go in `data/thelionking/recipes/`.

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
2. **`/lk quest set` command** — calls `ensureWorldState()` directly. Instant.
3. **Entity tick fallback** — calls `ensureWorldState()` every ~100 ticks. Handles chunk reload edge cases only.

Use `/quest-skip-check` to verify all stages follow this pattern.

### Adding Quest Stages
When inserting new stages into `OutlandsQuestline.Stage`, check if `TREE_OCCUPATION_STAGES` in `OutlandsQuestActions.java` needs updating. Missing a stage causes Rafiki to spawn prematurely or Zira's tree corruption to toggle incorrectly.

### NPC Chat
Use `ChatHelper.sendNpcMessage(player, name, message)` for all NPC dialogue — never inline `§e<Name> §f` formatting. For broadcasts use `ChatHelper.broadcastNpcMessage(level, name, message)`. Direction utilities are in `DirectionHelper`.

### Testing Commands
Use `/lk quest` for quest testing:
- `/lk quest info <questId>` — show current stageKey
- `/lk quest advance <questId>` — skip to next stageKey (bypasses triggers/items)
- `/lk quest set <questId> <stageKey>` — jump to specific stageKey
- `/lk quest reset <questId>` — reset to first stageKey

Quest IDs: `rafiki`, `outlands`. Stage names match the enum values (e.g., `FIND_RAFIKI`, `COLLECT_BONES`).

### Workaround Policy
**Never use temporary workarounds without tracking them.** Every "for now" substitution must be recorded in `docs/TODO_WORKAROUNDS.md`.

## Key Files

| File                          | Purpose                                  |
|-------------------------------|------------------------------------------|
| `event/CommonEvents.java`    | Entity attributes + spawn placement rules |
| `event/LionKingForgeEvents.java` | Forge bus events (combat, NPC interaction, breeding, ticks) |
| `data/WorldData.java`        | World-level saved data (overworld storage), quest-derived state |
| `data/LionKingCriteriaTriggers.java` | Custom advancement triggers       |

## Related Docs

- `docs/MIGRATION_AUDIT.md` — Full audit of what's ported vs missing, with priority roadmap
- `docs/TODO_WORKAROUNDS.md` — Tracked temporary substitutions and placeholder items

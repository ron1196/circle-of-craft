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

### Registration
All registries use `DeferredRegister` in dedicated classes under `registry/`. Register to the mod event bus in `TheLionKingMod` constructor.

```java
public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, TheLionKingMod.MOD_ID);
public static final RegistryObject<Block> PRIDESTONE = BLOCKS.register("pridestone",
    () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 10.0F)));
```

### Naming
- Registry names: `snake_case` (e.g., `corrupt_pridestone`, `mango_planks`)
- Java classes: `PascalCase`

### Data Files Per Block
Each block needs: blockstate JSON, block model JSON, item model JSON, loot table JSON, lang entry. Recipes go in `data/thelionking/recipes/`.

### Nullability Annotations
**Always add `@NotNull` to every parameter and return type of an `@Override` method** unless the value can genuinely be null (use `@Nullable` then). NeoForge/Minecraft classes are annotated with both `@ParametersAreNonnullByDefault` (parameters) and `@MethodsReturnNonnullByDefault` (return types), so un-annotated overrides produce IDE warnings for both. Use `org.jetbrains.annotations.NotNull` / `org.jetbrains.annotations.Nullable` exclusively.

```java
@Override
public @NotNull InteractionResult use(
        @NotNull BlockState state,
        @NotNull Level level,
        @NotNull BlockPos pos,
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull BlockHitResult hit
) { ... }
```

### Method Signature Formatting
When a method signature is too long to fit on one line, put each parameter on its own line with 8-space indent, and the closing `) {` on its own line:

```java
public void onRemove(
        @NotNull BlockState state,
        @NotNull Level level,
        @NotNull BlockPos pos,
        @NotNull BlockState newState,
        boolean isMoving
) {
```

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
When inserting new stages into `OutlandsQuestline.Stage`, check if `TREE_OCCUPATION_STAGES` in `WorldData.java` needs updating. Missing a stage causes Rafiki to spawn prematurely or Zira's tree corruption to toggle incorrectly.

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

## Project Structure

```
src/main/java/io/github/ron1196/thelionking/
  TheLionKingMod.java        — Main mod class, event bus registration
  registry/                  — DeferredRegister classes (LionKingBlocks, LionKingItems, EntityTypes, etc.)
  block/                     — Block subclasses
  block/entity/              — BlockEntity classes
  item/                      — Item subclasses
  item/tier/                 — LKToolTiers, LKArmorMaterials
  entity/animal/             — Passive mobs (LKAnimal base class)
  entity/hostile/            — Hostile mobs
  entity/npc/                — Named NPCs (Rafiki, Simba, etc.)
  entity/projectile/         — Darts, spears, bombs
  entity/                    — Transient entities (RugEntity, PumbaaExplosionEntity)
  entity/ai/                 — Custom AI goals
  world/dimension/           — Teleporter
  world/feature/             — Custom worldgen features
  quest/                     — Quest system (Questline, QuestlineManager, QuestlineRegistry)
  quest/questline/           — Questline definitions (RafikiQuestline, OutlandsQuestline)
  quest/stage/                  — QuestObjective, QuestTrigger, ClaimableReward, StageId
  command/                   — Debug/testing commands (LionKingCommands)
  data/                      — WorldData (SavedData), PlayerData, custom triggers
  menu/                      — Container menus
  network/                   — Packet handling (Networking, SimbaSitPacket, QuestSyncPacket, QuestCheckPacket, etc.)
  event/                     — Event handlers (CommonEvents, ClientEvents, LionKingForgeEvents)
  client/gui/                — Screens
  client/model/              — Entity models
  client/renderer/           — Entity & block entity renderers
  client/sound/              — Music handler

src/main/resources/
  assets/thelionking/
    blockstates/             — Blockstate JSONs
    lang/en_us.json          — All translations
    models/block/            — Block model JSONs
    models/item/             — Item model JSONs
    sounds.json              — Sound event definitions
    sounds/entity/<mob>/     — Entity sound files
    sounds/block/            — Block sound files
    sounds/item/             — Item sound files
    sounds/music/            — Music tracks (streamed)
    textures/block/          — Block textures
    textures/item/           — Item textures
    textures/entity/         — Entity textures
    textures/gui/            — GUI textures
  data/thelionking/
    advancements/            — 33 advancement JSONs
    dimension/               — 3 dimensions (pride_lands, outlands, upendi)
    dimension_type/          — Dimension type configs
    loot_tables/             — Block & entity loot tables
    recipes/                 — Smelting/cooking recipes
    worldgen/biome/          — 14 biome JSONs
    worldgen/configured_feature/
    worldgen/placed_feature/
```

## Key Files

| File                          | Purpose                                  |
|-------------------------------|------------------------------------------|
| `registry/LionKingBlocks.java` | All block registrations                |
| `registry/LionKingItems.java`  | All item registrations + block items   |
| `registry/EntityTypes.java`    | All entity type registrations          |
| `registry/LionKingSoundEvents.java` | Sound event registrations         |
| `registry/Features.java`     | Custom worldgen feature registrations    |
| `event/CommonEvents.java`    | Entity attributes + spawn placement rules |
| `event/ClientEvents.java`    | Renderers, models, GUI screens           |
| `event/LionKingForgeEvents.java` | Forge bus events (combat, NPC interaction, breeding, ticks) |
| `network/Networking.java`    | SimpleChannel packet registration        |
| `data/WorldData.java`        | World-level saved data (overworld storage), quest-derived state |
| `util/ChatHelper.java`       | NPC message formatting (`sendNpcMessage`, `broadcastNpcMessage`) |
| `util/DirectionHelper.java`  | Compass direction utility |
| `command/LionKingCommands.java` | Debug commands: `/lk quest`, `/lk pridelands`, etc. |
| `data/LionKingCriteriaTriggers.java` | Custom advancement triggers       |
| `sounds.json`                | Maps sound event names to file paths     |
| `lang/en_us.json`            | All translatable strings                 |

## Related Docs

- `docs/MIGRATION_AUDIT.md` — Full audit of what's ported vs missing, with priority roadmap
- `docs/TODO_WORKAROUNDS.md` — Tracked temporary substitutions and placeholder items

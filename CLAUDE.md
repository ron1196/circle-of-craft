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

### Workaround Policy
**Never use temporary workarounds without tracking them.** Every "for now" substitution must be recorded in `docs/TODO_WORKAROUNDS.md`.

## Project Structure

```
src/main/java/io/github/ron1196/thelionking/
  TheLionKingMod.java        — Main mod class, event bus registration
  registry/                  — DeferredRegister classes (LKBlocks, LKItems, LKEntityTypes, etc.)
  block/                     — Block subclasses
  block/entity/              — BlockEntity classes
  item/                      — Item subclasses
  item/tier/                 — LKToolTiers, LKArmorMaterials
  entity/animal/             — Passive mobs (LKAnimal base class)
  entity/hostile/            — Hostile mobs
  entity/npc/                — Named NPCs (Rafiki, Simba, etc.)
  entity/projectile/         — Darts, spears, bombs
  entity/ai/                 — Custom AI goals
  world/dimension/           — Teleporter
  world/feature/             — Custom worldgen features
  quest/                     — Quest system (LKQuestBase, LKQuestRafiki, LKQuestOutlands)
  data/                      — LKLevelData (SavedData), custom recipes
  menu/                      — Container menus
  network/                   — Packet handling (LKNetworking, SimbaSitPacket, QuestSyncPacket, QuestCheckPacket)
  event/                     — Event handlers (LKCommonEvents, LKClientEvents, LKForgeEvents)
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
| `registry/LKBlocks.java`     | All block registrations                  |
| `registry/LKItems.java`      | All item registrations + block items     |
| `registry/LKEntityTypes.java`| All entity type registrations            |
| `registry/LKSoundEvents.java`| Sound event registrations                |
| `registry/LKFeatures.java`   | Custom worldgen feature registrations    |
| `event/LKCommonEvents.java`  | Entity attribute registration            |
| `event/LKClientEvents.java`  | Renderers, models, GUI screens           |
| `event/LKForgeEvents.java`   | Forge bus events (combat, NPC interaction, ticks) |
| `network/LKNetworking.java`  | SimpleChannel packet registration        |
| `data/LKLevelData.java`      | World-level saved data (quests, state)   |
| `data/LKCriteriaTriggers.java`| Custom advancement triggers             |
| `sounds.json`                | Maps sound event names to file paths     |
| `lang/en_us.json`            | All translatable strings                 |

## Related Docs

- `docs/MIGRATION_AUDIT.md` — Full audit of what's ported vs missing, with priority roadmap
- `docs/TODO_WORKAROUNDS.md` — Tracked temporary substitutions and placeholder items

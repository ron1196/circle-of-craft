# The Lion King Mod — NeoForge 1.20.1

## Project Overview

A massive total-conversion Minecraft mod ported from 1.6.4 Forge to 1.20.1 NeoForge.
Original code: `old/code/`, Original assets: `old/assets/`.

## Stack

- **Minecraft:** 1.20.1
- **Mod Loader:** NeoForge 47.1.x
- **Java:** 17
- **Build:** Gradle 8.1.1 with NeoGradle
- **Mappings:** Official (Mojang)
- **Mod ID:** `thelionking`
- **Package:** `io.github.ron1196.thelionking`

## Key Conventions

### Registration Pattern
Use `DeferredRegister` for all registries. Register in dedicated classes under `registry/`:
```java
public class LKBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, TheLionKingMod.MOD_ID);
    public static final RegistryObject<Block> PRIDESTONE = BLOCKS.register("pridestone",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 10.0F)));
}
```
Register the DeferredRegister to the mod event bus in the main mod class constructor.

### Naming
- Block/item registry names: `snake_case` (e.g., `corrupt_pridestone`, `mango_planks`)
- Java classes: `PascalCase` with `LK` prefix for mod-specific base classes
- Package structure follows the plan in the project spec

### Metadata Splitting
Old metadata blocks become separate block IDs. Example:
- `pridestone` meta 0,1 → `pridestone`, `corrupt_pridestone`
- `planks` meta 0-5 → `acacia_planks`, `rainforest_planks`, `mango_planks`, `passion_planks`, `banana_planks`, `deadwood_planks`

### Data Files Per Block
Each block needs: blockstate JSON, block model JSON, item model JSON, loot table JSON, lang entry.
Recipes go in `data/thelionking/recipes/`.

### Asset Paths
- Block textures: `assets/thelionking/textures/block/`
- Item textures: `assets/thelionking/textures/item/`
- Entity textures: `assets/thelionking/textures/entity/`
- GUI textures: `assets/thelionking/textures/gui/`
- Sounds: `assets/thelionking/sounds/`

### Old Code Reference
- Main class (all registrations): `old/code/common/mod_LionKing.java`
- Entity AI template: `old/code/common/LKEntityLionBase.java`
- Block entity template: `old/code/common/LKTileEntityGrindingBowl.java`
- Biome template: `old/code/common/LKPrideLandsBiome.java`
- Quest system: `old/code/common/LKQuestBase.java`

## Build & Run

```bash
./gradlew build          # Build the mod JAR
./gradlew runClient      # Launch Minecraft with the mod
./gradlew runServer      # Launch dedicated server
./gradlew runData        # Run data generators
```

## Phase Plan

- [x] Phase 0: Project skeleton — mod loads in-game
- [ ] Phase 1: Core blocks, items, tool tiers, creative tabs
- [ ] Phase 2: Wood types, nature blocks, crops
- [ ] Phase 3: Passive entities (lions, zebras, giraffes, etc.)
- [ ] Phase 4: Hostile entities, combat items, armor, enchantments
- [ ] Phase 5: Block entities, GUIs, custom recipes
- [ ] Phase 6: NPCs, quest system, dialogue
- [ ] Phase 7: Dimensions, world generation, biomes, portals
- [ ] Phase 8: Sounds, music, advancements, polish

## Package Structure

```
io.github.ron1196.thelionking/
├── TheLionKingMod.java
├── registry/          — DeferredRegister classes
├── block/             — Block subclasses
├── block/entity/      — BlockEntity classes
├── item/              — Item subclasses
├── item/tier/         — Tool tiers, armor materials
├── entity/animal/     — Passive mobs
├── entity/hostile/    — Hostile mobs
├── entity/npc/        — Named NPCs
├── entity/projectile/ — Darts, spears, bombs
├── entity/ai/         — Custom AI goals
├── world/dimension/   — ChunkGenerators, Teleporter
├── world/feature/     — Custom worldgen features
├── quest/             — Quest system
├── data/              — SavedData, custom recipes
├── menu/              — Container menus
├── network/           — Packet handling
├── event/             — Event handlers
└── client/            — Client-only code
    ├── gui/           — Screens
    ├── model/         — Entity models
    ├── renderer/      — Entity & block entity renderers
    ├── particle/      — Custom particles
    └── sound/         — Music handler
```

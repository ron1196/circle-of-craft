# Migration Audit: Old 1.6.4 Mod → New 1.20.1 NeoForge Port

Last updated: 2026-03-09

This document tracks everything that has been migrated from the original Lion King mod
and everything that still needs work.

## Old Code Reference

- Original code: `old/code/`, Original assets: `old/assets/`
- Main class (all registrations): `old/code/common/mod_LionKing.java`
- Entity AI template: `old/code/common/LKEntityLionBase.java`
- Block entity template: `old/code/common/LKTileEntityGrindingBowl.java`
- Biome template: `old/code/common/LKPrideLandsBiome.java`
- Quest system: `old/code/common/LKQuestBase.java`

## Metadata Splitting Convention

Old metadata blocks became separate block IDs:
- `pridestone` meta 0,1 → `pridestone`, `corrupt_pridestone`
- `planks` meta 0-5 → `acacia_planks`, `rainforest_planks`, `mango_planks`, `passion_planks`, `banana_planks`, `deadwood_planks`

## Completed Phases (0-8)

- Phase 0: Project skeleton — mod loads in-game
- Phase 1: Core blocks, items, tool tiers, creative tabs
- Phase 2: Wood types, nature blocks, crops
- Phase 3: Passive entities (lions, zebras, giraffes, etc.)
- Phase 4: Hostile entities, combat items, armor, enchantments
- Phase 5: Block entities, GUIs, custom recipes
- Phase 6: NPCs, quest system, dialogue
- Phase 7: Dimensions, world generation, biomes, portals
- Phase 8: Sounds, music, advancements, polish

---

## Table of Contents

1. [Critical Gaps (Broken/Non-functional)](#1-critical-gaps)
2. [Missing Entities](#2-missing-entities)
3. [Missing AI Goals](#3-missing-ai-goals)
4. [Missing Blocks](#4-missing-blocks)
5. [Missing Items](#5-missing-items)
6. [Missing GUIs](#6-missing-guis)
7. [Networking](#7-networking)
8. [Event Handlers](#8-event-handlers)
9. [World Generation](#9-world-generation)
10. [Textures & Assets](#10-textures--assets)
11. [What's Working Well](#11-whats-working-well)

---

## 1. Critical Gaps

These are systems that exist but are broken or non-functional:

- [ ] **Networking — ZERO packets implemented**
  - Quest state won't sync in multiplayer
  - Old packets: `lk.questDoStage`, `lk.questDelay`, `lk.questCheck`, `lk.questStage`
  - Login sync packet (`lk.login`, 333-byte full world state) missing
  - Simba sit toggle, armor damage, quest check packets missing
  - Impact: Multiplayer is broken for quests and world state

- [ ] **Grinding Bowl — No recipe processing logic**
  - Block entity exists, GUI exists, but no recipes are processed
  - Old `LKGrindingRecipes.java` had 29 hardcoded recipes
  - Need: Custom recipe type OR hardcoded logic in block entity
  - Recipes include: hyena bones → shards, mangoes → dust, termites → dust, flowers → dyes

- [ ] **Server-side Event Handlers — Missing**
  - No `AttackEntityEvent` handler (Scar rug interaction)
  - No `LivingHurtEvent` handler (fire damage immunity with boots)
  - No `LivingDeathEvent` handler (special death drops)
  - No `UseHoeEvent` handler (Tilled Sand creation)
  - No `BonemealEvent` handler (prevent bonemeal in Pride Lands)
  - No server tick handler (quest updates, periodic mechanics)

- [ ] **Outlands Dimension — Water instead of lava**
  - Uses `minecraft:overworld` noise settings
  - Should generate lava below Y=63, not water
  - Need: Custom noise settings or chunk generator override

---

## 2. Missing Entities

| Entity | Old Class | Description | Priority |
|--------|-----------|-------------|----------|
| Termite Queen | `LKEntityTermiteQueen` | Boss mob with dynamic scaling, spawns projectiles | High |
| Thrown Termite | `LKEntityThrownTermite` | Projectile spawned by Termite Queen | High |
| Zazu Egg | `LKEntityZazuEgg` | Throwable egg projectile | Medium |
| Coin | `LKEntityCoin` | Teleportation entity (2 types: Paradise Peak, Mound) | Medium |
| Lightning | `LKEntityLightning` | Custom weather/magic effect with fire creation | Low |
| Outsand | `LKEntityOutsand` | Falling sand block entity for physics | Low |
| Scar Rug | `LKEntityScarRug` | Interactive corpse entity (post-boss defeat) | Low |
| Skeletal Hyena Head | `LKEntitySkeletalHyenaHead` | Decorative mob variant | Low |

---

## 3. Missing AI Goals

16 custom AI goal classes not ported. Entities use generic vanilla AI instead.

| AI Goal | Old Class | Used By | Priority |
|---------|-----------|---------|----------|
| Lion Attack | `LKEntityAILionAttack` | Lions | High |
| Simba Attack | `LKEntityAISimbaAttack` | Simba | High |
| Simba Follow | `LKEntityAISimbaFollow` | Simba | High |
| Simba Wander | `LKEntityAISimbaWander` | Simba | Medium |
| Simba Attack Player Attacker | `LKEntityAISimbaAttackPlayerAttacker` | Simba | Medium |
| Simba Attack Player Target | `LKEntityAISimbaAttackPlayerTarget` | Simba | Medium |
| Pumbaa Follow Timon | `LKEntityAIPumbaaFollowTimon` | Pumbaa | Medium |
| Termite Queen Attack | `LKEntityAITermiteQueenAttack` | Termite Queen | High |
| Bug Find Trap | `LKEntityAIBugFindTrap` | Bug | Medium |
| Angerable Panic | `LKEntityAIAngerablePanic` | Various | Medium |
| Angerable Mate | `LKEntityAIAngerableMate` | Various | Low |
| Angerable Attackable Target | `LKEntityAIAngerableAttackableTarget` | Various | Low |
| Zazu Mate | `LKEntityAIZazuMate` | Zazu | Low |
| Ambient Panic | `LKEntityAIAmbientPanic` | Ambient mobs | Low |
| Ambient Wander | `LKEntityAIAmbientWander` | Ambient mobs | Low |
| Ambient Avoid | `LKEntityAIAmbientAvoid` | Ambient mobs | Low |

---

## 4. Missing Blocks

| Block | Old Class | Description | Priority |
|-------|-----------|-------------|----------|
| Banana Cake | `LKBlockBananaCake` | Placeable cake, eaten in slices | Medium |
| Mounted Shooter | `LKBlockMountedShooter` | Turret that shoots darts | Medium |
| Outlands Altar | `LKBlockOutlandsAltar` | Quest altar block | Medium |
| Star Altar | `LKBlockStarAltar` | Quest altar block | Medium |
| Tilled Sand | `LKBlockTilledSand` | Farmland for Pride Lands crops | Medium |
| Vase | `LKBlockVase` | Decorative block | Low |
| Custom Bed | `LKBlockBed` | Pride Lands themed bed | Low |
| Custom Lever | `LKBlockLever` | Pride Lands themed lever | Low |

---

## 5. Missing Items

### Tools & Weapons (~10 items)

| Item | Old Class | Description | Priority |
|------|-----------|-------------|----------|
| Fire Sword | `LKItemSwordFire` | Sword with fire aspect | Medium |
| Fire Pickaxe | `LKItemPickaxeFire` | Pickaxe with fire ability | Medium |
| Fire Axe | `LKItemAxeFire` | Axe with fire ability | Medium |
| Fire Shovel | `LKItemShovelFire` | Shovel with fire ability | Medium |
| Kivulite Sword | — | Kivulite tier sword | Medium |
| Kivulite Pickaxe | — | Kivulite tier pickaxe | Medium |
| Kivulite Axe | — | Kivulite tier axe | Medium |
| Kivulite Shovel | — | Kivulite tier shovel | Medium |
| Kivulite Hoe | — | Kivulite tier hoe | Medium |
| Corrupt Hoe | — | Corrupt pridestone hoe | Low |
| Tunnah Diggah | `LKItemTunnahDiggah` | Special enchanted shovel | Low |

### Quest & Special Items (~8 items)

| Item | Old Class | Description | Priority |
|------|-----------|-------------|----------|
| Amulet | `LKItemAmulet` | Animalspeak amulet | Medium |
| Simba Charm | — | Quest item, active/inactive states | Medium |
| Zazu Egg | — | Used in banana cake recipe, breeding | Medium |
| Giraffe Saddle | — | Mount saddle for giraffes | Medium |
| Giraffe Tie | — | Giraffe control item | Low |
| Dart Quiver | `LKItemDartQuiver` | Stores darts (6-slot container) | Medium |
| Rug Dye | `LKItemRugDye` | Colors fur rugs | Low |
| Passion Fruit | — | Drops from passion leaves | Medium |

### Jar Items (5 items)

| Item | Description | Priority |
|------|-------------|----------|
| Empty Jar | Base jar item | Medium |
| Jar of Milk | Used in bug stew recipe (currently uses milk_bucket) | Medium |
| Jar of Water | Crafting ingredient | Medium |
| Jar of Lava | Crafting ingredient | Low |
| Jar of Mango Juice | Food/drink item | Low |

### Other Missing Items

| Item | Description | Priority |
|------|-------------|----------|
| Hyena Meal | Food item | Low |
| Scar Rug / Zira Rug | Quest reward items | Low |
| Mounted Shooter (item) | Places mounted shooter block | Medium |
| Block Placer | `LKItemBlockPlacer` | Utility item | Low |
| Info Item | `LKItemInfo` | Documentation/info item | Low |

---

## 6. Missing GUIs

| GUI | Old Container | Old Screen | Description | Priority |
|-----|---------------|------------|-------------|----------|
| Quiver | `LKContainerQuiver` | — | 6-slot dart storage | Medium |
| Timon Merchant | `LKContainerTimon` | — | NPC trading (5 merchant slots) | Medium |
| Simba Inventory | `LKContainerSimba` | — | 9-slot companion inventory | Medium |
| Item Info | `LKContainerItemInfo` | — | Item documentation display | Low |

---

## 7. Networking

**Status: NOT IMPLEMENTED**

### Required Packets

| Packet | Direction | Description | Priority |
|--------|-----------|-------------|----------|
| Quest Stage Sync | S→C | Sync current quest stage to all clients | High |
| Quest Delay | S→C | Set quest delay flag | High |
| Quest Check | C→S / S→C | Mark quest as checked | High |
| Login Sync | S→C | Full world state on player join | High |
| Simba Sit | C→S | Toggle Simba sit/stand | Medium |
| Damage Item | C→S | Armor damage from abilities | Medium |
| World State | S→C | Mound location, Scar defeated, etc. | Medium |
| Simba Ownership | S→C | Who owns Simba | Medium |

---

## 8. Event Handlers

**Status: Only mod bus events implemented (attribute registration, client setup)**

### Missing FORGE Bus Events

| Event | Old Handler | Purpose | Priority |
|-------|-------------|---------|----------|
| `LivingHurtEvent` | `onEntityLivingHurt` | Fire damage immunity with boots | High |
| `LivingDeathEvent` | `onEntityLivingDeath` | Special death drops | High |
| `AttackEntityEvent` | `onEntityAttack` | Scar rug drop logic | Medium |
| `PlayerInteractEvent` | `onEntityInteract` | NPC dialogue triggers | High |
| `BlockEvent.BreakEvent` | — | Special block drops (leaves → fruit) | Medium |
| `UseHoeEvent` | `onUseHoe` | Tilled Sand creation | Low |
| `BonemealEvent` | `onUseBonemeal` | Prevent bonemeal in Pride Lands | Low |
| Server Tick | `LKTickHandlerServer` | Quest timers, periodic spawning | High |
| Client Tick | `LKTickHandlerClient` | UI updates, animations | Low |

---

## 9. World Generation

### Tree Features

| Tree | Old Generator | Java Feature | JSON Config | Status |
|------|--------------|--------------|-------------|--------|
| Pride Acacia | `LKWorldGenTrees` | No | Yes | JSON only, may not generate |
| Rainforest | `LKWorldGenRainforestTrees` | No | Yes | JSON only, may not generate |
| Huge Rainforest | `LKWorldGenHugeRainforestTrees` | No | No | Not ported |
| Mango | `LKWorldGenMangoTrees` | No | Yes | JSON only, may not generate |
| Passion | `LKWorldGenPassionTrees` | No | Yes | JSON only, may not generate |
| Banana | `LKWorldGenBananaTrees` | Yes | Yes | Fully ported |
| Dead | `LKWorldGenDeadTrees` | Yes | Yes | Fully ported |

### Landmark Structures (0/5 ported)

| Structure | Old Generator | Description | Priority |
|-----------|--------------|-------------|----------|
| Rafiki's Tree | `LKWorldGenRafiki` | Spawns at 0,0 in Pride Lands | High |
| Zira's Mound | `LKWorldGenZiraMound` | Lava crater dungeon in Outlands | High |
| Timon & Pumbaa Lodge | `LKWorldGenTimonAndPumbaa` | Specific coordinates | Medium |
| Ticket Booth | `LKWorldGenTicketBooth` | Specific coordinates | Medium |
| Treasure Mound | `LKWorldGenTreasureMound` | Random Outlands locations | Low |

### Crop/Plant Features (0/5 ported)

| Feature | Old Generator | Biome | Priority |
|---------|--------------|-------|----------|
| Maize | `LKWorldGenMaize` | Savannah, Grassland | Medium |
| Kiwano | `LKWorldGenKiwano` | Arid Savannah | Medium |
| Yams | `LKWorldGenYams` | Rainforest | Medium |
| Lily Pads | `LKWorldGenLily` | Rainforest (water) | Low |
| Tall Flowers | `LKWorldGenTallFlowers` | Various | Low |

### Ore Generation

- [ ] Biome feature arrays (steps 2-7) are **completely empty**
- [ ] No pride coal ore, silver ore, or peacock ore generation configured
- [ ] Need placed features for each ore type per biome

### Other Missing World Gen

- [ ] Dungeons (`LKWorldGenDungeons` — 10 per chunk in old code)
- [ ] Outlands lava lakes (`LKWorldGenOutlandsLakes`)
- [ ] Outsand generation
- [ ] Zazu spawner areas

---

## 10. Textures & Assets

### Block Textures

- **Old:** 156 textures in `old/assets/textures/blocks/`
- **New:** 99 textures in `assets/thelionking/textures/block/`
- **~151 old textures not migrated** (naming changed from camelCase to snake_case)
- Most new blocks use magenta placeholder textures
- Key missing: wood variants, leaf variants, portal animations, rug colors, drum sides, crop stages

### Item Textures

- **Old:** 178 textures in `old/assets/textures/items/`
- **New:** 105 textures in `assets/thelionking/textures/item/`
- **~162 old textures not migrated**
- Key missing: all tool/armor textures, dart variants, food items, jar items, dyes

### Entity Textures

- **Old:** 41 textures in `old/assets/mob/`
- **New:** 46 textures in `assets/thelionking/textures/entity/`
- **Mostly complete** — new has additional projectile textures

### GUI Textures

- **Old:** 12 textures in `old/assets/gui/`
- **New:** 5 textures in `assets/thelionking/textures/gui/`
- **Missing:** `quiver.png`, `simba.png`, `timon.png`, `flatulence.png`, `icons.png`

### Data Files

| Type | Count | Status |
|------|-------|--------|
| Blockstates | 98 | Complete for all registered blocks |
| Block Models | 168 | 11 blocks missing models (crops, portals, walls) |
| Item Models | 221 | Complete |
| Loot Tables | 107 | 8 blocks missing (crops, portals) |
| Lang Entries | 369 | Complete for implemented content |
| Recipes | 33 | Smelting/cooking only, no crafting recipes for tools/armor |

### Missing Crafting Recipes

- [ ] All tool crafting recipes (pridestone, silver, peacock, corrupt)
- [ ] All armor crafting recipes
- [ ] Dart crafting recipes
- [ ] Spear crafting recipes
- [ ] Portal frame crafting recipes
- [ ] Decorative block recipes (stairs, slabs, walls from stonecutter)
- [ ] Grinding bowl recipe
- [ ] Bongo drum recipe
- [ ] Bug trap recipe

---

## 11. What's Working Well

These systems are fully ported and functional:

- **Dimensions:** 3/3 (Pride Lands, Outlands, Upendi) with correct properties
- **Biomes:** 14/14 created with correct temperatures, mob spawning
- **Portals:** Full portal mechanics (frame validation, teleportation, activation items)
- **Enchantments:** 6/6 fully registered with correct levels and effects
- **Advancements:** 33 JSON advancements with dependency chains
- **Sound Events:** All organized in subdirectories, 24 events registered
- **Music:** 5 Lion King songs with streaming playback
- **Passive Entities:** 10 animals with models, renderers, spawn eggs
- **Hostile Entities:** 7 mobs with AI, drops, models
- **NPC Entities:** 7 NPCs with dialogue, quest integration
- **Projectile Entities:** 3 (Dart, Spear, Pumbaa Bomb)
- **Block Entities:** 7 types (Grinding Bowl, Bongo Drum, Bug Trap, Hyena Head, Outlands Pool, Spawner, Fur Rug)
- **GUIs:** 4 screens (Grinding Bowl, Bongo Drum, Bug Trap, Quest Book)
- **Quest System:** 2 quest lines with stage progression (single-player)
- **Creative Tabs:** 8 organized tabs
- **Tool Tiers:** 5 tiers (Pridestone, Corrupt, Silver, Peacock, Kivulite defined but tools missing)
- **Armor Materials:** 5 sets registered
- **Smelting Recipes:** 33 recipes for cooking/smelting

---

## Priority Roadmap

### Phase 9: Critical Fixes
1. Grinding Bowl recipe processing
2. Server-side event handlers
3. Outlands lava generation fix
4. Missing crafting recipes (tools, armor, blocks)

### Phase 10: Missing Content
1. Fire tools (4 items)
2. Kivulite tools (5 items)
3. Jar items (5 items)
4. Missing blocks (Banana Cake, Mounted Shooter, Altars, Tilled Sand)
5. Missing quest items (Amulet, Simba Charm, Zazu Egg)
6. Termite Queen boss entity

### Phase 11: World Generation
1. Tree feature Java implementations (Mango, Passion, Rainforest, Acacia)
2. Ore generation in biome features
3. Landmark structures (Rafiki's Tree, Zira's Mound)
4. Crop/plant world gen features

### Phase 12: Networking & Multiplayer
1. NeoForge packet system implementation
2. Quest state sync packets
3. Login sync packet
4. Simba/world state packets

### Phase 13: Polish & Assets
1. Migrate old textures (rename camelCase → snake_case)
2. Missing GUI textures
3. Missing container GUIs (Quiver, Timon, Simba)
4. Custom AI goals for entities
5. Remaining missing entities (Lightning, Outsand, ScarRug)

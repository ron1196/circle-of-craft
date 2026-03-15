# Migration Audit: Old 1.6.4 Mod → New 1.20.1 NeoForge Port

Last updated: 2026-03-12

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

## Completed Phases (0-13)

- Phase 0: Project skeleton — mod loads in-game
- Phase 1: Core blocks, items, tool tiers, creative tabs
- Phase 2: Wood types, nature blocks, crops
- Phase 3: Passive entities (lions, zebras, giraffes, etc.)
- Phase 4: Hostile entities, combat items, armor, enchantments
- Phase 5: Block entities, GUIs, custom recipes
- Phase 6: NPCs, quest system, dialogue
- Phase 7: Dimensions, world generation, biomes, portals
- Phase 8: Sounds, music, advancements, polish
- Phase 9: Events, networking, tick handlers
- Phase 10: Crafting recipes (~100), missing items (~20), ore gen, tree features, grinding bowl expansion
- Phase 11: 16 custom AI goals, Outlands lava fix, Termite Queen boss, projectile entities
- Phase 12: 6 blocks, 3 GUIs/menus, 5 landmark structures
- Phase 13: Custom advancement triggers, quest logic completion, workaround fixes

---

## Table of Contents

1. [Critical Gaps (Resolved)](#1-critical-gaps-resolved)
2. [Entities](#2-entities)
3. [AI Goals](#3-ai-goals)
4. [Blocks](#4-blocks)
5. [Items](#5-items)
6. [GUIs & Menus](#6-guis--menus)
7. [Networking](#7-networking)
8. [Event Handlers](#8-event-handlers)
9. [World Generation](#9-world-generation)
10. [Textures & Assets](#10-textures--assets)
11. [What's Working Well](#11-whats-working-well)

---

## 1. Critical Gaps (Resolved)

All previously critical gaps have been addressed:

- [x] **Networking — 3 packets implemented** (Phase 9)
  - `SimbaSitPacket` (C→S), `QuestSyncPacket` (S→C), `QuestCheckPacket` (C→S)
  - SimpleChannel on port "main", protocol version "1"
  - Remaining: Login sync, world state, damage item, simba ownership packets not yet implemented

- [x] **Grinding Bowl — 29 recipes implemented** (Phase 10)
  - Full recipe processing in `GrindingBowlBlockEntity`
  - 200-tick grind time, input/output slot logic

- [x] **Server-side Event Handlers — Implemented** (Phase 9)
  - `LKForgeEvents.java` handles: LivingHurtEvent, LivingDeathEvent, PlayerInteractEvent, player tick, level tick
  - Enchantment effects, special drops, NPC dialogue, quest updates

- [x] **Outlands Dimension — Lava generation fixed** (Phase 11)

---

## 2. Entities

**Status: 36 entities registered** — all entities ported (Lightning, Outsand resolved).

### Implemented

| Category | Entities |
|----------|----------|
| Passive (10) | Lion, Lioness, Zebra, Giraffe, Rhino, Gemsbok, DikDik, Flamingo, Zazu, Bug |
| Hostile (8) | Hyena, SkeletalHyena, Outlander, Outlandess, Vulture, Crocodile, Termite, TermiteQueen |
| NPCs (7+) | Rafiki, Simba, Timon, Pumbaa, Scar, Zira, TicketLion |
| Projectiles (6) | Dart, Spear, PumbaaBomb, ThrownTermite, Coin, ZazuEgg |
| Interactive (1) | ScarRug (Scar/Zira types) |
| Other Hostile (1) | SkeletalHyenaHead |
| Magic (1) | LKLightningBolt (extends vanilla LightningBolt) |

### Previously Not Ported — Now Done

| Entity | Old Class | Solution | Status |
|--------|-----------|----------|--------|
| Lightning | `LKEntityLightning` | `LKLightningBoltEntity` extends vanilla `LightningBolt` — free rendering, custom power/fire/damage | Done |
| Outsand | `LKEntityOutsand` | Obsolete — `OutsandBlock` extends `FallingBlock` for vanilla falling physics | Done (no entity needed) |
| Scar Rug | `LKEntityScarRug` | `ScarRugEntity` — interactive corpse entity | Done |
| Skeletal Hyena Head | `LKEntitySkeletalHyenaHead` | `SkeletalHyenaHeadEntity` — hopping undead mob | Done |

---

## 3. AI Goals

**Status: 19 custom AI goals implemented** (Phase 11) — exceeds the original 16.

| AI Goal | Class | Used By | Status |
|---------|-------|---------|--------|
| Lion Attack | `LionAttackGoal` | Lions | Done |
| Simba Attack | `SimbaAttackGoal` | Simba | Done |
| Simba Follow | `SimbaFollowOwnerGoal` | Simba | Done |
| Simba Wander | `SimbaWanderGoal` | Simba | Done |
| Simba Attack Player Attacker | `SimbaAttackPlayerAttackerGoal` | Simba | Done |
| Simba Attack Player Target | `SimbaAttackPlayerTargetGoal` | Simba | Done |
| Pumbaa Follow Timon | `PumbaaFollowTimonGoal` | Pumbaa | Done |
| Pumbaa Swell | `SwellGoal` | Pumbaa | Done |
| Termite Queen Attack | `TermiteQueenAttackGoal` | Termite Queen | Done |
| Bug Find Trap | `BugFindTrapGoal` | Bug | Done |
| Angerable Panic | `AngerablePanicGoal` | Various | Done |
| Angerable Mate | `AngerableMateGoal` | Various | Done |
| Angerable Attack | `AngerableAttackGoal` | Various | Done |
| Zazu Mate | `ZazuMateGoal` | Zazu | Done |
| Ambient Panic | `AmbientPanicGoal` | Ambient mobs | Done |
| Ambient Wander | `AmbientWanderGoal` | Ambient mobs | Done |
| Ambient Avoid | `AmbientAvoidGoal` | Ambient mobs | Done |
| Head Hop | `HeadHopGoal` | Skeletal Hyena Head | Done |

---

## 4. Blocks

**Status: 106 blocks registered** — all planned blocks implemented.

### Previously Missing Blocks — Now Done

| Block | Status |
|-------|--------|
| Banana Cake | Done (Phase 12) |
| Mounted Shooter | Done (Phase 12) |
| Outlands Altar | Done (Phase 12) |
| Star Altar | Done (Phase 12) |
| Tilled Sand | Done (Phase 12) |
| Vase | Done (Phase 12) |

| Pride Bed | Done |
| Pride Lever | Done |

---

## 5. Items

**Status: 147 items registered** — all major items implemented.

### Previously Missing — Now Done

| Category | Items | Status |
|----------|-------|--------|
| Kivulite Tools (5) | Sword, Pickaxe, Axe, Shovel, Hoe | Done (Phase 10) |
| Corrupt Tools | Hoe | Done (Phase 10) |
| Jar Items (3) | Empty Jar, Jar of Water, Jar of Milk | Done (Phase 10) |
| Quest Items | Amulet, Simba Charm (functional class), Zazu Egg | Done (Phase 10) |
| Rafiki Stick | Quest weapon — grow crops, spread vegetation, thunder enchantment | Done |
| Rafiki Dust | Star Altar item — summons baby Simba with lightning | Done |
| Rhythm Staff (renamed) | Was "Staff" — bongo drum activation, quest item | Done (renamed from `staff` → `rhythm_staff`) |
| Giraffe Saddle | Mount saddle | Done (Phase 10) |
| Dart Quiver | 6-slot dart storage | Done (Phase 10) |
| Passion Fruit | Drops from passion leaves | Done (Phase 10) |

### Not Ported

| Item | Description | Priority |
|------|-------------|----------|
| Fire Sword | Sword with fire aspect | Medium |
| Fire Pickaxe | Pickaxe with fire ability | Medium |
| Fire Axe | Axe with fire ability | Medium |
| Fire Shovel | Shovel with fire ability | Medium |
| Jar of Lava | Crafting ingredient | Low |
| Jar of Mango Juice | Food/drink item | Low |
| Giraffe Tie | Giraffe control item | Low |
| Rug Dye | Colors fur rugs | Low |
| Hyena Meal | Food item | Low |
| Scar Rug / Zira Rug | Quest reward items | Done |
| Block Placer | Utility item | Low |
| Info Item | Documentation/info item | Low |
| Tunnah Diggah | Special enchanted shovel | Low |

---

## 6. GUIs & Menus

**Status: 7 screens, 6 menus** — all major GUIs implemented.

| GUI | Menu | Screen | Status |
|-----|------|--------|--------|
| Grinding Bowl | `GrindingBowlMenu` | `GrindingBowlScreen` | Done |
| Bongo Drum | `BongoDrumMenu` | `BongoDrumScreen` | Done |
| Bug Trap | `BugTrapMenu` | `BugTrapScreen` | Done |
| Quest Book | — | `QuestBookScreen` | Done |
| Quiver | `QuiverMenu` | `QuiverScreen` | Done (Phase 12) |
| Timon Merchant | `TimonMerchantMenu` | `TimonMerchantScreen` | Done (Phase 12) |
| Simba Inventory | `SimbaInventoryMenu` | `SimbaInventoryScreen` | Done (Phase 12) |

### Not Ported

| GUI | Description | Priority |
|-----|-------------|----------|
| Item Info | Item documentation display | Low |

---

## 7. Networking

**Status: 3 packets implemented** (Phase 9)

| Packet | Direction | Status |
|--------|-----------|--------|
| Quest Sync | S→C | Done (`QuestSyncPacket`) |
| Quest Check | C→S | Done (`QuestCheckPacket`) |
| Simba Sit | C→S | Done (`SimbaSitPacket`) |

### Not Implemented

| Packet | Direction | Description | Priority |
|--------|-----------|-------------|----------|
| Login Sync | S→C | Full world state on player join | Medium |
| Damage Item | C→S | Armor damage from abilities | Low |
| World State | S→C | Mound location, Scar defeated, etc. | Low |
| Simba Ownership | S→C | Who owns Simba | Low |

---

## 8. Event Handlers

**Status: Fully implemented** (Phase 9)

`LKForgeEvents.java` handles:
- [x] `LivingHurtEvent` — Peacock boots fall damage negation, Scourge of Hyenas enchantment
- [x] `LivingDeathEvent` — Hyena special drops (hyena head with looting)
- [x] `PlayerInteractEvent.EntityInteract` — NPC dialogue (Rafiki, Timon, Ticket Lion)
- [x] `PlayerEvent.PlayerLoggedInEvent` / tick events — Quest updates, data saving
- [x] `RegisterCommandsEvent` — `/lk` commands (10 subcommands)
- [x] Zira spawn event — when quest stage 22, spawns Zira with visual lightning on Outlands surface

### Not Implemented

| Event | Purpose | Priority |
|-------|---------|----------|
| `AttackEntityEvent` | Scar rug drop logic | Done |
| `UseHoeEvent` | Tilled Sand creation | Low |
| `BonemealEvent` | Prevent bonemeal in Pride Lands | Low |

---

## 9. World Generation

### Tree Features

| Tree | Java Feature | JSON Config | Status |
|------|--------------|-------------|--------|
| Banana | `BananaTreeFeature` | Yes | Done |
| Dead | `DeadTreeFeature` | Yes | Done |
| Mango | `MangoTreeFeature` | Yes | Done (Phase 10) |
| Passion | `PassionTreeFeature` | Yes | Done (Phase 10) |
| Rainforest | `RainforestTreeFeature` | Yes | Done (Phase 10) |
| Pride Acacia | — | Yes | JSON only, may not generate |
| Huge Rainforest | — | No | Not ported |

### Landmark Structures (5/5 ported) — Phase 12

| Structure | Feature Class | Status |
|-----------|--------------|--------|
| Rafiki's Tree | `RafikiTreeFeature` | Done |
| Zira's Mound | `ZiraMoundFeature` | Done |
| Timon & Pumbaa Lodge | `TimonPumbaaLodgeFeature` | Done |
| Ticket Booth | `TicketBoothFeature` | Done |
| Treasure Mound | `TreasureMoundFeature` | Done |

Also: `TermiteMoundFeature`, `FeatureHelper` utility class.

### Ore Generation

- [x] Configured & placed features for pride coal ore, silver ore, peacock ore (Phase 10)

### Crop/Plant Features

Still using JSON-only configs. No dedicated Java feature classes.

| Feature | Old Generator | Status |
|---------|--------------|--------|
| Maize | `LKWorldGenMaize` | JSON config only |
| Kiwano | `LKWorldGenKiwano` | JSON config only |
| Yams | `LKWorldGenYams` | JSON config only |
| Lily Pads | `LKWorldGenLily` | Not ported |
| Tall Flowers | `LKWorldGenTallFlowers` | Not ported |

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
- **~150 old textures not migrated** (naming changed from camelCase to snake_case)
- Most new blocks use magenta placeholder textures
- Key missing: wood variants, leaf variants, portal animations, rug colors, drum sides, crop stages

### Item Textures

- **Old:** 178 textures in `old/assets/textures/items/`
- **New:** 105 textures in `assets/thelionking/textures/item/`
- **~160 old textures not migrated**
- Key missing: all tool/armor textures, dart variants, food items, jar items, dyes

### Entity Textures

- **Old:** 41 textures in `old/assets/mob/`
- **New:** 47 textures in `assets/thelionking/textures/entity/`
- **Mostly complete** — new has additional projectile textures

### GUI Textures

- **Old:** 12 textures in `old/assets/gui/`
- **New:** 5 textures in `assets/thelionking/textures/gui/`
- **Missing:** `quiver.png`, `simba.png`, `timon.png`, `flatulence.png`, `icons.png`

### Data Files

| Type | Count | Status |
|------|-------|--------|
| Blockstates | 100 | Complete for all registered blocks |
| Block Models | 172 | 11 blocks missing models (crops, portals, walls) |
| Item Models | 223 | Complete |
| Loot Tables | 109 | 8 blocks missing (crops, portals) |
| Lang Entries | 371 | Complete for implemented content |
| Recipes | 129 | Crafting, smelting, blasting, smoking, campfire |

### Advancement Icons

4 advancement icons use placeholder substitutions for items not yet registered:
- `outlandish_dart`, `tunnah_diggah`, `ticket_lion_helmet`, `peacock_wings`

(Previously 5 — `lion_dust` resolved: now `rafiki_dust` with real texture)

---

## 11. What's Working Well

These systems are fully ported and functional:

- **Dimensions:** 3/3 (Pride Lands, Outlands, Upendi) with correct properties
- **Biomes:** 14/14 created with correct temperatures, mob spawning
- **Portals:** Full portal mechanics (frame validation, teleportation, activation items)
- **Enchantments:** 6/6 fully registered with correct levels and effects; custom `RAFIKI_STICK_CATEGORY` ensures Rafiki enchantments appear on enchanting table for Rafiki Stick only
- **Advancements:** 33 JSON advancements with dependency chains
- **Advancement Triggers:** 11 custom triggers (shoot dart, quest complete, enter dimensions, etc.)
- **Sound Events:** All organized in subdirectories, 24 events registered
- **Music:** 5 Lion King songs with streaming playback
- **Passive Entities:** 10 animals with models, renderers, spawn eggs
- **Hostile Entities:** 9 mobs with AI, drops, models (including Termite Queen boss, Skeletal Hyena Head)
- **NPC Entities:** 7 NPCs with dialogue, quest integration (Zira: boss fight lightning spawns, death explosion)
- **Projectile Entities:** 6 (Dart, Spear, Pumbaa Bomb, Thrown Termite, Coin, Zazu Egg)
- **Interactive Entities:** Scar Rug / Zira Rug (talk on interact, quest reward)
- **AI Goals:** 19 custom goals wired into entities
- **Block Entities:** 8 types (Grinding Bowl, Bongo Drum, Bug Trap, Hyena Head, Outlands Pool, Spawner, Fur Rug, Pride Bed)
- **GUIs:** 7 screens (Grinding Bowl, Bongo Drum, Bug Trap, Quest Book, Quiver, Timon, Simba)
- **Quest System:** 2 quest lines with stage progression, networking sync
- **Networking:** 3 packets (quest sync, quest check, simba sit)
- **Event Handlers:** Forge bus events for combat, drops, NPC interaction, ticks
- **Creative Tabs:** 8 organized tabs
- **Tool Tiers:** 5 tiers with all tools registered
- **Armor Materials:** 5 sets with all pieces registered
- **Crafting Recipes:** 129 recipes (tools, armor, blocks, food, materials)
- **Grinding Bowl:** 29 grinding recipes with full processing logic
- **Landmark Structures:** 5 structures (Rafiki Tree, Zira Mound, Ticket Booth, Lodge, Treasure Mound)
- **Commands:** 10 `/lk` subcommands for teleportation and debugging

---

## Remaining Work Summary

### Medium Priority
- Fire tools (4 items — sword, pickaxe, axe, shovel)
- Pride Acacia tree Java feature (currently JSON only)
- Huge Rainforest tree feature (not ported)
- Remaining networking packets (login sync, world state)
- Crop/plant world gen Java features (maize, kiwano, yams)

### Low Priority
- ~12 items not ported (giraffe tie, rug dye, jars of lava/mango, etc.)
- 2 event handlers not ported (UseHoe, Bonemeal)
- 1 GUI not ported (Item Info)
- Missing world gen (dungeons, lava lakes, outsand, zazu spawners)

### Assets (Ongoing)
- ~150 block textures need migration from old camelCase to snake_case
- ~160 item textures need migration
- 5 GUI textures missing
- 4 advancement icon placeholders
- NPC placeholder models (Scar, Zira, Ticket Lion) need proper models

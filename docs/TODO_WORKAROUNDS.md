# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Models / Rendering

- [x] ~~**NPC entities use placeholder models**~~ — RESOLVED: Old mod also used lion model for Scar/Zira/Ticket Lion. Renamed `NpcPlaceholderModel` → `NpcLionModel` to reflect this is intentional.

## Missing Item Behaviour

- [x] ~~**`ground_rhino_horn` has no interaction logic**~~ — RESOLVED: `GroundRhinoHornItem` handled via `PlayerInteractEvent.EntityInteract` in `LionKingForgeEvents`. Right-click adult animal with nearby mate → sets both in love mode (1/3 fail chance with smoke). `CrossTypeBreedGoal` handles walk-toward + breeding, including cross-type pairs (lion ↔ lioness). Fires `USE_RHINO_HORN` trigger.

## Missing Items (not yet ported from old mod)

- [x] ~~**Fire Sword / Pickaxe / Axe / Shovel**~~ — RESOLVED: These are the Kivulite tools (`KivuliteSwordItem`, etc.) with `FireToolHelper` auto-smelt logic.
- [x] ~~**Jar of Lava**~~ — RESOLVED: Registered `jar_lava` item with container return, lang entry, item model.
- [x] ~~**Jar of Mango Juice**~~ — RESOLVED: Registered `mango_juice` as food item (6 nutrition, 0.5 sat) with container return.
- [x] ~~**Hyena Meal**~~ — RESOLVED: `HyenaMealItem` — bonemeal for LK dimensions, grows saplings/crops, spreads vegetation on grass.
- [x] ~~**Giraffe Tie**~~ — RESOLVED: 8 tie items registered, GiraffeEntity has saddled/tie synched data with interaction logic.
- [x] ~~**Fur Rug Coloring / Rug Dye**~~ — SKIP: Vanilla dye system handles coloring natively; no custom rug dye item needed.
- [x] ~~**Musical Notes**~~ — RESOLVED: 7 `MusicalNoteItem` variants with pitch values and enchantment glint.
- [x] ~~**Tunnah Diggah**~~ — RESOLVED: `TunnahDiggahItem` AoE pickaxe, works with existing BIGGAH_DIGGAH and PRECISION enchantments.
- [x] ~~**Block Placer**~~ — RESOLVED: Obsolete in modern MC — `BlockItem` handles this natively.
- [x] ~~**Info Item**~~ — SKIP: Replaced by modern item tooltips. Not needed as separate item/GUI.

## Missing Blocks

- [x] ~~**Zira Mound Gate block**~~ — RESOLVED: `ZiraMoundGateBlock` — indestructible, breakable with Rafiki Stick (chain break). ZiraMoundFeature updated to use it.

## Advancements

- [x] ~~**"Horn of Plenty" (`rhino_horn`) advancement uses wrong trigger**~~ — RESOLVED: Replaced `PlayerTrigger` with custom `UseGrindingBowlTrigger` (passes output `ItemStack`). `rhino_horn.json` now filters on `thelionking:ground_rhino_horn` via `item` condition.
- [x] ~~**Remaining advancement icon substitutions**~~ — RESOLVED: Registered `dart_outlandish` (plain Item + texture migrated) and `peacock_wings` (GEMSBOK chestplate + texture migrated). `ticket_lion_head` was already registered. Updated `termite_dart.json` and `peacock_wings.json` icons to real items. ~~`tunnah_diggah`~~ FIXED: now uses real item, ~~`lion_dust`~~ previously resolved. Advancement `rhino_horn` renamed to `ground_rhino_horn`.

## Missing World Generation

- [x] ~~**Huge Rainforest Tree**~~ — RESOLVED: Renamed to `mega_rainforest_tree` (matching vanilla naming). Uses `minecraft:tree` with `mega_jungle_trunk_placer` (h 25–50) + rainforest log/leaves + vine decorators. Placed feature wired into rainforest, rainforest_hills, and upendi biomes. No custom Java class needed.
- [x] ~~**Pride Acacia Tree**~~ — RESOLVED: Uses `minecraft:tree` with `forking_trunk_placer` + acacia foliage placer + pride_acacia log/leaves. Placed feature wired into arid_savannah, wooded_savannah, savannah, pride_river, pride_mountains biomes. No custom Java class needed.
- [x] ~~**Lily Pad Distribution**~~ — RESOLVED: `lily_pad` configured feature uses `random_patch` (10 tries, 7-block spread) with `simple_random_selector` picking between red/violet/white lily. Placed in rainforest, rainforest_hills, and upendi biomes.
- [x] ~~**Tall Flower Distribution**~~ — SKIP: `LKWorldGenTallFlowers` existed but `purpleFlowersPerChunk` and `redFlowersPerChunk` were both 0 everywhere — never generated in the old mod.
- [x] ~~**Dungeons**~~ — RESOLVED: `PrideDungeonFeature` — 7x7x4 pride brick rooms with LK spawner (2/3 hyena, 1/3 crocodile) and 1-2 loot chests. Placed feature with 8 attempts per chunk at y 0-56 in all Outlands biomes.
- [ ] **Nuka Ore & Kivulite Ore blocks** — Old mod used metadata variants: `prideCoal` meta 1 = Nuka Ore (`oreNuka.png`, drops nuka shards), `oreSilver` meta 1 = Kivulite Ore (`oreKivulite.png`, drops kivulite). Need separate blocks `nuka_ore` and `kivulite_ore`, textures migrated from `old/assets/textures/blocks/`, and placed features wired into Outlands biomes (replacing current silver/coal ore placements there)
- [ ] **Outlands Lava Lakes** — Old: `LKWorldGenOutlandsLakes` used removed API. Must be ported as a `PlacedFeature` using `LakeFeature` (1.18+ approach)
- [x] ~~**Zazu Spawner Areas**~~ — SKIP: No dedicated spawner gen in old mod. Zazus spawn via biome config.

## Missing HUD Overlays

Old `LKGuiIngame.java` — none ported. Implement via `RenderGuiOverlayEvent` in `LKClientEvents`.

- [x] ~~**Boss HP Bar**~~ — RESOLVED: Vanilla `ServerBossEvent` in `ScarEntity` (RED) and `ZiraEntity` (PURPLE). Bar appears when player tracks the boss's chunk. No custom rendering needed.
- [x] ~~**Portal Overlay**~~ — RESOLVED: `PortalOverlayPacket` renders portal overlay with nausea wobble effect via `spinningEffectIntensity`.
- [x] ~~**Flatulence Overlay**~~ — RESOLVED: `FlatulencePacket` (S→C) sent to all players within 15 blocks of a Pumbaa bomb explosion. `LKHudOverlays` renders `flatulence.png` full-screen with 60-tick fade via `RenderGuiEvent.Post`.

## Missing Event Handlers

- [ ] **UseHoeEvent** — Tilled Sand creation when hoeing sand blocks
- [x] ~~**BonemealEvent**~~ — SKIP: Both vanilla bonemeal and Hyena Meal work in all dimensions.
- [x] ~~**Pride Lands Entry Song**~~ — RESOLVED: `DimensionMusicHandler` plays Circle of Life on dimension change.
- [ ] **Respawn Dimension Redirect** — Old mod: dying in Outlands or Upendi respawns the player in Pride Lands (not Overworld). Needs a `PlayerEvent.PlayerRespawnEvent` handler to teleport the player to Pride Lands world spawn when they die in those dimensions without a bed set.
- [ ] **Verify `handleZiraSpawnEvent`** — The Zira spawn event (ziraStage 22) in `LionKingForgeEvents` spawns a visual `LightningBoltEntity` which now triggers `onEntityJoinLevel` → `convertSandToOutsand`. Check that this doesn't create an unwanted outsand patch at Zira's spawn point. May need to skip conversion for our custom `LightningBoltEntity` subclass.

## Missing Networking Packets

- [x] ~~**Login Sync Packet**~~ — RESOLVED: `LoginSyncPacket` syncs defeatedScar, ziraStage, pumbaaStage, outlandersHostile on player join.
- [x] ~~**World State Packet**~~ — RESOLVED: Covered by `LoginSyncPacket` + `PlayerDataSyncPacket`.
- [x] ~~**Damage Item Packet**~~ — SKIP: `ItemStack.hurtAndBreak()` is server-authoritative in 1.20.1; no C→S packet needed.
- [x] ~~**Simba Ownership Packet**~~ — SKIP: `TamableAnimal` base class + `SynchedEntityData` syncs ownership automatically; no custom packet needed.

## Missing GUI

- [x] ~~**Item Info GUI**~~ — SKIP: Replaced by modern item tooltips.

## Bed Texture

- [ ] **Pride Bed 64x64 texture composed programmatically** — The old mod had 6 separate 16x16 face textures. These were stitched into a single 64x64 entity texture matching vanilla's UV layout. May need artist review for pixel-perfect accuracy, especially side/end face alignment.
  - Source textures: `old/assets/textures/blocks/bed_*.png`
  - Generated texture: `textures/entity/pride_bed.png`

## Placeholder Textures

Items using generated placeholder textures (not from old mod):

- [ ] **`kivulite_hoe`** — Old mod had no kivulite hoe; using generated teal placeholder
- [ ] **`corrupt_hoe`** — Old mod had no corrupt hoe; using generated purple placeholder
- [ ] **`mounted_shooter` block textures** — Old mod only had item textures; block front/side/top are solid-color placeholders
- [ ] **`outlands_altar` block texture** — No old texture exists; reusing `corrupt_pridestone.png`
- [ ] **`tilled_sand` item texture** — Generated sandy placeholder; block textures from old mod are correct
- [ ] **`star_altar` item texture** — Generated placeholder; block textures (side/top) from old mod are correct
- [ ] **`outlands_altar` item texture** — Generated dark placeholder
- [ ] **~150 block textures** need migration from old camelCase to snake_case
- [ ] **~160 item textures** need migration
- [x] ~~**5 GUI textures**~~ — RESOLVED: `quiver.png`, `simba.png`, `timon.png`, `flatulence.png` all migrated from old assets. `icons.png` not needed — vanilla handles HUD icons.

## Deprecated BlockBehaviour.use() Override

- [ ] **8 blocks override the deprecated `BlockBehaviour.use()` method** — Mojang deprecated this in 1.20.1 in preparation for a refactor that landed in 1.21, where it was split into `useWithoutItem()` (empty-hand interaction) and `useItemOn()` (item-in-hand interaction). No non-deprecated replacement exists in 1.20.1, so each block currently suppresses the warning with `@SuppressWarnings("deprecation")`. When upgrading to 1.21+, migrate all 8 blocks:
  - `PortalFrameBlock` — item-triggered portal creation
  - `ZiraMoundGateBlock` — Rafiki Stick chain-break
  - `StarAltarBlock` — star altar activation
  - `OutlandsAltarBlock` — outlands altar activation
  - `BongoDrumBlock` — drum playing
  - `BananaCakeBlock` — eating
  - `BugTrapBlock` — bug collection
  - `GrindingBowlBlock` — ingredient insertion / output extraction
- [ ] **3 blocks override the deprecated `BlockBehaviour.onRemove()`** — same 1.21 migration. Migrate to the non-deprecated replacement when upgrading:
  - `BugTrapBlock`, `GrindingBowlBlock`, `BongoDrumBlock` — all drop block entity contents on removal
- [ ] **`LionKingFlowerBlock` uses the deprecated `FlowerBlock(MobEffect, int, Properties)` constructor** — In 1.20.1, `FlowerBlock` deprecated this in favour of `FlowerBlock(Holder<MobEffect>, int, Properties)`, but `MobEffect.builtInRegistryHolder()` is not available in NeoForge 47.1.x. Suppressed with `@SuppressWarnings("deprecation")`. When upgrading to 1.21+, switch to `Holder<MobEffect>` and remove the annotation.

## CharacterSpeech Split

- [ ] **`CharacterSpeech.java` is a monolithic dialogue class** — All NPC and animal dialogue lives in one big enum. Should be split:
  - **Rafiki quest dialogue** → move to `RafikiQuestline.java` or `RafikiEntity.java`
  - **Outlands quest dialogue** → move to `OutlandsQuestline.java` or `ZiraEntity.java`
  - **Boss rug dialogue** → move to `RugEntity.java` or a rug-specific class
  - **NPC dialogue** → move to respective NPC entity classes
  - **Animal dialogue** → move to `quest/animal/` or to each animal entity class
  - Keep a shared `SpeechUtil.giveSpeech(name, lines)` helper for the formatting pattern

## Crop Block Models

- [ ] **Kiwano, Maize, and Yam 3D models need rework** — Current block models don't look right. Need proper crop stage models matching the old mod's appearance.

## Crop Block Classes (Wrong Base Class)

- [ ] **Maize is `CropBlock` but should be sugar cane-like** — Old mod: multi-block tall, grows near water, not on farmland. Needs custom block class extending `BushBlock` or similar, with water-adjacent check and multi-block stacking.
- [ ] **Yam is `CropBlock` but should grow on grass** — Old mod places yams on grass blocks, but `CropBlock.canSurvive()` requires farmland. Needs `mayPlaceOn` override to accept grass.
- [ ] **Kiwano worldgen places `kiwano_block` (fruit) on sand** — Matches old mod. Stem (`kiwano_stem`) is the crop version for player farming.

## Grinding Bowl Recipes (Hardcoded)

- [ ] **Grinding bowl recipes are hardcoded in `GrindingBowlBlockEntity.getRecipes()`** — 29 recipes live in a static `Map<Item, Item>` inside the block entity. Should be refactored to a custom `RecipeType<GrindingBowlRecipe>` + `RecipeSerializer` with JSON recipes under `data/thelionking/recipes/grinding/`. This would decouple game content from machine logic and enable datapack compatibility.

## Networking (Not Fully Wired)

- [x] ~~**`QuestSyncPacket`**~~ — RESOLVED: Already sent from `QuestlineManager.syncToAllPlayers()` on quest advance.
- [x] ~~**`QuestCheckPacket`**~~ — RESOLVED: Already sent from `QuestBookScreen` on click.
- [ ] **`SimbaSitPacket`** — Sit toggle works via `mobInteract()`. Packet exists for future keybind (toggle sit from distance).

## GUIs (Not Fully Wired)

- [x] ~~**Quiver GUI**~~ — RESOLVED: `QuiverItem` with right-click `use()` opens `QuiverMenu`.
- [x] ~~**Timon Merchant GUI**~~ — RESOLVED: Sneak+interact on Timon opens `TimonMerchantMenu`.
- [x] ~~**Simba Inventory GUI**~~ — RESOLVED: Sneak+interact on owned Simba opens `SimbaInventoryMenu`.

## Advancement Triggers (Not Fully Wired)

- [ ] **`USE_GRINDING_BOWL` trigger is orphaned** — `UseGrindingBowlTrigger` is registered and fires in `GrindingBowlMenu.onTake`, but no advancement uses it. The old mod had no grinding achievement. Decision needed: remove the trigger + `UseGrindingBowlTrigger` class entirely, or add a new "first grind" advancement not present in the old mod.

- [x] ~~**All 9 triggers wired**~~ — RESOLVED:
  - `SHOOT_DART` in DartShooterItem, `USE_GRINDING_BOWL` in GrindingBowlBlockEntity (nearest player)
  - `RIDE_GIRAFFE` in GiraffeEntity (on saddle equip), `PLAY_BONGO_DRUM` in BongoDrumBlock
  - `ENTER_PRIDE_LANDS/OUTLANDS/UPENDI` in LKForgeEvents.onPlayerTick (first-time via LKPlayerData flags)
  - `BEHEAD_HYENA`, `KILL_SCAR`, `KILL_ZIRA` in LKForgeEvents.onLivingDeath

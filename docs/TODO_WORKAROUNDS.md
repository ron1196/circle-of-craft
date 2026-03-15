# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Models / Rendering

- [x] ~~**NPC entities use placeholder models**~~ — RESOLVED: Old mod also used lion model for Scar/Zira/Ticket Lion. Renamed `NpcPlaceholderModel` → `NpcLionModel` to reflect this is intentional.

## Missing Items (not yet ported from old mod)

- [x] ~~**Fire Sword / Pickaxe / Axe / Shovel**~~ — RESOLVED: These are the Kivulite tools (`KivuliteSwordItem`, etc.) with `FireToolHelper` auto-smelt logic.
- [x] ~~**Jar of Lava**~~ — RESOLVED: Registered `jar_lava` item with container return, lang entry, item model.
- [x] ~~**Jar of Mango Juice**~~ — RESOLVED: Registered `mango_juice` as food item (6 nutrition, 0.5 sat) with container return.
- [x] ~~**Hyena Meal**~~ — RESOLVED: `HyenaMealItem` — bonemeal for LK dimensions, grows saplings/crops, spreads vegetation on grass.
- [x] ~~**Giraffe Tie**~~ — RESOLVED: 8 tie items registered, GiraffeEntity has saddled/tie synched data with interaction logic.
- [ ] **Fur Rug Coloring** — Add rug coloring using vanilla dyes (replaces old mod's custom rug dye items)
- [x] ~~**Musical Notes**~~ — RESOLVED: 7 `MusicalNoteItem` variants with pitch values and enchantment glint.
- [x] ~~**Tunnah Diggah**~~ — RESOLVED: `TunnahDiggahItem` AoE pickaxe, works with existing BIGGAH_DIGGAH and PRECISION enchantments.
- [x] ~~**Block Placer**~~ — RESOLVED: Obsolete in modern MC — `BlockItem` handles this natively.
- [x] ~~**Info Item**~~ — SKIP: Replaced by modern item tooltips. Not needed as separate item/GUI.

## Missing Blocks

- [x] ~~**Zira Mound Gate block**~~ — RESOLVED: `ZiraMoundGateBlock` — indestructible, breakable with Rafiki Stick (chain break). ZiraMoundFeature updated to use it.

## Advancements

- [ ] **Remaining advancement icon substitutions** (items not yet registered):
  - `lion_dust` → `termite_dust`, `outlandish_dart` → `dart_black`
  - `tunnah_diggah` → `pridestone_shovel`, `ticket_lion_helmet` → `ticket_lion_head`
  - `peacock_wings` → `peacock_gem`

## Missing World Generation

- [ ] **Huge Rainforest Tree** — Old: `LKWorldGenHugeRainforest`, not ported (no Java feature or JSON config)
- [ ] **Pride Acacia Tree** — JSON config only, no Java feature class, may not generate
- [ ] **Lily Pad Distribution** — Old: `LKWorldGenLily`, not ported at all
- [ ] **Tall Flower Distribution** — Old: `LKWorldGenTallFlowers`, not ported at all
- [ ] **Dungeons** — Old: `LKWorldGenDungeons` (10 per chunk in Outlands), not ported
- [ ] **Outlands Lava Lakes** — Old: `LKWorldGenOutlandsLakes`, not ported
- [ ] **Zazu Spawner Areas** — Old had spawner-specific generation logic, not ported

## Missing Event Handlers

- [ ] **UseHoeEvent** — Tilled Sand creation when hoeing sand blocks
- [ ] **BonemealEvent** — Prevent bonemeal usage in Pride Lands dimension

## Missing Networking Packets

- [ ] **Login Sync Packet** (S→C) — Full world state sync on player join
- [ ] **World State Packet** (S→C) — Mound location, Scar defeated flag, etc.
- [ ] **Damage Item Packet** (C→S) — Armor damage from abilities
- [ ] **Simba Ownership Packet** (S→C) — Who owns Simba

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
- [ ] **5 GUI textures** missing: `quiver.png`, `simba.png`, `timon.png`, `flatulence.png`, `icons.png`

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

## Networking (Not Fully Wired)

Packets are registered and defined, but client-side send triggers are missing:

- [ ] **`SimbaSitPacket`** — Needs client-side right-click handler or keybind to call `LKNetworking.CHANNEL.sendToServer()`
- [ ] **`QuestSyncPacket`** — Needs server code to send on quest state change via `PacketDistributor.PLAYER`
- [ ] **`QuestCheckPacket`** — Needs client GUI button to send

## GUIs (Not Fully Wired)

Menu + Screen classes exist but opening triggers are incomplete:

- [ ] **Quiver GUI** — Needs right-click handler on `dart_quiver` item to call `player.openMenu()`
- [ ] **Timon Merchant GUI** — Needs `player.openMenu()` call in Timon NPC interaction (currently shows chat message only)
- [ ] **Simba Inventory GUI** — Needs right-click handler on Simba entity to open menu

## Advancement Triggers (Not Fully Wired)

Custom `PlayerTrigger` instances are registered but some are never fired from game events:

- [ ] **`SHOOT_DART`** — Needs `LKCriteriaTriggers.SHOOT_DART.trigger(player)` in dart shooter use code
- [ ] **`USE_GRINDING_BOWL`** — Needs trigger in `GrindingBowlBlockEntity` when grinding completes
- [ ] **`RIDE_GIRAFFE`** — Needs trigger when player mounts giraffe
- [ ] **`PLAY_BONGO_DRUM`** — Needs trigger in bongo drum interaction
- [ ] **`ENTER_PRIDE_LANDS` / `ENTER_OUTLANDS` / `ENTER_UPENDI`** — Need triggers in player tick when dimension changes
- [ ] **`BEHEAD_HYENA`** — Needs trigger in `LKForgeEvents.onLivingDeath` when hyena head drops
- [ ] **`KILL_SCAR` / `KILL_ZIRA`** — Need triggers in death event when Scar/Zira die

# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Missing World Generation

- [ ] **Nuka Ore & Kivulite Ore blocks** — Old mod used metadata variants: `prideCoal` meta 1 = Nuka Ore (`oreNuka.png`, drops nuka shards), `oreSilver` meta 1 = Kivulite Ore (`oreKivulite.png`, drops kivulite). Need separate blocks `nuka_ore` and `kivulite_ore`, textures migrated from `old/assets/textures/blocks/`, and placed features wired into Outlands biomes (replacing current silver/coal ore placements there)
- [x] **Outlands Lava Lakes** — Ported using vanilla `LakeFeature` with corrupt pridestone barrier. Underground (rarity 5) + surface (rarity 40) placed features in all 3 Outlands biomes.
- [ ] **Outlands Dungeon variant** — Pride Dungeons currently use the same loot table in both Pride Lands and Outlands. Outlands dungeons should have better/different loot (e.g. kivulite items, corrupt tools, rare gems) to reward exploring a more dangerous dimension. May also want Outlands-specific building materials (corrupt pride brick?) instead of regular pride brick.

## Missing Event Handlers

- [ ] **UseHoeEvent** — Tilled Sand creation when hoeing sand blocks
- [ ] **Respawn Dimension Redirect** — Old mod: dying in Outlands or Upendi respawns the player in Pride Lands (not Overworld). Needs a `PlayerEvent.PlayerRespawnEvent` handler to teleport the player to Pride Lands world spawn when they die in those dimensions without a bed set.
- [ ] **Verify `handleZiraSpawnEvent`** — The Zira spawn event (ziraStage 22) in `LionKingForgeEvents` spawns a visual `LightningBoltEntity` which now triggers `onEntityJoinLevel` → `convertSandToOutsand`. Check that this doesn't create an unwanted outsand patch at Zira's spawn point. May need to skip conversion for our custom `LightningBoltEntity` subclass.

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

- [x] **Maize is `CropBlock` but should be sugar cane-like** — Replaced with `MaizeCropBlock`: stacks up to 4 tall, requires adjacent water on grass/dirt, has_corn state for harvestable corn ears.
- [x] **Yam is `CropBlock` but should grow on grass** — Replaced with `YamCropBlock`: `mayPlaceOn` accepts dirt tag (grass, dirt, etc.).
- [ ] **Kiwano worldgen places `kiwano_block` (fruit) on sand** — Matches old mod. Stem (`kiwano_stem`) is the crop version for player farming.

## Grinding Bowl Recipes (Hardcoded)

- [ ] **Grinding bowl recipes are hardcoded in `GrindingBowlBlockEntity.getRecipes()`** — 29 recipes live in a static `Map<Item, Item>` inside the block entity. Should be refactored to a custom `RecipeType<GrindingBowlRecipe>` + `RecipeSerializer` with JSON recipes under `data/thelionking/recipes/grinding/`. This would decouple game content from machine logic and enable datapack compatibility.


## Advancement Triggers (Not Fully Wired)

- [ ] **`USE_GRINDING_BOWL` trigger is orphaned** — `UseGrindingBowlTrigger` is registered and fires in `GrindingBowlMenu.onTake`, but no advancement uses it. The old mod had no grinding achievement. Decision needed: remove the trigger + `UseGrindingBowlTrigger` class entirely, or add a new "first grind" advancement not present in the old mod.

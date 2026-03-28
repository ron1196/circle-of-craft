# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Missing World Generation

- [x] ~~**Outlands Dungeon variant**~~ — RESOLVED: Outlands dungeons now use a separate loot table with kivulite/corrupt tools, black darts, nuka shards, and zira coins.

## Missing Event Handlers

- [x] ~~**UseHoeEvent**~~ — RESOLVED: Right-clicking sand with a hoe creates tilled sand. Added in `LionKingForgeEvents.onRightClickBlock`.
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
- [ ] **`star_altar` item texture** — Generated placeholder (unused — item model inherits block model 3D render). Can delete the file.
- [ ] **`outlands_altar` item texture** — Generated dark placeholder
- [ ] **~150 block textures** need migration from old camelCase to snake_case
- [ ] **~160 item textures** need migration

## Deprecated BlockBehaviour.use() Override

- [ ] **7 blocks override the deprecated `BlockBehaviour.use()` method** — Mojang deprecated this in 1.20.1 in preparation for a refactor that landed in 1.21, where it was split into `useWithoutItem()` (empty-hand interaction) and `useItemOn()` (item-in-hand interaction). No non-deprecated replacement exists in 1.20.1, so each block currently suppresses the warning with `@SuppressWarnings("deprecation")`. When upgrading to 1.21+, migrate all 7 blocks:
  - `PortalFrameBlock` — item-triggered portal creation
  - `ZiraMoundGateBlock` — Rafiki Stick chain-break (also checks quest completion)
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

## Crop Blocks

- [ ] **Kiwano worldgen places `kiwano_block` (fruit) on sand** — Matches old mod. Stem (`kiwano_stem`) is the crop version for player farming.

## Grinding Bowl Recipes (Hardcoded)

- [ ] **Grinding bowl recipes are hardcoded in `GrindingBowlBlockEntity.getRecipes()`** — 29 recipes live in a static `Map<Item, Item>` inside the block entity. Should be refactored to a custom `RecipeType<GrindingBowlRecipe>` + `RecipeSerializer` with JSON recipes under `data/thelionking/recipes/grinding/`. This would decouple game content from machine logic and enable datapack compatibility.

## Missing Quest Mechanics

- [ ] **Scar has no natural spawn** — `ScarEntity` is registered but has no spawning logic (no biome spawn entry, no structure placement, no quest-triggered spawn). In the old mod, Scar appeared at Pride Rock. Needs either a structure-based placement or a quest-triggered spawn (e.g. spawning near the player when the `DEFEAT_SCAR` stage is reached). Currently only accessible via `/summon thelionking:scar`.
- [ ] **Outlands quest stage 4 (`THROW_IN_OUTWATER`) has no trigger** — The Outwater throwing mechanic is not implemented. Stage blocks progression until manually advanced. Needs: detect player throwing ingots into an Outwater pool block and fire a trigger to advance.
- [ ] **Outlands quest stage 6 (`FOLLOW_OUTLANDERS`) has no trigger** — The Outlander follow/march mechanic is not implemented. Needs: Outlander NPCs path to Pride Lands and trigger fires on arrival or proximity.
- [ ] **Outlands quest stage 7 (`ZIRA_OCCUPIES_TREE`) has no trigger** — Zira occupying Rafiki's tree is not implemented. Needs: Zira entity placed at tree location, quest auto-advances or triggers on player proximity.
- [ ] **Outlands quest stage 11 (`RAFIKI_RETURNS`) has no trigger** — Rafiki returning to his tree after Pumbaa Box is not implemented. Needs: Rafiki entity returns to tree, quest auto-advances or triggers on player proximity.

## New Features

- [ ] **Pride Compass — points to last-used portal** — Create a custom compass item that reads `PlayerData.homePortalX/Y/Z` and points the needle toward the player's last-used portal. Saves automatically when entering a portal. Replace the vanilla compass in dungeon loot with this item. Needs: custom item class, client-side needle rendering, item texture.

## Advancement Triggers (Not Fully Wired)

- [ ] **`USE_GRINDING_BOWL` trigger is orphaned** — `UseGrindingBowlTrigger` is registered and fires in `GrindingBowlMenu.onTake`, but no advancement uses it. The old mod had no grinding achievement. Decision needed: remove the trigger + `UseGrindingBowlTrigger` class entirely, or add a new "first grind" advancement not present in the old mod.

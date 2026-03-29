# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Placeholder Textures

- [ ] **`corrupt_hoe`** — Old mod had no corrupt hoe; using generated purple placeholder
- [ ] **`mounted_shooter` block textures** — Old mod only had item textures; block front/side/top are solid-color placeholders
- [x] ~~**`outlands_altar` textures**~~ — RESOLVED: Migrated `poolFocus_top/side` from old assets. Fixed shape to 10/16 height. Added `noOcclusion`.
- [ ] **~150 block textures** need migration from old camelCase to snake_case
- [ ] **~160 item textures** need migration

## Deprecated API (1.21+ Migration)

- [ ] **7 blocks override deprecated `BlockBehaviour.use()`** — No replacement in 1.20.1. When upgrading to 1.21+, split
  into `useWithoutItem()` / `useItemOn()`: `PortalFrameBlock`, `ZiraMoundGateBlock`, `OutlandsAltarBlock`,
  `BongoDrumBlock`, `BananaCakeBlock`, `BugTrapBlock`, `GrindingBowlBlock`
- [ ] **3 blocks override deprecated `BlockBehaviour.onRemove()`** — `BugTrapBlock`, `GrindingBowlBlock`,
  `BongoDrumBlock`
- [ ] **`LionKingFlowerBlock` deprecated `FlowerBlock` constructor** — Switch to `Holder<MobEffect>` in 1.21+

## CharacterSpeech Split

- [ ] **`CharacterSpeech.java` is monolithic** — Should split dialogue into respective entity/questline classes. Keep
  shared `SpeechUtil.giveSpeech()` helper.

## Crop Block Models

- [ ] **Kiwano, Maize, and Yam 3D models need rework** — Current block models don't look right. Kiwano stem textures are
  generated placeholders.

## Crop Blocks

- [ ] **Kiwano worldgen places `kiwano_block` (fruit) on sand** — Matches old mod. Stem (`kiwano_stem`) is the crop
  version for player farming.

## Grinding Bowl Recipes (Hardcoded)

- [ ] **29 recipes hardcoded in `GrindingBowlBlockEntity`** — Should be refactored to `RecipeType<GrindingBowlRecipe>` +
  JSON recipes for datapack compatibility.

## Missing Quest Mechanics

- [ ] **Scar has no natural spawn** — Needs structure placement or quest-triggered spawn. Currently only `/summon`.
- [ ] **Outlands quest stage 4 (`THROW_IN_OUTWATER`)** — No trigger. Outwater throwing mechanic not implemented.
- [ ] **Outlands quest stage 6 (`FOLLOW_OUTLANDERS`)** — No trigger. Outlander march mechanic not implemented.
- [ ] **Outlands quest stage 7 (`ZIRA_OCCUPIES_TREE`)** — No trigger. Zira tree occupation not implemented.
- [ ] **Outlands quest stage 11 (`RAFIKI_RETURNS`)** — No trigger. Rafiki return mechanic not implemented.

## New Features

- [ ] **Pride Compass — points to last-used portal** — Custom compass item reading `PlayerData.homePortalX/Y/Z`. Replace
  vanilla compass in dungeon loot.

## Advancement Triggers

- [ ] **`USE_GRINDING_BOWL` trigger is orphaned** — Registered and fires but no advancement uses it. Remove or add a "
  first grind" advancement.

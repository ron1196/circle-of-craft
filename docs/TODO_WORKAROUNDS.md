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


## Grinding Bowl Recipes (Hardcoded)

- [x] ~~**Grinding bowl recipes**~~ — RESOLVED: Refactored to `RecipeType<GrindingBowlRecipe>` + 25 JSON recipes in `recipes/grinding/`.

## Missing Quest Mechanics

- [x] ~~**Scar spawn**~~ — RESOLVED: Quest-triggered spawn in caves when player brings bones to Rafiki.
- [ ] **Outlands quest stage 6 (`FOLLOW_OUTLANDERS`)** — No trigger. Outlander march mechanic not implemented.
- [ ] **Outlands quest stage 7 (`ZIRA_OCCUPIES_TREE`)** — No trigger. Zira tree occupation not implemented.
- [ ] **Outlands quest stage 11 (`RAFIKI_RETURNS`)** — No trigger. Rafiki return mechanic not implemented.

## New Features

- [ ] **Pride Compass — points to last-used portal** — Custom compass item reading `PlayerData.homePortalX/Y/Z`. Replace
  vanilla compass in dungeon loot.

## Advancement Triggers

- [ ] **`USE_GRINDING_BOWL` trigger is orphaned** — Registered and fires but no advancement uses it. Remove or add a "
  first grind" advancement.

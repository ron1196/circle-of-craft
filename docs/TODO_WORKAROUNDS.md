# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Placeholder Textures

- [x] ~~**`corrupt_hoe`**~~ — RESOLVED: Proper texture added.
- [x] ~~**`mounted_shooter` needs BlockEntityRenderer**~~ — RESOLVED: Ported 3D model + BlockEntityRenderer from old mod. Block now extends BaseEntityBlock with fire recoil animation.
- [x] ~~**`outlands_altar` textures**~~ — RESOLVED: Migrated `poolFocus_top/side` from old assets. Fixed shape to 10/16 height. Added `noOcclusion`.
- [x] ~~**Remaining placeholder textures**~~ — RESOLVED: Migrated maize crop stages, banana cake bottom from old mod. Deleted unused mounted shooter face textures and tilled sand item texture.

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
- [x] ~~**Outlands quest stages 6, 7, 11**~~ — RESOLVED: March cutscene (flame particles, Outlanders/Zira vanish), tree occupation (Zira spawns at tree, 3-part dialogue, Rafiki despawns), Pumbaa Box explosion triggers Rafiki return + auto-advance.

## New Features

- [ ] **Pride Compass — points to last-used portal** — Custom compass item reading `PlayerData.homePortalX/Y/Z`. Replace
  vanilla compass in dungeon loot.

## Advancement Triggers

- [x] ~~**`USE_GRINDING_BOWL` trigger is orphaned**~~ — RESOLVED: Added "...Squash Banana!" advancement (use_grinding_bowl.json).
- [ ] **`feed_animal` trigger never fires** — Registered for "The Animal Whisperer" advancement (feed animal with Animalspeak Amulet). Needs Animalspeak Amulet feature implemented.
- [ ] **`teleport_simba` trigger never fires** — Registered for "Nants Ingonyama" advancement (take Simba through a portal). Needs Simba portal teleport feature implemented.

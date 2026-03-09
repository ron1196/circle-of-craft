# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Recipes

- [x] ~~**Bug Stew recipe uses `minecraft:milk_bucket` instead of `thelionking:jar_milk`**~~ — RESOLVED: updated to `thelionking:jar_milk`
  - File: `src/main/resources/data/thelionking/recipes/bug_stew.json`

## Items

- [ ] **Banana Cake item/block not yet implemented**
  - Original mod had a placeable cake block (`LKBlockBananaCake`) and item (`bananaCake`)
  - Recipe: milk jars + bananas + zazu egg + wheat (cake-style)
  - Needs: block class, block entity(?), item, textures, recipe

## Models / Rendering

- [ ] **NPC entities use placeholder models**
  - Scar, Zira, Ticket Lion use `NpcPlaceholderModel` instead of proper unique models
  - Files: `src/main/java/.../client/model/NpcPlaceholderModel.java`
  - Need custom models ported from old mod or newly designed

## Missing Items (from old mod, not yet ported)

- [x] ~~**Jar items** — `jar_empty`, `jar_milk`, `jar_water`~~ — RESOLVED: all three items now registered in LKItems
- [x] ~~**Zazu Egg**~~ — RESOLVED: now registered in LKItems
- [x] ~~**Nuka Shard**~~ — RESOLVED: now registered in LKItems
- [x] ~~**Kivulite**~~ — RESOLVED: now registered in LKItems

## Advancements

- [x] ~~**21 advancements use `minecraft:impossible` trigger**~~ — DONE (Phase 13): replaced with custom criteria triggers and vanilla triggers
- [ ] **Remaining advancement icon substitutions** (items not yet registered):
  - `lion_dust` → `termite_dust`, `outlandish_dart` → `dart_black`
  - `tunnah_diggah` → `pridestone_shovel`, `ticket_lion_helmet` → `ticket_lion_head`
  - `peacock_wings` → `peacock_gem`
- [x] ~~`kivulite_pickaxe` → `kivulite`~~ — DONE: updated to `thelionking:kivulite_pickaxe`
- [x] ~~`simba_charm` → `rafiki_coin`~~ — DONE: updated to `thelionking:simba_charm`
- [x] ~~`passion_fruit` → `kiwano`~~ — DONE: updated to `thelionking:passion_fruit`
- [x] ~~`animalspeak_amulet` → `crystal`~~ — DONE: updated to `thelionking:amulet`
- [x] ~~`giraffe_saddle` → `minecraft:saddle`~~ — DONE: updated to `thelionking:giraffe_saddle`

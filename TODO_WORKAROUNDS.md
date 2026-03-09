# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Recipes

- [ ] **Bug Stew recipe uses `minecraft:milk_bucket` instead of `thelionking:jar_milk`**
  - File: `src/main/resources/data/thelionking/recipes/bug_stew.json`
  - Original recipe used `jar_milk` (a custom item from the old mod)
  - Jar milk item needs to be created first, then update this recipe

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

- [ ] **Jar items** — `jar_empty`, `jar_milk`, `jar_water` (used in multiple recipes)
- [ ] **Zazu Egg** — used in banana cake recipe
- [ ] **Nuka Shard** — smelted from pride_coal_ore (meta 1, needs separate ore block?)
- [ ] **Kivulite** — smelted from silver_ore (meta 1, needs separate ore block?)

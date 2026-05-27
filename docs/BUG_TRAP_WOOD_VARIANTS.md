# Bug Trap Wood Variants — TODO

The bug trap currently exists only as a `pride_acacia`-planks variant (registered as plain `circleofcraft:bug_trap`). Goal: support every plank type the player can craft with, like vanilla slabs/stairs.

## Approach

- Keep existing `bug_trap` as the pride_acacia variant (backward-compatible with existing world saves).
- Add `<wood>_bug_trap` blocks for every other plank.
- Reuse the existing 3 model files (`bug_trap.json`, `bug_trap_closing_face.json`, `bug_trap_closed_face.json`) as **parent templates**. Each per-wood variant is a tiny child model overriding only the `planks` texture:

  ```json
  {
    "parent": "circleofcraft:block/bug_trap",
    "textures": { "planks": "minecraft:block/oak_planks" }
  }
  ```

- Each variant needs:
  - 3 model child files (open, closing, closed)
  - 1 blockstate JSON (15 variants, using uvlock + rotation as the pride_acacia version does)
  - Block registration in `LionKingBlocks`
  - Item registration in `LionKingItems`
  - Add block to `BlockEntityTypes.BUG_TRAP` valid blocks list
  - 1 recipe JSON (shaped: 6 planks + 2 silver ingots, like the current `bug_trap` recipe)
  - 1 loot table JSON (drops self)
  - 1 lang entry

## Status

### Done
- [x] `pride_acacia` (existing `bug_trap`)

### Vanilla woods (11)
- [x] `oak`
- [x] `spruce`
- [x] `birch`
- [x] `jungle`
- [x] `acacia` (vanilla, separate from `pride_acacia`)
- [x] `dark_oak`
- [x] `mangrove`
- [x] `cherry`
- [x] `bamboo`
- [x] `crimson`
- [x] `warped`

### Mod woods (5)
- [x] `banana`
- [x] `rainforest`
- [x] `mango`
- [x] `passion`
- [x] `deadwood`

**All 17 variants implemented.**

## Texture inventory

Mod plank textures present in `assets/circleofcraft/textures/block/`:
- `pride_acacia_planks.png` ✓
- `banana_planks.png` ✓
- `deadwood_planks.png` ✓
- `mango_planks.png` ✓
- `passion_planks.png` ✓
- `rainforest_planks.png` ✓

All vanilla plank textures are provided by Minecraft (no new PNGs needed).

**No missing textures.** Every wood we want to support has a usable plank texture available.

## Implementation order suggestion

1. Refactor existing `bug_trap` block class so it can be instantiated per-wood (parametrize the block reference name).
2. Add a registration helper `registerBugTrap(String wood, RegistryObject<Block> planks)` that creates the block + item.
3. Bulk-generate model/blockstate/recipe/loot JSONs via a small script (each file is ~10 lines).
4. Walk through each wood and add the lang entry.

## Notes

- BlockEntityType needs all variants in its `.validBlocks(...)` chain. Forge's `BlockEntityType.Builder.of(factory, blocks...)` accepts varargs.
- Recipe inputs must use the appropriate `minecraft:` plank or `circleofcraft:` plank itemId.
- For consistency with vanilla, use `<wood>_bug_trap` naming for everything except `pride_acacia` which stays as plain `bug_trap`.

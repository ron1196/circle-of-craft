# Temporary Workarounds & TODOs

This file tracks all "for now" substitutions and temporary workarounds that need to be revisited.

## Recipes

- [x] ~~**Bug Stew recipe uses `minecraft:milk_bucket` instead of `thelionking:jar_milk`**~~ — RESOLVED: updated to `thelionking:jar_milk`
  - File: `src/main/resources/data/thelionking/recipes/bug_stew.json`

## Items

- [x] ~~**Banana Cake item/block not yet implemented**~~ — RESOLVED (Phase 12): `BananaCakeBlock`, block item, textures from old mod, blockstate with 7 bite variants

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

## Placeholder Textures

Items using generated placeholder textures (not from old mod):

- [ ] **`kivulite_hoe`** — Old mod had no kivulite hoe; using generated teal placeholder
- [ ] **`corrupt_hoe`** — Old mod had no corrupt hoe; using generated purple placeholder
- [ ] **`mounted_shooter` block textures** — Old mod only had item textures (`mountedShooter_wood.png`, `mountedShooter_silver.png`); block front/side/top are solid-color placeholders
- [ ] **`outlands_altar` block texture** — No old texture exists; reusing `corrupt_pridestone.png`
- [ ] **`tilled_sand` item texture** — Generated sandy placeholder; block textures from old mod are correct
- [ ] **`star_altar` item texture** — Generated placeholder; block textures (side/top) from old mod are correct
- [ ] **`outlands_altar` item texture** — Generated dark placeholder

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

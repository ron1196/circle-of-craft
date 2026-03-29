# Testing Guide — Phases 9-13

Last updated: 2026-03-09

## How to Run

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew runClient
```

Create a new world in Creative mode. Most tests use commands.

---

## Phase 9: Events, Networking & Tick Handlers

### What it does
Wires up the Forge event bus (`LKForgeEvents`) so combat mechanics, NPC interactions, and tick handlers fire. Adds a `SimpleChannel` network for multiplayer packet sync.

### Tests

#### Scourge of Hyenas Enchantment
```
/give @p diamond_sword{Enchantments:[{id:"thelionking:scourge_of_hyenas",lvl:5}]} 1
/summon thelionking:hyena
```
Hit the hyena — should take significantly more damage (+2.5 per level = +12.5 extra at level 5). Compare with an unenchanted sword to see the difference.

#### Peacock Boots Fall Immunity
```
/give @p thelionking:peacock_boots
```
Build up 20+ blocks, jump off. No fall damage should be taken.

#### Hyena Head Drop
```
/give @p diamond_sword{Enchantments:[{id:"minecraft:looting",lvl:3}]} 1
```
Kill hyenas repeatedly. Base 5% drop chance (+3% per looting level = 14% at Looting III). A hyena head item should eventually drop.

#### NPC Interaction — Rafiki
```
/summon thelionking:rafiki
```
Right-click Rafiki. Expected:
- "Rafiki greets you!" appears in chat
- A Quest Book appears in your inventory (only once — right-click again, no duplicate)

#### NPC Interaction — Timon
```
/summon thelionking:timon
```
Right-click Timon. Expected: "Timon is ready to trade with you!" in chat.

#### NPC Interaction — Ticket Lion
```
/summon thelionking:ticket_lion
```
Right-click. Expected: "The Ticket Lion can sell you passage to the Pride Lands!" in chat.

### Networking (LAN Test)

#### What exists
- `LKNetworking.java` — SimpleChannel on `thelionking:main`, protocol version `"1"`
- `SimbaSitPacket` — Client→Server, toggles Simba sit/stand
- `QuestSyncPacket` — Server→Client, syncs quest index/stageKey/checked
- `QuestCheckPacket` — Client→Server, marks quest objective checked

#### How to test LAN
1. Launch with `./gradlew runClient`
2. Load into a world
3. Press **Esc → Open to LAN → Start LAN World**
4. Have a second instance join (or friend on same network)

#### What you can verify now
- **No crash on LAN join** — The protocol version handshake works. Both sides must agree on version `"1"` or the connection is rejected.
- **Server events fire for both players** — Both host and LAN player get quest books from Rafiki, Scourge enchant works for both, etc.
- **No "channel mismatch" errors** — Check the log for any network-related errors.

#### What's NOT wired up yet
The packet classes exist and the channel is registered, but **no client-side code sends them yet**. To fully test packet send/receive:

1. **SimbaSitPacket** — Needs a client-side right-click handler or keybind that calls:
   ```java
   LKNetworking.CHANNEL.sendToServer(new SimbaSitPacket(simbaEntityId));
   ```
2. **QuestSyncPacket** — Needs server code to send on quest state change:
   ```java
   LKNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new QuestSyncPacket(...));
   ```
3. **QuestCheckPacket** — Needs a client GUI button to send:
   ```java
   LKNetworking.CHANNEL.sendToServer(new QuestCheckPacket(questIndex, checked));
   ```

These are small follow-up tasks. The infrastructure is in place.

---

## Phase 10: Recipes, Items & World Gen

### What it does
~100 crafting recipes, ~20 new items (Kivulite/Corrupt tools, jars, quest items), ore generation, crop patches, 3 tree features, 29 grinding bowl recipes.

### Tests

#### New Items Exist
```
/give @p thelionking:kivulite_sword
/give @p thelionking:corrupt_pickaxe
/give @p thelionking:jar_empty
/give @p thelionking:jar_milk
/give @p thelionking:amulet
/give @p thelionking:simba_charm
/give @p thelionking:giraffe_saddle
/give @p thelionking:passion_fruit
/give @p thelionking:dart_quiver
/give @p thelionking:zazu_egg
```
All should appear as valid items. Check creative tabs too.

#### Crafting Recipes
```
/give @p thelionking:silver_ingot 64
/give @p thelionking:peacock_gem 64
/give @p thelionking:kivulite 64
```
Open crafting table. Use the recipe book (green book icon) and search "thelionking" or browse:
- **Tools:** Silver, peacock, kivulite, pridestone, corrupt — sword/pickaxe/axe/shovel/hoe (25 recipes)
- **Armor:** Silver, gemsbok, peacock — helmet/chestplate/leggings/boots (12 recipes)
- **Wood:** 6 wood types — planks from logs, stairs, slabs (18 recipes)
- **Weapons:** 5 colored darts, 2 dart shooters, spears, pumbaa bomb (10 recipes)

#### Ore Generation
```
/execute in thelionking:pride_lands run tp @p 0 -40 0
```
Switch to survival or use `/gamemode spectator` and look around underground. Expected ores:
- `pride_coal_ore` — common, Y -16 to 192
- `silver_ore` — moderate, Y -16 to 64
- `peacock_ore` — rare, Y -16 to 32

#### Tree Features
Fly around in Pride Lands:
- **Rainforest biome**: Tall trees (8-12 blocks), wide 3-layer canopy
- **Savannah**: Mango trees (medium, rounded canopy)
- **Wooded savannah**: Passion trees (medium, passion leaves)

#### Grinding Bowl (29 recipes)
```
/give @p thelionking:grinding_bowl
```
Place it, right-click to open. Put items in the input slot:
- Bone → Bone Meal
- Feather → White Dye
- Various flowers → dyes
- Termite → Termite Dust
- Mango → Mango Dust

---

## Phase 11: Entity AI, Outlands Fix & Boss

### What it does
16 custom AI goals wired into entities. Lions hunt prey, Simba follows player, prey flee predators. Termite Queen boss (200 HP). Outlands has lava below Y=63.

### Tests

#### Lions Hunt Prey
```
/summon thelionking:lion
/summon thelionking:zebra ~ ~ ~5
/summon thelionking:dikdik ~ ~ ~8
/summon thelionking:gemsbok ~ ~ ~10
```
Lions should stalk and attack zebras, dikdiks, and gemsboks. Not guaranteed every time (1/200 tick hunger check), so wait ~10 seconds.

#### Prey Flee Predators
```
/summon thelionking:zebra ~ ~ ~3
/summon thelionking:hyena
```
Zebras, dikdiks should panic and run (12-block detection range). Giraffes and gemsboks flee from lions/lionesses only (not hyenas).

#### Pumbaa Follows Timon
```
/summon thelionking:timon
/summon thelionking:pumbaa ~ ~ ~15
```
Wait — Pumbaa should walk toward Timon (32-block search radius).

#### Bugs Find Traps
```
/give @p thelionking:bug_trap
```
Place the bug trap, put bait in input slot. Then:
```
/summon thelionking:bug ~ ~ ~10
```
Bug should pathfind toward the baited trap (16-block range).

#### Zazu Egg Production
```
/summon thelionking:zazu
```
Wait ~5 minutes. Zazu should drop a zazu_egg item (every ~6000 ticks).

#### Termite Queen Boss
```
/summon thelionking:termite_queen
```
Expected:
- Purple boss health bar appears (200 HP)
- She attacks you in melee when close (3 blocks)
- Spawns 1-3 termite minions every 5 seconds when you're far (capped at 8 nearby)
- Fight her to death — drops nuka shards and crystals

#### Outlands Lava
```
/execute in thelionking:outlands run tp @p 0 80 0
```
Dig down. Below Y=63 should be lava (custom noise settings with `default_fluid: lava`).

---

## Phase 12: Blocks, GUIs & Landmarks

### What it does
6 new blocks, 3 GUI menus, 5 landmark structure features placed in biomes.

### Tests

#### New Blocks
```
/give @p thelionking:banana_cake
/give @p thelionking:mounted_shooter
/give @p thelionking:star_altar
/give @p thelionking:outlands_altar
/give @p thelionking:tilled_sand
/give @p thelionking:vase
```
- **Banana Cake**: Place and right-click to eat slices (7 bites, like vanilla cake)
- **Mounted Shooter**: Place, apply redstone signal → should shoot darts
- **Star Altar / Outlands Altar**: Place — decorative quest blocks
- **Tilled Sand**: Farmland variant for Pride Lands crops
- **Vase**: Decorative block

#### GUIs
The 3 menus (Quiver, Timon Merchant, Simba Inventory) are registered and the screens render. They open via NPC interaction or item use. Currently the open triggers are partially wired:
- **Timon Merchant**: Right-click Timon (currently shows chat message, full trade GUI needs the menu to be opened via `player.openMenu()`)
- **Quiver**: Needs right-click handler on dart_quiver item
- **Simba Inventory**: Needs right-click handler on Simba entity

#### Landmark Structures
These generate as worldgen features in Pride Lands / Outlands biomes. They are rare.

| Structure | Biomes | Rarity | What to look for |
|-----------|--------|--------|-----------------|
| Rafiki's Tree | Savannah, grassland, wooded, arid | 1/256 chunks | Giant 16-block 2x2 trunk, Rafiki NPC on top |
| Ticket Booth | Savannah, grassland, wooded, arid | 1/128 chunks | Small pridestone/brick building, Ticket Lion inside |
| Timon & Pumbaa Lodge | Savannah + rainforest biomes | 1/128 chunks | Mango wood hut, both NPCs inside |
| Zira's Mound | Outlands, outlands_mountains | 1/256 chunks | Corrupt pridestone cone, Zira on top |
| Treasure Mound | Outlands, outlands_mountains | 1/64 chunks | Small dome with chest inside |

To find landmarks faster:
```
/execute in thelionking:pride_lands run tp @p 0 80 0
/gamemode spectator
```
Fly around at speed. Structures are surface-level and visible. For Outlands:
```
/execute in thelionking:outlands run tp @p 0 80 0
```

---

## Phase 13: Advancements, Quests & Polish

### What it does
11 custom advancement triggers, 21 advancements updated from `impossible` to real triggers, quest stageKey transitions (Rafiki 7 stages, Outlands 10 stages), workaround fixes.

### Tests

#### Advancements
Open advancement screen (**L** key). Look for the Lion King tab.

Test manual granting:
```
/advancement grant @p only thelionking:enter_pride_lands
/advancement grant @p only thelionking:kill_scar
/advancement grant @p only thelionking:shoot_dart
```

Test real triggers (these fire when the custom trigger is activated in code):
- Enter Pride Lands dimension → `enter_pride_lands`
- Kill a hyena → `kill_hyena`
- Get a mango → `get_mango`

#### Rafiki Quest Progression
1. `/summon thelionking:rafiki` → right-click → get Quest Book (stageKey 0→1)
2. Collect 5 hyena bones (`/give @p thelionking:hyena_bone 5`) → talk to Rafiki (stageKey 1→2)
3. Find Simba NPC → interact (stageKey 2→3)
4. Kill 10 hyenas (stageKey 3→4)
5. Kill Scar: `/summon thelionking:scar` → kill (stageKey 4→5)
6. Return to Rafiki → talk (stageKey 5→6)
7. Receives rewards: Simba Charm, 16 gold ingots, Rafiki Staff (stageKey 6→7 COMPLETE)

Note: Quest stageKey advancement relies on `tryAdvanceStage()` being called, which happens in the player tick handler every 20 ticks. Some stages need manual NPC interaction triggers to be fully wired.

#### Outlands Quest Progression
1. Enter Outlands dimension (stageKey 0→1)
2. Find and talk to Zira NPC (stageKey 1→2)
3. Collect 20 corrupt pridestone (stageKey 2→3)
4. Kill 15 outlanders (stageKey 3→4)
5. Find Outlands Altar (stageKey 4→5)
6. Place offering on altar (stageKey 5→6)
7. Kill Termite Queen (stageKey 6→7)
8. Kill Zira (stageKey 7→8)
9. Return to Pride Lands (stageKey 8→9)
10. Complete — rewards: Wayward Feathers, Kivulite, Diamonds, Amulet

#### Bug Stew Recipe Fix
```
/give @p thelionking:jar_milk
```
Verify bug stew recipe uses `thelionking:jar_milk` (not `minecraft:milk_bucket`).

---

## Quick Smoke Test Checklist

| # | Test | Command | Expected |
|---|------|---------|----------|
| 1 | Mod loads | — | No crash, creative tabs visible |
| 2 | Kivulite sword | `/give @p thelionking:kivulite_sword` | Item appears with texture |
| 3 | Crafting works | Crafting table + silver ingots | Recipes in recipe book |
| 4 | Peacock boots | Equip + jump 20 blocks | No fall damage |
| 5 | Lion hunts zebra | `/summon` both nearby | Lion chases zebra |
| 6 | Termite Queen | `/summon thelionking:termite_queen` | Boss bar, 200 HP |
| 7 | Rafiki interaction | `/summon thelionking:rafiki` + right-click | Quest Book given |
| 8 | Banana cake | `/give @p thelionking:banana_cake` | Places, edible |
| 9 | Pride Lands ores | Dig underground in Pride Lands | Silver/peacock ore |
| 10 | Outlands lava | Outlands below Y=63 | Lava, not water |
| 11 | LAN join | Open to LAN, second client joins | No crash or channel mismatch |

---

## Known Limitations

These are implemented but not fully wired end-to-end:

| Feature | Status | What's missing |
|---------|--------|----------------|
| Network packets | Channel registered, packets defined | Client-side send triggers (keybinds, GUI buttons) |
| Timon trade GUI | Menu + screen exist | `player.openMenu()` call in NPC interaction |
| Quiver GUI | Menu + screen exist | Right-click handler on dart_quiver item |
| Simba Inventory GUI | Menu + screen exist | Right-click handler on Simba entity |
| Quest stageKey auto-advance | Logic in `tryAdvanceStage()` | Some stages need NPC interaction hooks to call it |
| Advancement triggers | Custom triggers registered | Some need to be fired from game events (e.g. `LKCriteriaTriggers.SHOOT_DART.trigger(player)`) |

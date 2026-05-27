# Quest System Redesign — Complete Spec

## Overview

Redesign the quest system to properly separate world-level state from per-player state, support multiplayer, and unify the main quest and animal mini-quest systems under a clean architecture.

## Data Storage

### `LKWorldData` (replaces `LKLevelData`, extends `SavedData`)

World-level persistent data shared across all players.

| Field | Type | Purpose |
|-------|------|---------|
| `defeatedScar` | `boolean` | Whether Scar has been killed |
| `ziraStage` | `int` | Controls Zira dramatic spawn event |
| `outlandersHostile` | `boolean` | Whether Outlander mobs are hostile |
| `pumbaaStage` | `int` | Timon/Pumbaa dialogue sequence progress (0-13) |
| `questManager` | `LKQuestManager` | Owns all main quest state |

Portal coordinates are **not** stored here — they are per-player (see `LKPlayerData`).

The old `simbas` map (`Map<String, Integer>`) is removed. Simba ownership is now tracked per-player via `LKPlayerData.hasSimba`. The old map stored a boolean-as-int (1 = has simba, 0 = no simba), so a simple boolean is sufficient. On upgrade, the old map data is lost — all players can spawn a new Simba, which is acceptable.

NBT keys: same as current `LKLevelData` for backwards compatibility (`DefeatedScar`, `ZiraStage`, etc.). Legacy migration reads old keys on first load.

Rename `LKLevelData` → `LKWorldData` everywhere. The `DATA_NAME` constant stays `"circleofcraft_data"` (unchanged value).

### `LKPlayerData` (new Forge Capability on player)

Per-player persistent data attached via `AttachCapabilitiesEvent<Entity>`.

| Field | Type | Purpose |
|-------|------|---------|
| `receivedQuestBook` | `boolean` | Whether this player got a Quest Book from Rafiki |
| `homePortalX/Y/Z` | `int` | This player's Pride Lands portal location |
| `hasSimba` | `boolean` | Whether this player has spawned a Simba (prevents duplicates) |
| `claimedRewards` | `Set<String>` | Reward keys already collected (e.g. `"rafiki:1"`) |

**Capability registration:**
- `LKPlayerDataProvider` implements `ICapabilitySerializable<CompoundTag>`
- Attached in `AttachCapabilitiesEvent<Entity>` for `Player` entities
- Key: `ResourceLocation("circleofcraft", "player_data")`

**Copy-on-death:**
- Handle `PlayerEvent.Clone` event
- Copy all `LKPlayerData` fields from old player to new player
- This ensures data survives respawn

**NBT format:**
```
PlayerData {
  ReceivedQuestBook: boolean
  HomePortalX: int
  HomePortalY: int
  HomePortalZ: int
  HasSimba: boolean
  ClaimedRewards: ListTag of StringTags
}
```

### Animal Quest Storage (per-player, on entity)

`LKAnimal` changes from single `LKAnimalQuest` to `Map<UUID, AnimalQuestEntry>`.

Each `AnimalQuestEntry` contains:
- `Item requiredItem`
- `int requiredAmount`

Serialized in entity NBT as:
```
AnimalQuests {
  "<uuid-string>" {
    QuestItem: string (registry name)
    QuestAmount: int
  }
  ...
}
```

Old single-slot format (`AnimalQuest { HasQuest, QuestItem, QuestAmount }`) is not migrated — animal quests are transient enough that losing them on upgrade is acceptable.

Note: wild animals can despawn naturally, which silently removes any active quests on that entity. This matches the old mod's behavior and is acceptable — animal quests are lightweight and easily re-assigned.

## Quest System

### `LKQuestManager` (world-level, owned by `LKWorldData`)

Holds `Map<String, LKQuestState>` — one state per registered quest.

**Core method `tryAdvance(questId, player, trigger)`:**
1. Look up quest definition and current state
2. If complete, return false
3. If no trigger is defined for the current stageKey, return false (stageKey cannot be advanced via `tryAdvance`)
4. If trigger doesn't match expected trigger for current stageKey, return false
5. Check item requirements against the advancing player
6. Consume items from the advancing player
7. Run custom transition (if defined for this stageKey)
8. Increment stageKey, set checked = false
9. Mark `LKWorldData` dirty
10. Sync to all players
11. Return true

**No reward giving.** Rewards are claimed separately through NPC interaction.

**Stages with no trigger:** Some Outlands quest stages (3, 5, 6, 7, 8) have no trigger defined. These are placeholder stages for future gameplay mechanics (Outwater throwing, Outlander following, exploration). They cannot be advanced via `tryAdvance` — the quest blocks at these stages until the mechanics are implemented. For testing, a `/lkquest advance <questId>` command can skip them.

Other methods: `getStage(questId)`, `isComplete(questId)`, `canStart(questId)`, `anyUnchecked()`, `syncToPlayer(player)`, `syncToAllPlayers(server)`, `writeToNBT(tag)`, `readFromNBT(tag)`.

NBT format (new):
```
Quests {
  "rafiki" { Stage: int, Checked: boolean }
  "outlands" { Stage: int, Checked: boolean }
}
```

Legacy migration: if `"Quests"` compound missing but `"Quest_0_Stage"` exists, read index-based format via `Map.of(0, "rafiki", 1, "outlands")`.

### `LKQuest` (immutable definition)

Fields:
- `id` (String)
- `displayName` (String)
- `icon` (Supplier<ItemStack>)
- `stages` (List<LKQuestStage>)
- `canStart` (Predicate<LKQuestManager>)
- `prerequisites` (String[]) — display text for UI
- `triggerByStage` (Map<Integer, LKQuestTrigger>)
- `customTransitions` (Map<Integer, BiConsumer<ServerPlayer, LKQuestManager>>)
- `claimableRewards` (Map<Integer, List<ClaimableReward>>)

Built via `LKQuest.builder(id)`.

### `ClaimableReward` (new record)

```java
record ClaimableReward(Supplier<Item> item, int count, String rewardKey)
```

`rewardKey` is the string stored in `LKPlayerData.claimedRewards` (e.g. `"rafiki:1"`).

### `LKQuestStage` (simplified)

```java
record LKQuestStage(
    String objectiveText,
    List<ItemRequirement> requirements
)
```

`ItemReward` removed from the record. Rewards live in `LKQuest.claimableRewards`.

`ItemRequirement` unchanged: `record ItemRequirement(Supplier<Item> item, int count, Source source)`. Default source is `Source.MAIN_HAND` (used by Rafiki quest requirements). Outlands quest explicitly specifies `Source.INVENTORY` for silver ingots.

### `LKQuestRegistry`

Builds both quests declaratively. Example for Rafiki:

```java
LKQuest.builder("rafiki")
    .displayName("Rafiki's Quest")
    .icon(() -> new ItemStack(LKItems.RHYTHM_STAFF.get()))
    .stageKey(new LKQuestStage("Find Rafiki and speak to him"))
    .stageKey(new LKQuestStage("Bring Rafiki 64 hyena bones",
        List.of(new ItemRequirement(() -> LKItems.HYENA_BONE.get(), 64))))
    .stageKey(new LKQuestStage("Defeat Scar"))
    // ... remaining stages
    .trigger(0, RAFIKI_TALK)
    .trigger(1, RAFIKI_TALK)
    .trigger(2, SCAR_KILLED)
    // ... remaining triggers
    .claimableReward(1, new ClaimableReward(() -> LKItems.RHYTHM_STAFF.get(), 1, "rafiki:1"))
    .build();
```

Stage constants remain as `public static final int` fields for readability in entity code.

Eligible items for animal mini-quests are configured in `LKAnimal.getQuestRequestItems()` (returns array of items: MANGO, BANANA, CORN, KIWANO, APPLE, BREAD, WHEAT). Random amount is 1-5. This is unchanged from the current implementation.

### `LKQuestTrigger` (enum, unchanged)

`RAFIKI_TALK`, `SCAR_KILLED`, `STAR_ALTAR_USED`, `ENTER_OUTLANDS`, `ZIRA_TALK`, `ZIRA_KILLED`

### `LKQuestState` (unchanged)

Mutable: `currentStage` (int), `checked` (boolean). NBT helpers.

## NPC Interaction Flow

All quest NPCs follow this pattern:

```
Player clicks NPC:
  1. Find earliest unclaimed reward for this player
     → If found: give reward, send stageKey dialogue, return
  2. Try to advance quest (trigger + requirements)
     → If advanced: send advancement dialogue, return
  3. Send contextual speech (hints based on current stageKey)
```

### Reward Claiming

Helper method on `LKQuest` or utility class:

```java
static boolean tryClaimNextReward(LKQuest quest, LKQuestManager manager,
                                   ServerPlayer player, LKPlayerData playerData) {
    int currentStage = manager.getStage(quest.getId());
    for (int stageKey = 0; stageKey < currentStage; stageKey++) {
        List<ClaimableReward> rewards = quest.getClaimableRewards(stageKey);
        if (rewards == null) continue;
        for (ClaimableReward reward : rewards) {
            if (!playerData.hasClaimedReward(reward.rewardKey())) {
                player.addItem(new ItemStack(reward.item().get(), reward.count()));
                playerData.claimReward(reward.rewardKey());
                return true; // caller sends dialogue
            }
        }
    }
    return false;
}
```

The method returns true when a reward is claimed. The NPC determines which dialogue to send based on which stageKey's reward was just claimed. The NPC can query the stageKey by checking which reward key was most recently added to `claimedRewards`, or the helper can return the stageKey index.

### RafikiEntity Interaction

```
1. Give quest book if not received (check LKPlayerData.receivedQuestBook)
2. Try claim next reward → if claimed, send stageKey dialogue, return
3. Try advance quest with RAFIKI_TALK → if advanced, send advancement dialogue, return
4. Send contextual speech based on current stageKey
```

### ZiraEntity Interaction

Same pattern with ZIRA_TALK trigger and Outlands quest rewards.

### ScarEntity Death

```
On die: LKWorldData.getQuestManager().tryAdvance("rafiki", killer, SCAR_KILLED)
```

### RafikiDustItem

```
Check LKPlayerData.hasSimba (from capability, not world data)
Call tryAdvance("rafiki", player, STAR_ALTAR_USED)
```

## Animal Mini-Quest Redesign

### `LKAnimal` Changes

Replace:
```java
private final LKAnimalQuest animalQuest = new LKAnimalQuest();
```
With:
```java
private final Map<UUID, AnimalQuestEntry> animalQuests = new HashMap<>();
```

### `AnimalQuestEntry` (new, simple record or inner class)

```java
record AnimalQuestEntry(Item requiredItem, int requiredAmount)
```

### Interaction Flow

```java
mobInteract(Player player, InteractionHand hand):
  UUID playerId = player.getUUID()
  AnimalQuestEntry entry = animalQuests.get(playerId)

  if entry exists:
    if player holds required item with enough count:
      consume items, give reward, remove entry from map
      send completion dialogue
    else:
      send reminder dialogue
  else:
    1-in-3 chance: assign random quest, put in map
    send quest start dialogue
```

Item pool and amounts configured in `LKAnimal.getQuestRequestItems()` (unchanged). Random amount: `1 + random.nextInt(5)`.

### `LKAnimalQuest` Changes

The class becomes a **static utility** for dialogue generation (phrases, number-to-word conversion) and `giveReward(player, animalType)`. All instance state is removed. The per-entity quest state lives in the `Map<UUID, AnimalQuestEntry>` on `LKAnimal`.

### NBT Serialization

```java
addAdditionalSaveData(CompoundTag tag):
  CompoundTag questsTag = new CompoundTag()
  for each (uuid, entry) in animalQuests:
    CompoundTag entryTag = new CompoundTag()
    entryTag.putString("Item", registryName(entry.requiredItem))
    entryTag.putInt("Amount", entry.requiredAmount)
    questsTag.put(uuid.toString(), entryTag)
  tag.put("AnimalQuests", questsTag)
```

## Networking

### Packets

| Packet | Direction | Purpose |
|--------|-----------|---------|
| `LoginSyncPacket` | S→C | Full sync on join: world flags, quest states, player data |
| `QuestSyncPacket` | S→C | Single quest state update |
| `QuestCheckPacket` | C→S | Player viewed quest in book |
| `PlayerDataSyncPacket` | S→C | Player data changed (reward claimed, etc.) |
| `SimbaSitPacket` | C→S | Unchanged |

### `LoginSyncPacket` Changes

Encoding order:
1. World flags: `defeatedScar` (boolean), `ziraStage` (varint), `outlandersHostile` (boolean), `pumbaaStage` (varint)
2. Quest states: count (varint), then for each: questId (utf), stageKey (varint), checked (boolean)
3. Player data: `receivedQuestBook` (boolean), `homePortalX` (int), `homePortalY` (int), `homePortalZ` (int), `hasSimba` (boolean), claimedRewards count (varint), then each reward key (utf)

Old world-level `homePortalX/Y/Z` fields are no longer sent in the world section.

### `PlayerDataSyncPacket` (new)

Sent when player data changes (reward claimed, quest book received, simba spawned, portal set).

Encodes all `LKPlayerData` fields in the same order as the player data section of `LoginSyncPacket`.

Client stores the data in `ClientWorldState` (single class, with both world and player sections).

### `ClientWorldState` Changes

Add player-specific fields alongside existing world fields:
- `receivedQuestBook` (boolean)
- `homePortalX/Y/Z` (int)
- `hasSimba` (boolean)
- `claimedRewards` (Set<String>)

Keep it as one class for simplicity. Rename would be misleading — it's "client-side cached state" for both world and local player.

### Protocol Version

Bump from `"2"` to `"3"`.

## Client / UI

### `QuestBookScreen`

- Reads quest list from `LKQuestRegistry.getOrdered()`
- Reads quest stages from `ClientWorldState.getQuestStage(id)`
- Reads `canStart` by checking prerequisite quest completion on client state
- Sends `QuestCheckPacket` with quest ID when selected
- Shows portal location from `ClientWorldState.homePortalX/Y/Z` (player's portal)
- Shows reward status per stageKey (claimed/unclaimed) using `ClientWorldState.claimedRewards`

### `QuestBookItem`

- `isFoil`: checks `ClientWorldState` for any unchecked quests
- Unchanged logic, just reads from client state

## File Changes

### New Files

| File | Purpose |
|------|---------|
| `quest/ClaimableReward.java` | Record: item supplier, count, reward key |
| `quest/AnimalQuestEntry.java` | Record: required item, required amount |
| `data/LKPlayerData.java` | Per-player capability class |
| `data/LKPlayerDataProvider.java` | Capability provider, serializer, attach event |
| `network/PlayerDataSyncPacket.java` | Syncs player data to client |

### Renamed Files

| Old | New |
|-----|-----|
| `data/LKLevelData.java` | `data/LKWorldData.java` |

### Modified Files

| File | Changes |
|------|---------|
| `quest/LKQuestStage.java` | Remove `ItemReward` from record, remove `rewards` field |
| `quest/LKQuest.java` | Add `claimableRewards` map, add `claimableReward()` builder method |
| `quest/LKQuestRegistry.java` | Move rewards to `claimableReward()` calls |
| `quest/LKQuestManager.java` | Remove reward giving from `tryAdvance`, handle null triggers |
| `quest/LKAnimalQuest.java` | Convert to static utility (dialogue, rewards). Remove instance state |
| `entity/animal/LKAnimal.java` | Replace single `LKAnimalQuest` with `Map<UUID, AnimalQuestEntry>` |
| `entity/npc/RafikiEntity.java` | Add reward claiming before quest advancement, use capability for quest book |
| `entity/npc/ScarEntity.java` | Use `LKWorldData` instead of `LKLevelData` |
| `entity/npc/ZiraEntity.java` | Add reward claiming, use `LKWorldData` |
| `item/RafikiDustItem.java` | Check `hasSimba` from capability |
| `item/QuestBookItem.java` | Unchanged logic, reads from client state |
| `event/LKForgeEvents.java` | Register capability, handle clone event, use `LKWorldData` |
| `network/LKNetworking.java` | Register `PlayerDataSyncPacket`, bump protocol to `"3"` |
| `network/LoginSyncPacket.java` | Include player data fields, remove world portal coords |
| `network/QuestSyncPacket.java` | Unchanged |
| `network/QuestCheckPacket.java` | Use `LKWorldData` |
| `network/ClientWorldState.java` | Add player data fields (`claimedRewards`, `hasSimba`, portal, etc.) |
| `client/gui/QuestBookScreen.java` | Adapt to new data sources |

### Deleted Files

| File | Reason |
|------|--------|
| `quest/LKQuestBase.java` | Already deleted |
| `quest/LKQuestRafiki.java` | Already deleted |
| `quest/LKQuestOutlands.java` | Already deleted |
| `quest/LKQuests.java` | Already deleted |

## NBT Migration

### World Data

`LKWorldData.load()`: Read current keys (`DefeatedScar`, `ZiraStage`, etc.) unchanged. Quest migration handled by `LKQuestManager.readFromNBT` (name-based → read directly, index-based `Quest_0_Stage` → migrate via legacy map). The old `simbas` map and `homePortalX/Y/Z` keys in world data are ignored on load (no migration — see rationale in Data Storage section).

### Player Data

No migration needed — `LKPlayerData` is new. Old `receivedQuestBook` was world-level; on first load it won't exist per-player, so all players will get a fresh quest book from Rafiki. This is acceptable.

Old `homePortalX/Y/Z` from world data is not migrated to per-player. Players re-enter the portal to set their location. Simpler than tracking which player the old coords belonged to.

## Verification

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```

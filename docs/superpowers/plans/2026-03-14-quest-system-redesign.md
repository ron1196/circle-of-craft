# Quest System Redesign Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Redesign the quest system to separate world-level from per-player state, add per-player reward claiming, per-player animal quests, and rename LKLevelData to LKWorldData.

**Architecture:** World-level quest state (stages, checked flags, world flags) lives in `LKWorldData` (SavedData). Per-player state (receivedQuestBook, hasSimba, portal coords, claimed rewards) uses a Forge Capability on the player. Animal quests become per-player via UUID-keyed map on entity. Rewards are claimed through NPC interaction (one per click), not given automatically on quest advance.

**Tech Stack:** NeoForge 1.20.1, Java 17, Mojang mappings, SimpleChannel networking

**Spec:** `docs/superpowers/specs/2026-03-14-quest-system-redesign.md`

---

## File Structure

### New Files
| File | Responsibility |
|------|---------------|
| `quest/ClaimableReward.java` | Record: item supplier, count, reward key |
| `quest/AnimalQuestEntry.java` | Record: required item, required amount for per-player animal quests |
| `data/LKWorldData.java` | Renamed from LKLevelData. World-level SavedData |
| `data/LKPlayerData.java` | Per-player capability class |
| `data/LKPlayerDataProvider.java` | Capability provider, attach event, clone handler |
| `network/PlayerDataSyncPacket.java` | Syncs per-player data to client |

### Modified Files
| File | Changes |
|------|---------|
| `quest/LKQuestStage.java` | Remove ItemReward, remove rewards field |
| `quest/LKQuest.java` | Add claimableRewards map and builder method |
| `quest/LKQuestRegistry.java` | Move rewards to claimableReward() calls |
| `quest/LKQuestManager.java` | Remove reward giving, handle null triggers, update owner type |
| `quest/LKAnimalQuest.java` | Convert to static utility, remove instance state |
| `entity/animal/LKAnimal.java` | Replace LKAnimalQuest with Map<UUID, AnimalQuestEntry> |
| `entity/npc/RafikiEntity.java` | Add reward claiming, use capability for quest book |
| `entity/npc/ScarEntity.java` | Use LKWorldData |
| `entity/npc/ZiraEntity.java` | Add reward claiming, use LKWorldData |
| `item/RafikiDustItem.java` | Check hasSimba from capability |
| `item/QuestBookItem.java` | Read from client state |
| `event/LKForgeEvents.java` | Register capability, handle clone, use LKWorldData |
| `network/LKNetworking.java` | Register PlayerDataSyncPacket, bump protocol to "3" |
| `network/LoginSyncPacket.java` | Include player data, remove world portal coords |
| `network/QuestCheckPacket.java` | Use LKWorldData |
| `network/ClientWorldState.java` | Add player data fields |
| `client/gui/QuestBookScreen.java` | Adapt to new data sources |
| `TheLionKingMod.java` | No changes needed — `@AutoRegisterCapability` and `@EventBusSubscriber` handle registration |

### Deleted Files
| File | Reason |
|------|--------|
| `data/LKLevelData.java` | Replaced by LKWorldData |

---

## Chunk 1: Core Data Layer

### Task 1: Create ClaimableReward record

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/quest/ClaimableReward.java`

- [ ] **Step 1: Create the record**

```java
package io.github.ron1196.thelionking.quest;

import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public record ClaimableReward(Supplier<Item> item, int count, String rewardKey) {
}
```

- [ ] **Step 2: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/ClaimableReward.java
git commit -m "feat(quest): add ClaimableReward record for per-player rewards"
```

### Task 2: Simplify LKQuestStage — remove ItemReward

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/quest/LKQuestStage.java`

- [ ] **Step 1: Remove ItemReward and rewards from LKQuestStage**

Replace the entire file content with:

```java
package io.github.ron1196.thelionking.quest;

import net.minecraft.world.item.Item;
import java.util.List;
import java.util.function.Supplier;

public record LKQuestStage(
        String objectiveText,
        List<ItemRequirement> requirements
) {
    public LKQuestStage(String objectiveText) {
        this(objectiveText, List.of());
    }

    public enum Source {
        MAIN_HAND,
        INVENTORY
    }

    public record ItemRequirement(Supplier<Item> item, int count, Source source) {
        public ItemRequirement(Supplier<Item> item, int count) {
            this(item, count, Source.MAIN_HAND);
        }
    }
}
```

- [ ] **Step 2: Compile — expect failures in LKQuestRegistry (references to ItemReward)**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava 2>&1 | tail -20
```
Expected: Compile errors in LKQuestRegistry.java (ItemReward references)

- [ ] **Step 3: Commit (partial — will fix LKQuestRegistry in next task)**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/LKQuestStage.java
git commit -m "refactor(quest): remove ItemReward from LKQuestStage"
```

### Task 3: Update LKQuest — add claimableRewards

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/quest/LKQuest.java`

- [ ] **Step 1: Add claimableRewards field and builder method**

Add to the `LKQuest` class fields (after `customTransitions`):

```java
private final Map<Integer, List<ClaimableReward>> claimableRewards;
```

Add to constructor (after customTransitions assignment):

```java
this.claimableRewards = Map.copyOf(builder.claimableRewards);
```

Add getter:

```java
public List<ClaimableReward> getClaimableRewards(int stage) {
    return claimableRewards.get(stage);
}
```

Add to Builder class fields:

```java
private final Map<Integer, List<ClaimableReward>> claimableRewards = new HashMap<>();
```

Add to Builder methods:

```java
public Builder claimableReward(int stageIndex, ClaimableReward reward) {
    this.claimableRewards.computeIfAbsent(stageIndex, k -> new ArrayList<>()).add(reward);
    return this;
}
```

Add `ArrayList` to the imports.

- [ ] **Step 2: Compile — may still fail due to LKQuestRegistry**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava 2>&1 | tail -20
```

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/LKQuest.java
git commit -m "feat(quest): add claimableRewards to LKQuest builder"
```

### Task 4: Update LKQuestRegistry — move rewards to claimableReward()

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/quest/LKQuestRegistry.java`

- [ ] **Step 1: Update buildRafikiQuest() — remove ItemReward from stages, add claimableReward()**

In `buildRafikiQuest()`, change the stage 1 definition from:

```java
.stage(new LKQuestStage(
        "Bring Rafiki 64 hyena bones",
        List.of(new ItemRequirement(() -> LKItems.HYENA_BONE.get(), 64)),
        List.of(new ItemReward(() -> LKItems.RHYTHM_STAFF.get(), 1))))
```

To:

```java
.stage(new LKQuestStage(
        "Bring Rafiki 64 hyena bones",
        List.of(new ItemRequirement(() -> LKItems.HYENA_BONE.get(), 64))))
```

Remove all `ItemReward` and empty `List.of()` third arguments from other stages. Add after the last `.trigger()` call:

```java
.claimableReward(1, new ClaimableReward(() -> LKItems.RHYTHM_STAFF.get(), 1, "rafiki:1"))
```

Remove the `LKQuestStage.*` static import for `ItemReward` if present. Add import for `ClaimableReward`.

- [ ] **Step 2: Update buildOutlandsQuest() — same pattern**

Remove all `ItemReward` third arguments from stages. The Outlands quest currently has no item rewards in its stage definitions, so this is just removing empty `List.of()` third arguments from the 3-arg LKQuestStage constructor calls. Update them to use the 2-arg or 1-arg constructors as appropriate.

- [ ] **Step 3: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/LKQuestRegistry.java
git commit -m "refactor(quest): move rewards from stages to claimableRewards"
```

### Task 5: Update LKQuestManager — remove reward giving, handle null triggers

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/quest/LKQuestManager.java`

- [ ] **Step 1: Update tryAdvance() — handle null triggers, remove reward giving**

In `tryAdvance()`, after the `if (currentStage >= quest.getNumStages()) return false;` check, change:

```java
LKQuestTrigger expected = quest.getTriggerForStage(currentStage);
if (expected != trigger) return false;
```

To:

```java
LKQuestTrigger expected = quest.getTriggerForStage(currentStage);
if (expected == null || expected != trigger) return false;
```

Remove the `giveRewards()` call from `tryAdvance()`. Remove the `giveRewards()` method entirely. Remove the `LKQuestStage.ItemReward` import if present.

- [ ] **Step 2: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/LKQuestManager.java
git commit -m "refactor(quest): remove reward giving from tryAdvance, handle null triggers"
```

### Task 6: Create AnimalQuestEntry record

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/quest/AnimalQuestEntry.java`

- [ ] **Step 1: Create the record**

```java
package io.github.ron1196.thelionking.quest;

import net.minecraft.world.item.Item;

public record AnimalQuestEntry(Item requiredItem, int requiredAmount) {
}
```

- [ ] **Step 2: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/AnimalQuestEntry.java
git commit -m "feat(quest): add AnimalQuestEntry record for per-player animal quests"
```

---

## Chunk 2: Player Capability & World Data Rename

### Task 7: Create LKPlayerData capability class

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/data/LKPlayerData.java`

- [ ] **Step 1: Create the capability class**

```java
package io.github.ron1196.thelionking.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.HashSet;
import java.util.Set;

@AutoRegisterCapability
public class LKPlayerData {

    private boolean receivedQuestBook;
    private int homePortalX;
    private int homePortalY;
    private int homePortalZ;
    private boolean hasSimba;
    private final Set<String> claimedRewards = new HashSet<>();

    public boolean hasReceivedQuestBook() {
        return receivedQuestBook;
    }

    public void setReceivedQuestBook(boolean received) {
        this.receivedQuestBook = received;
    }

    public int getHomePortalX() { return homePortalX; }
    public int getHomePortalY() { return homePortalY; }
    public int getHomePortalZ() { return homePortalZ; }

    public void setHomePortal(int x, int y, int z) {
        this.homePortalX = x;
        this.homePortalY = y;
        this.homePortalZ = z;
    }

    public boolean hasSimba() {
        return hasSimba;
    }

    public void setHasSimba(boolean hasSimba) {
        this.hasSimba = hasSimba;
    }

    public boolean hasClaimedReward(String rewardKey) {
        return claimedRewards.contains(rewardKey);
    }

    public void claimReward(String rewardKey) {
        claimedRewards.add(rewardKey);
    }

    public Set<String> getClaimedRewards() {
        return claimedRewards;
    }

    public void copyFrom(LKPlayerData other) {
        this.receivedQuestBook = other.receivedQuestBook;
        this.homePortalX = other.homePortalX;
        this.homePortalY = other.homePortalY;
        this.homePortalZ = other.homePortalZ;
        this.hasSimba = other.hasSimba;
        this.claimedRewards.clear();
        this.claimedRewards.addAll(other.claimedRewards);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("ReceivedQuestBook", receivedQuestBook);
        tag.putInt("HomePortalX", homePortalX);
        tag.putInt("HomePortalY", homePortalY);
        tag.putInt("HomePortalZ", homePortalZ);
        tag.putBoolean("HasSimba", hasSimba);
        ListTag rewardsList = new ListTag();
        for (String key : claimedRewards) {
            rewardsList.add(StringTag.valueOf(key));
        }
        tag.put("ClaimedRewards", rewardsList);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        receivedQuestBook = tag.getBoolean("ReceivedQuestBook");
        homePortalX = tag.getInt("HomePortalX");
        homePortalY = tag.getInt("HomePortalY");
        homePortalZ = tag.getInt("HomePortalZ");
        hasSimba = tag.getBoolean("HasSimba");
        claimedRewards.clear();
        ListTag rewardsList = tag.getList("ClaimedRewards", Tag.TAG_STRING);
        for (int i = 0; i < rewardsList.size(); i++) {
            claimedRewards.add(rewardsList.getString(i));
        }
    }
}
```

- [ ] **Step 2: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/data/LKPlayerData.java
git commit -m "feat(data): add LKPlayerData capability class"
```

### Task 8: Create LKPlayerDataProvider

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/data/LKPlayerDataProvider.java`

- [ ] **Step 1: Create the capability provider**

```java
package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LKPlayerDataProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation IDENTIFIER =
            new ResourceLocation(TheLionKingMod.MOD_ID, "player_data");

    public static final Capability<LKPlayerData> CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final LKPlayerData data = new LKPlayerData();
    private final LazyOptional<LKPlayerData> optional = LazyOptional.of(() -> data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        data.deserializeNBT(tag);
    }

    public static LKPlayerData get(Player player) {
        return player.getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalStateException("LKPlayerData capability missing on player"));
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(IDENTIFIER, new LKPlayerDataProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        LKPlayerData oldData = get(event.getOriginal());
        LKPlayerData newData = get(event.getEntity());
        newData.copyFrom(oldData);
        event.getOriginal().invalidateCaps();
    }
}
```

- [ ] **Step 2: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/data/LKPlayerDataProvider.java
git commit -m "feat(data): add LKPlayerDataProvider with attach and clone events"
```

### Task 9: Rename LKLevelData → LKWorldData

**Files:**
- Delete: `src/main/java/io/github/ron1196/thelionking/data/LKLevelData.java`
- Create: `src/main/java/io/github/ron1196/thelionking/data/LKWorldData.java`
- Modify: All files that import `LKLevelData`

- [ ] **Step 1: Create LKWorldData.java**

Copy current `LKLevelData.java` content, rename class to `LKWorldData`. Remove `receivedQuestBook`, `homePortalX/Y/Z`, `simbas` map, and `hasSimba()` method. Keep `defeatedScar`, `ziraStage`, `pumbaaStage`, `outlandersHostile`, and `questManager`.

```java
package io.github.ron1196.thelionking.data;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.quest.LKQuestManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class LKWorldData extends SavedData {

    private static final String DATA_NAME = TheLionKingMod.MOD_ID + "_data";

    public int ziraStage = 0;
    public int pumbaaStage = 0;
    public boolean outlandersHostile = false;
    public boolean defeatedScar = false;

    private final LKQuestManager questManager = new LKQuestManager(this);

    public LKWorldData() {
    }

    public LKQuestManager getQuestManager() {
        return questManager;
    }

    public static LKWorldData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(LKWorldData::load, LKWorldData::new, DATA_NAME);
    }

    public static LKWorldData load(CompoundTag tag) {
        LKWorldData data = new LKWorldData();
        data.ziraStage = tag.getInt("ZiraStage");
        data.pumbaaStage = tag.getInt("PumbaaStage");
        data.outlandersHostile = tag.getBoolean("OutlandersHostile");
        data.defeatedScar = tag.getBoolean("DefeatedScar");

        // Load quests (supports legacy migration)
        data.questManager.readFromNBT(tag);

        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        tag.putInt("ZiraStage", ziraStage);
        tag.putInt("PumbaaStage", pumbaaStage);
        tag.putBoolean("OutlandersHostile", outlandersHostile);
        tag.putBoolean("DefeatedScar", defeatedScar);

        // Save quests
        questManager.writeToNBT(tag);

        return tag;
    }
}
```

- [ ] **Step 2: Delete LKLevelData.java**

```bash
rm src/main/java/io/github/ron1196/thelionking/data/LKLevelData.java
```

- [ ] **Step 3: Find and replace all LKLevelData references**

Search for all files referencing `LKLevelData` and update imports and usages to `LKWorldData`. Key files:
- `quest/LKQuestManager.java` — no direct reference (uses `SavedData` owner)
- `entity/npc/RafikiEntity.java` — `LKLevelData.get()` → `LKWorldData.get()`
- `entity/npc/ScarEntity.java` — same
- `entity/npc/ZiraEntity.java` — same
- `item/RafikiDustItem.java` — same
- `event/LKForgeEvents.java` — same
- `network/LoginSyncPacket.java` — constructor param type
- `network/QuestCheckPacket.java` — `LKLevelData.get()` → `LKWorldData.get()`

In each file: replace `import ...LKLevelData` with `import ...LKWorldData`, replace `LKLevelData` with `LKWorldData` in code.

- [ ] **Step 4: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "refactor(data): rename LKLevelData to LKWorldData, remove per-player fields"
```

---

## Chunk 3: Networking Updates

### Task 10: Create PlayerDataSyncPacket

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/network/PlayerDataSyncPacket.java`

- [ ] **Step 1: Create the packet**

```java
package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.PlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PlayerDataSyncPacket {

    private final boolean receivedQuestBook;
    private final int homePortalX;
    private final int homePortalY;
    private final int homePortalZ;
    private final boolean hasSimba;
    private final Set<String> claimedRewards;

    public PlayerDataSyncPacket(LKPlayerData data) {
        this.receivedQuestBook = data.hasReceivedQuestBook();
        this.homePortalX = data.getHomePortalX();
        this.homePortalY = data.getHomePortalY();
        this.homePortalZ = data.getHomePortalZ();
        this.hasSimba = data.hasSimba();
        this.claimedRewards = new HashSet<>(data.getClaimedRewards());
    }

    public PlayerDataSyncPacket(FriendlyByteBuf buf) {
        this.receivedQuestBook = buf.readBoolean();
        this.homePortalX = buf.readInt();
        this.homePortalY = buf.readInt();
        this.homePortalZ = buf.readInt();
        this.hasSimba = buf.readBoolean();
        int count = buf.readVarInt();
        this.claimedRewards = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            claimedRewards.add(buf.readUtf());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(receivedQuestBook);
        buf.writeInt(homePortalX);
        buf.writeInt(homePortalY);
        buf.writeInt(homePortalZ);
        buf.writeBoolean(hasSimba);
        buf.writeVarInt(claimedRewards.size());
        for (String key : claimedRewards) {
            buf.writeUtf(key);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ClientWorldState.receivedQuestBook = receivedQuestBook;
            ClientWorldState.playerHomePortalX = homePortalX;
            ClientWorldState.playerHomePortalY = homePortalY;
            ClientWorldState.playerHomePortalZ = homePortalZ;
            ClientWorldState.hasSimba = hasSimba;
            ClientWorldState.claimedRewards.clear();
            ClientWorldState.claimedRewards.addAll(claimedRewards);
        });
        context.setPacketHandled(true);
    }
}
```

- [ ] **Step 2: Compile — expect failure (ClientWorldState fields don't exist yet)**

This is expected. We'll fix ClientWorldState in the next task.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/network/PlayerDataSyncPacket.java
git commit -m "feat(network): add PlayerDataSyncPacket for per-player data sync"
```

### Task 11: Update ClientWorldState — add player data fields

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/network/ClientWorldState.java`

- [ ] **Step 1: Add per-player fields**

Replace entire file with:

```java
package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.quest.LKQuestState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientWorldState {
    // World state
    public static boolean defeatedScar;
    public static int ziraStage;
    public static int pumbaaStage;
    public static boolean outlandersHostile;

    // Quest state (world-level)
    public static final Map<String, LKQuestState> questStates = new HashMap<>();

    // Player state (local client only)
    public static boolean receivedQuestBook;
    public static int playerHomePortalX;
    public static int playerHomePortalY;
    public static int playerHomePortalZ;
    public static boolean hasSimba;
    public static final Set<String> claimedRewards = new HashSet<>();

    public static int getQuestStage(String questId) {
        LKQuestState state = questStates.get(questId);
        return state != null ? state.getCurrentStage() : 0;
    }

    public static boolean isQuestChecked(String questId) {
        LKQuestState state = questStates.get(questId);
        return state != null && state.isChecked();
    }

    public static void reset() {
        defeatedScar = false;
        ziraStage = 0;
        pumbaaStage = 0;
        outlandersHostile = false;
        questStates.clear();
        receivedQuestBook = false;
        playerHomePortalX = 0;
        playerHomePortalY = 0;
        playerHomePortalZ = 0;
        hasSimba = false;
        claimedRewards.clear();
    }
}
```

- [ ] **Step 2: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL (or minor errors from LoginSyncPacket referencing old fields — fix in next task)

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/network/ClientWorldState.java
git commit -m "refactor(network): add per-player fields to ClientWorldState"
```

### Task 12: Update LoginSyncPacket — include player data, remove world portal

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/network/LoginSyncPacket.java`

- [ ] **Step 1: Rewrite LoginSyncPacket**

Replace entire file. The packet now takes `LKWorldData` and `LKPlayerData` as constructor arguments. Encoding order: world flags, quest states, player data.

```java
package io.github.ron1196.thelionking.network;

import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.LKQuest;
import io.github.ron1196.thelionking.quest.LKQuestRegistry;
import io.github.ron1196.thelionking.quest.LKQuestState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class LoginSyncPacket {

    // World state
    private final boolean defeatedScar;
    private final int ziraStage;
    private final int pumbaaStage;
    private final boolean outlandersHostile;

    // Quest data
    private final List<QuestEntry> questEntries;

    // Player data
    private final boolean receivedQuestBook;
    private final int homePortalX;
    private final int homePortalY;
    private final int homePortalZ;
    private final boolean hasSimba;
    private final Set<String> claimedRewards;

    private record QuestEntry(String questId, int stage, boolean checked) {}

    public LoginSyncPacket(LKWorldData worldData, LKPlayerData playerData) {
        this.defeatedScar = worldData.defeatedScar;
        this.ziraStage = worldData.ziraStage;
        this.pumbaaStage = worldData.pumbaaStage;
        this.outlandersHostile = worldData.outlandersHostile;

        this.questEntries = new ArrayList<>();
        for (LKQuest quest : LKQuestRegistry.getOrdered()) {
            LKQuestState state = worldData.getQuestManager().getState(quest.getId());
            questEntries.add(new QuestEntry(quest.getId(), state.getCurrentStage(), state.isChecked()));
        }

        this.receivedQuestBook = playerData.hasReceivedQuestBook();
        this.homePortalX = playerData.getHomePortalX();
        this.homePortalY = playerData.getHomePortalY();
        this.homePortalZ = playerData.getHomePortalZ();
        this.hasSimba = playerData.hasSimba();
        this.claimedRewards = new HashSet<>(playerData.getClaimedRewards());
    }

    public LoginSyncPacket(FriendlyByteBuf buf) {
        this.defeatedScar = buf.readBoolean();
        this.ziraStage = buf.readVarInt();
        this.pumbaaStage = buf.readVarInt();
        this.outlandersHostile = buf.readBoolean();

        int questCount = buf.readVarInt();
        this.questEntries = new ArrayList<>(questCount);
        for (int i = 0; i < questCount; i++) {
            questEntries.add(new QuestEntry(buf.readUtf(), buf.readVarInt(), buf.readBoolean()));
        }

        this.receivedQuestBook = buf.readBoolean();
        this.homePortalX = buf.readInt();
        this.homePortalY = buf.readInt();
        this.homePortalZ = buf.readInt();
        this.hasSimba = buf.readBoolean();
        int rewardCount = buf.readVarInt();
        this.claimedRewards = new HashSet<>(rewardCount);
        for (int i = 0; i < rewardCount; i++) {
            claimedRewards.add(buf.readUtf());
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(defeatedScar);
        buf.writeVarInt(ziraStage);
        buf.writeVarInt(pumbaaStage);
        buf.writeBoolean(outlandersHostile);

        buf.writeVarInt(questEntries.size());
        for (QuestEntry entry : questEntries) {
            buf.writeUtf(entry.questId());
            buf.writeVarInt(entry.stage());
            buf.writeBoolean(entry.checked());
        }

        buf.writeBoolean(receivedQuestBook);
        buf.writeInt(homePortalX);
        buf.writeInt(homePortalY);
        buf.writeInt(homePortalZ);
        buf.writeBoolean(hasSimba);
        buf.writeVarInt(claimedRewards.size());
        for (String key : claimedRewards) {
            buf.writeUtf(key);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // World state
            ClientWorldState.defeatedScar = defeatedScar;
            ClientWorldState.ziraStage = ziraStage;
            ClientWorldState.pumbaaStage = pumbaaStage;
            ClientWorldState.outlandersHostile = outlandersHostile;

            // Quest state
            ClientWorldState.questStates.clear();
            for (QuestEntry entry : questEntries) {
                ClientWorldState.questStates.put(entry.questId(),
                        new LKQuestState(entry.stage(), entry.checked()));
            }

            // Player state
            ClientWorldState.receivedQuestBook = receivedQuestBook;
            ClientWorldState.playerHomePortalX = homePortalX;
            ClientWorldState.playerHomePortalY = homePortalY;
            ClientWorldState.playerHomePortalZ = homePortalZ;
            ClientWorldState.hasSimba = hasSimba;
            ClientWorldState.claimedRewards.clear();
            ClientWorldState.claimedRewards.addAll(claimedRewards);
        });
        context.setPacketHandled(true);
    }
}
```

- [ ] **Step 2: Compile — expect error in LKForgeEvents (LoginSyncPacket constructor changed)**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava 2>&1 | tail -20
```
Expected: Error in LKForgeEvents.java — `LoginSyncPacket(LKWorldData)` no longer valid

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/network/LoginSyncPacket.java
git commit -m "refactor(network): rewrite LoginSyncPacket with player data"
```

### Task 13: Update LKNetworking — register PlayerDataSyncPacket, bump protocol

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/network/LKNetworking.java`

- [ ] **Step 1: Bump protocol version and add PlayerDataSyncPacket**

Change protocol version from `"2"` to `"3"`.

Add after the `LoginSyncPacket` registration block:

```java
CHANNEL.messageBuilder(PlayerDataSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
        .encoder(PlayerDataSyncPacket::encode)
        .decoder(PlayerDataSyncPacket::new)
        .consumerMainThread(PlayerDataSyncPacket::handle)
        .add();
```

- [ ] **Step 2: Compile — still expect error from LKForgeEvents**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava 2>&1 | tail -20
```

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/network/LKNetworking.java
git commit -m "feat(network): register PlayerDataSyncPacket, bump protocol to v3"
```

### Task 14: Update QuestCheckPacket — use LKWorldData

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/network/QuestCheckPacket.java`

- [ ] **Step 1: Replace LKLevelData with LKWorldData**

Update the import and the `handle()` method:

```java
import io.github.ron1196.thelionking.data.WorldData;
```

In `handle()`, change:
```java
LKLevelData data = LKLevelData.get(sender.serverLevel());
```
To:
```java
LKWorldData data = LKWorldData.get(sender.serverLevel());
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/network/QuestCheckPacket.java
git commit -m "refactor(network): update QuestCheckPacket to use LKWorldData"
```

---

## Chunk 4: Entity & Item Updates

### Task 15: Update LKForgeEvents — use LKWorldData, fix LoginSyncPacket

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/event/LKForgeEvents.java`

- [ ] **Step 1: Update imports and onPlayerLoggedIn**

Replace `LKLevelData` import with `LKWorldData`. In `onPlayerLoggedIn()`, update LoginSyncPacket constructor to pass both world data and player data:

```java
@SubscribeEvent
public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
        ServerLevel overworld = serverPlayer.server.overworld();
        LKWorldData worldData = LKWorldData.get(overworld);
        LKPlayerData playerData = LKPlayerDataProvider.get(serverPlayer);
        LKNetworking.CHANNEL.send(
                net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> serverPlayer),
                new LoginSyncPacket(worldData, playerData)
        );
    }
}
```

Add imports for `LKPlayerData`, `LKPlayerDataProvider`, `LoginSyncPacket`, `LKNetworking`.

- [ ] **Step 2: Update onLevelTick — use LKWorldData**

Replace `LKLevelData.get(serverLevel)` with `LKWorldData.get(serverLevel)`.

- [ ] **Step 3: Update handleZiraSpawnEvent — use LKWorldData**

Same replacement.

- [ ] **Step 4: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL (or errors in entity files — next tasks)

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/event/LKForgeEvents.java
git commit -m "refactor(events): use LKWorldData and pass player data in login sync"
```

### Task 16: Update RafikiEntity — reward claiming, capability for quest book

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/entity/npc/RafikiEntity.java`

- [ ] **Step 1: Rewrite mobInteract with reward claiming flow**

Replace imports: `LKLevelData` → `LKWorldData`, add imports for `LKPlayerData`, `LKPlayerDataProvider`, `LKQuestManager`, `ClaimableReward`, `LKQuest`.

Rewrite `mobInteract()`:

```java
@Override
protected InteractionResult mobInteract(Player player, InteractionHand hand) {
    if (level().isClientSide()) return InteractionResult.SUCCESS;
    if (talkCooldown > 0) return InteractionResult.SUCCESS;
    if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
    if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

    talkCooldown = 40;
    LKWorldData worldData = LKWorldData.get(serverLevel);
    LKQuestManager quests = worldData.getQuestManager();
    LKPlayerData playerData = LKPlayerDataProvider.get(serverPlayer);
    int stage = quests.getStage("rafiki");

    // Give quest book on first meeting
    if (!playerData.hasReceivedQuestBook()) {
        playerData.setReceivedQuestBook(true);
        player.addItem(new ItemStack(LKItems.QUEST_BOOK.get()));
        syncPlayerData(serverPlayer, playerData);
    }

    // Try claim next unclaimed reward
    int claimedStage = tryClaimNextReward(serverPlayer, playerData, quests);
    if (claimedStage >= 0) {
        sendStageDialogue(player, claimedStage + 1);
        syncPlayerData(serverPlayer, playerData);
        return InteractionResult.SUCCESS;
    }

    // Try to advance the quest
    if (quests.tryAdvance("rafiki", serverPlayer, LKQuestTrigger.RAFIKI_TALK)) {
        sendStageDialogue(player, quests.getStage("rafiki"));
        return InteractionResult.SUCCESS;
    }

    // Quest didn't advance — give contextual speech
    switch (stage) {
        case LKQuestRegistry.RAFIKI_COLLECT_BONES -> sendSpeech(player, LKCharacterSpeech.HYENA_BONES);
        case LKQuestRegistry.RAFIKI_DEFEAT_SCAR -> sendSpeech(player, LKCharacterSpeech.MENTION_SCAR);
        case LKQuestRegistry.RAFIKI_COLLECT_TERMITES -> sendSpeech(player, LKCharacterSpeech.TERMITES);
        case LKQuestRegistry.RAFIKI_COLLECT_MANGOES -> sendSpeech(player, LKCharacterSpeech.MANGOES);
        case LKQuestRegistry.RAFIKI_USE_STAR_ALTAR -> sendSpeech(player, LKCharacterSpeech.STAR_ALTAR);
        default -> {
            if (quests.isComplete("rafiki")) sendSpeech(player, LKCharacterSpeech.HINT);
        }
    }
    return InteractionResult.SUCCESS;
}
```

- [ ] **Step 2: Add helper methods**

```java
private int tryClaimNextReward(ServerPlayer player, LKPlayerData playerData, LKQuestManager quests) {
    LKQuest quest = LKQuestRegistry.get("rafiki");
    int currentStage = quests.getStage("rafiki");
    for (int stage = 0; stage < currentStage; stage++) {
        java.util.List<ClaimableReward> rewards = quest.getClaimableRewards(stage);
        if (rewards == null) continue;
        for (ClaimableReward reward : rewards) {
            if (!playerData.hasClaimedReward(reward.rewardKey())) {
                player.addItem(new ItemStack(reward.item().get(), reward.count()));
                playerData.claimReward(reward.rewardKey());
                return stage;
            }
        }
    }
    return -1;
}

private void syncPlayerData(ServerPlayer player, LKPlayerData data) {
    LKNetworking.CHANNEL.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
            new io.github.ron1196.thelionking.network.PlayerDataSyncPacket(data)
    );
}
```

- [ ] **Step 3: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```

- [ ] **Step 4: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/entity/npc/RafikiEntity.java
git commit -m "feat(npc): add per-player reward claiming to RafikiEntity"
```

### Task 17: Update ScarEntity — use LKWorldData

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/entity/npc/ScarEntity.java`

- [ ] **Step 1: Replace LKLevelData with LKWorldData**

Update import. In `die()`, change:

```java
LKLevelData data = LKLevelData.get(serverLevel);
data.getQuestManager().tryAdvance("rafiki", serverPlayer, LKQuestTrigger.SCAR_KILLED);
```

To:

```java
LKWorldData data = LKWorldData.get(serverLevel);
data.getQuestManager().tryAdvance("rafiki", serverPlayer, LKQuestTrigger.SCAR_KILLED);
```

- [ ] **Step 2: Compile and commit**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
git add src/main/java/io/github/ron1196/thelionking/entity/npc/ScarEntity.java
git commit -m "refactor(npc): update ScarEntity to use LKWorldData"
```

### Task 18: Update ZiraEntity — reward claiming, use LKWorldData

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/entity/npc/ZiraEntity.java`

- [ ] **Step 1: Update imports and mobInteract — same pattern as RafikiEntity**

Replace `LKLevelData` → `LKWorldData`. Add `LKPlayerData`, `LKPlayerDataProvider`, `ClaimableReward` imports.

Update `mobInteract()` to add reward claiming before quest advancement (same pattern as RafikiEntity but for "outlands" quest).

Update `die()` to use `LKWorldData.get()`.

- [ ] **Step 2: Add tryClaimNextReward and syncPlayerData helpers (same pattern as Rafiki but for "outlands")**

- [ ] **Step 3: Compile and commit**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
git add src/main/java/io/github/ron1196/thelionking/entity/npc/ZiraEntity.java
git commit -m "feat(npc): add per-player reward claiming to ZiraEntity"
```

### Task 19: Update RafikiDustItem — use capability for hasSimba

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/item/RafikiDustItem.java`

- [ ] **Step 1: Replace LKLevelData with LKWorldData, use capability for hasSimba**

Update imports. In `useOn()`, change:

```java
LKLevelData data = LKLevelData.get((ServerLevel) level);
if (data.hasSimba(player)) {
```

To:

```java
LKWorldData worldData = LKWorldData.get((ServerLevel) level);
LKPlayerData playerData = LKPlayerDataProvider.get(player);
if (playerData.hasSimba()) {
```

Update the quest advance call:

```java
if (player instanceof ServerPlayer serverPlayer) {
    if (worldData.getQuestManager().tryAdvance("rafiki", serverPlayer, LKQuestTrigger.STAR_ALTAR_USED)) {
        broadcastMessage(level, "\u00a7e<Rafiki> \u00a7fYou see? He lives in you! Ohohoho!");
    }
}
```

- [ ] **Step 2: Compile and commit**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
git add src/main/java/io/github/ron1196/thelionking/item/RafikiDustItem.java
git commit -m "refactor(item): use LKPlayerData capability for hasSimba check"
```

---

## Chunk 5: Animal Quests & UI

### Task 20: Convert LKAnimalQuest to static utility

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/quest/LKAnimalQuest.java`

- [ ] **Step 1: Remove instance state, keep static utility methods**

Remove fields: `requiredItem`, `requiredAmount`, `hasQuest`. Remove methods: `setQuest()`, `hasQuest()`, `isRequiredItem()`, `getRequiredItem()`, `getRequiredAmount()`, `completeQuest()`, `save()`, `load()`.

Keep: `QUEST_START_PHRASES`, `QUEST_END_PHRASES`, `NUMBER_WORDS`, `RANDOM`, `getQuestStartMessage()` (make static, take item and amount as params), `getQuestEndMessage()` (make static, take animal name), `giveReward()` (already static).

Updated static methods:

```java
public static String getQuestStartMessage(String animalName, String itemName, int amount) {
    String phrase = QUEST_START_PHRASES[RANDOM.nextInt(QUEST_START_PHRASES.length)];
    String numberWord = amount >= 0 && amount < NUMBER_WORDS.length
            ? NUMBER_WORDS[amount] : String.valueOf(amount);
    phrase = phrase.replace("#", numberWord).replace("%", itemName);
    return "\u00a7e<" + animalName + "> \u00a7f" + phrase;
}

public static String getQuestEndMessage(String animalName) {
    String phrase = QUEST_END_PHRASES[RANDOM.nextInt(QUEST_END_PHRASES.length)];
    return "\u00a7e<" + animalName + "> \u00a7f" + phrase;
}
```

Remove unused imports (`CompoundTag`, etc.). Keep `ServerPlayer`, `Item`, `ItemStack`, `Items`, `Component`, `LKItems`, `Random`.

- [ ] **Step 2: Compile — expect errors in LKAnimal**

- [ ] **Step 3: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/quest/LKAnimalQuest.java
git commit -m "refactor(quest): convert LKAnimalQuest to static utility"
```

### Task 21: Update LKAnimal — per-player animal quests

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/entity/animal/LKAnimal.java`

- [ ] **Step 1: Replace single LKAnimalQuest with Map<UUID, AnimalQuestEntry>**

Replace:
```java
private final LKAnimalQuest animalQuest = new LKAnimalQuest();
```
With:
```java
private final Map<UUID, AnimalQuestEntry> animalQuests = new HashMap<>();
```

Add imports: `UUID`, `HashMap`, `Map`, `AnimalQuestEntry`.

Remove `getAnimalQuest()` method.

- [ ] **Step 2: Rewrite mobInteract()**

```java
@Override
public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
    if (level().isClientSide()) return InteractionResult.SUCCESS;
    if (!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;

    UUID playerId = player.getUUID();
    AnimalQuestEntry entry = animalQuests.get(playerId);
    ItemStack held = player.getItemInHand(hand);

    if (entry != null) {
        if (held.is(entry.requiredItem()) && held.getCount() >= entry.requiredAmount()) {
            held.shrink(entry.requiredAmount());
            LKAnimalQuest.giveReward(serverPlayer, getAnimalTypeKey());
            player.sendSystemMessage(Component.literal(
                    LKAnimalQuest.getQuestEndMessage(getAnimalDisplayName())));
            animalQuests.remove(playerId);
        } else {
            player.sendSystemMessage(Component.literal(
                    LKAnimalQuest.getQuestStartMessage(
                            getAnimalDisplayName(),
                            entry.requiredItem().getDescription().getString(),
                            entry.requiredAmount())));
        }
        return InteractionResult.SUCCESS;
    }

    if (QUEST_RANDOM.nextInt(3) == 0) {
        Item[] requestItems = getQuestRequestItems();
        if (requestItems != null && requestItems.length > 0) {
            Item item = requestItems[QUEST_RANDOM.nextInt(requestItems.length)];
            int amount = 1 + QUEST_RANDOM.nextInt(5);
            animalQuests.put(playerId, new AnimalQuestEntry(item, amount));
            player.sendSystemMessage(Component.literal(
                    LKAnimalQuest.getQuestStartMessage(
                            getAnimalDisplayName(),
                            item.getDescription().getString(),
                            amount)));
            return InteractionResult.SUCCESS;
        }
    }

    return super.mobInteract(player, hand);
}
```

Add `getAnimalTypeKey()` method (returns registry name suffix for reward lookup):

```java
protected String getAnimalTypeKey() {
    return net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(getType()).getPath();
}
```

Remove `assignRandomQuest()` and `getQuestReward()` methods (logic moved inline/to LKAnimalQuest).

- [ ] **Step 3: Rewrite save/load for per-player map**

```java
@Override
public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    CompoundTag questsTag = new CompoundTag();
    for (Map.Entry<UUID, AnimalQuestEntry> e : animalQuests.entrySet()) {
        CompoundTag entryTag = new CompoundTag();
        entryTag.putString("Item",
                net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(e.getValue().requiredItem()).toString());
        entryTag.putInt("Amount", e.getValue().requiredAmount());
        questsTag.put(e.getKey().toString(), entryTag);
    }
    tag.put("AnimalQuests", questsTag);
}

@Override
public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    animalQuests.clear();
    if (tag.contains("AnimalQuests")) {
        CompoundTag questsTag = tag.getCompound("AnimalQuests");
        for (String key : questsTag.getAllKeys()) {
            CompoundTag entryTag = questsTag.getCompound(key);
            net.minecraft.resources.ResourceLocation itemId =
                    new net.minecraft.resources.ResourceLocation(entryTag.getString("Item"));
            Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(itemId);
            if (item != null) {
                animalQuests.put(UUID.fromString(key), new AnimalQuestEntry(item, entryTag.getInt("Amount")));
            }
        }
    }
}
```

- [ ] **Step 4: Compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/entity/animal/LKAnimal.java
git commit -m "feat(entity): per-player animal quests with UUID-keyed map"
```

### Task 22: Update QuestBookScreen — adapt to new data sources

**Files:**
- Modify: `src/main/java/io/github/ron1196/thelionking/client/gui/QuestBookScreen.java`

- [ ] **Step 1: Update to use ClientWorldState for player data**

The screen already reads quest stages from `ClientWorldState`. Update the portal display (if shown) to use `ClientWorldState.playerHomePortalX/Y/Z`. The `canStartOnClient` method and quest rendering should work as-is since they read from `ClientWorldState.getQuestStage()`.

No major changes needed — verify the existing code compiles with the updated `ClientWorldState` (old `homePortalX` fields are gone, new `playerHomePortalX` fields exist).

If the screen references `homePortalX` directly anywhere, update to `playerHomePortalX`.

- [ ] **Step 2: Compile and commit**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
git add src/main/java/io/github/ron1196/thelionking/client/gui/QuestBookScreen.java
git commit -m "refactor(gui): adapt QuestBookScreen to updated ClientWorldState"
```

### Task 23: Final compilation and cleanup

- [ ] **Step 1: Full compile**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home && ./gradlew compileJava
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Search for any remaining LKLevelData references**

```bash
grep -r "LKLevelData" src/main/java/ --include="*.java"
```
Expected: No matches

- [ ] **Step 3: Search for any remaining LKQuestBase/LKQuestRafiki/LKQuestOutlands/LKQuests references**

```bash
grep -rE "LKQuestBase|LKQuestRafiki|LKQuestOutlands|import.*LKQuests\b" src/main/java/ --include="*.java"
```
Expected: No matches

- [ ] **Step 4: Verify all new files exist**

```bash
ls -la src/main/java/io/github/ron1196/thelionking/quest/ClaimableReward.java \
       src/main/java/io/github/ron1196/thelionking/quest/AnimalQuestEntry.java \
       src/main/java/io/github/ron1196/thelionking/data/LKWorldData.java \
       src/main/java/io/github/ron1196/thelionking/data/LKPlayerData.java \
       src/main/java/io/github/ron1196/thelionking/data/LKPlayerDataProvider.java \
       src/main/java/io/github/ron1196/thelionking/network/PlayerDataSyncPacket.java
```
Expected: All 6 files exist

- [ ] **Step 5: Verify LKLevelData.java is deleted**

```bash
ls src/main/java/io/github/ron1196/thelionking/data/LKLevelData.java 2>&1
```
Expected: "No such file or directory"

- [ ] **Step 6: Commit any remaining changes**

```bash
git add -A
git status
```
If changes exist, commit with: `"chore: final cleanup for quest system redesign"`

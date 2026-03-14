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

    // ── Getters ─────────────────────────────────────────────────────────────────

    public boolean hasReceivedQuestBook() {
        return receivedQuestBook;
    }

    public int getHomePortalX() {
        return homePortalX;
    }

    public int getHomePortalY() {
        return homePortalY;
    }

    public int getHomePortalZ() {
        return homePortalZ;
    }

    public boolean hasSimba() {
        return hasSimba;
    }

    public Set<String> getClaimedRewards() {
        return claimedRewards;
    }

    // ── Setters ─────────────────────────────────────────────────────────────────

    public void setReceivedQuestBook(boolean receivedQuestBook) {
        this.receivedQuestBook = receivedQuestBook;
    }

    public void setHomePortalX(int homePortalX) {
        this.homePortalX = homePortalX;
    }

    public void setHomePortalY(int homePortalY) {
        this.homePortalY = homePortalY;
    }

    public void setHomePortalZ(int homePortalZ) {
        this.homePortalZ = homePortalZ;
    }

    public void setHasSimba(boolean hasSimba) {
        this.hasSimba = hasSimba;
    }

    // ── Copy ────────────────────────────────────────────────────────────────────

    public void copyFrom(LKPlayerData other) {
        this.receivedQuestBook = other.receivedQuestBook;
        this.homePortalX = other.homePortalX;
        this.homePortalY = other.homePortalY;
        this.homePortalZ = other.homePortalZ;
        this.hasSimba = other.hasSimba;
        this.claimedRewards.clear();
        this.claimedRewards.addAll(other.claimedRewards);
    }

    // ── NBT ─────────────────────────────────────────────────────────────────────

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("ReceivedQuestBook", receivedQuestBook);
        tag.putInt("HomePortalX", homePortalX);
        tag.putInt("HomePortalY", homePortalY);
        tag.putInt("HomePortalZ", homePortalZ);
        tag.putBoolean("HasSimba", hasSimba);

        ListTag rewardsList = new ListTag();
        for (String reward : claimedRewards) {
            rewardsList.add(StringTag.valueOf(reward));
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

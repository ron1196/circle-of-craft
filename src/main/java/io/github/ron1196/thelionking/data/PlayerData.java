package io.github.ron1196.thelionking.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.HashSet;
import java.util.Set;

@AutoRegisterCapability
public class PlayerData {

    private boolean receivedQuestBook;
    private int homePortalX;
    private int homePortalY;
    private int homePortalZ;
    private boolean hasSimba;
    private boolean enteredPrideLands;
    private boolean enteredOutlands;
    private boolean enteredUpendi;
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

    public boolean hasEnteredPrideLands() {
        return enteredPrideLands;
    }

    public boolean hasEnteredOutlands() {
        return enteredOutlands;
    }

    public boolean hasEnteredUpendi() {
        return enteredUpendi;
    }

    public Set<String> getClaimedRewards() {
        return claimedRewards;
    }

    public boolean hasClaimedReward(String rewardKey) {
        return claimedRewards.contains(rewardKey);
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

    public void setEnteredPrideLands(boolean entered) {
        this.enteredPrideLands = entered;
    }

    public void setEnteredOutlands(boolean entered) {
        this.enteredOutlands = entered;
    }

    public void setEnteredUpendi(boolean entered) {
        this.enteredUpendi = entered;
    }

    public void claimReward(String rewardKey) {
        claimedRewards.add(rewardKey);
    }

    // ── Copy ────────────────────────────────────────────────────────────────────

    public void copyFrom(PlayerData other) {
        this.receivedQuestBook = other.receivedQuestBook;
        this.homePortalX = other.homePortalX;
        this.homePortalY = other.homePortalY;
        this.homePortalZ = other.homePortalZ;
        this.hasSimba = other.hasSimba;
        this.enteredPrideLands = other.enteredPrideLands;
        this.enteredOutlands = other.enteredOutlands;
        this.enteredUpendi = other.enteredUpendi;
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
        tag.putBoolean("EnteredPrideLands", enteredPrideLands);
        tag.putBoolean("EnteredOutlands", enteredOutlands);
        tag.putBoolean("EnteredUpendi", enteredUpendi);
        serializeClaimedRewards(tag);
        return tag;
    }

    private void serializeClaimedRewards(CompoundTag tag) {
        ListTag rewardsList = new ListTag();
        for (String reward : claimedRewards) {
            rewardsList.add(StringTag.valueOf(reward));
        }
        tag.put("ClaimedRewards", rewardsList);
    }

    public void deserializeNBT(CompoundTag tag) {
        receivedQuestBook = tag.getBoolean("ReceivedQuestBook");
        homePortalX = tag.getInt("HomePortalX");
        homePortalY = tag.getInt("HomePortalY");
        homePortalZ = tag.getInt("HomePortalZ");
        hasSimba = tag.getBoolean("HasSimba");
        enteredPrideLands = tag.getBoolean("EnteredPrideLands");
        enteredOutlands = tag.getBoolean("EnteredOutlands");
        enteredUpendi = tag.getBoolean("EnteredUpendi");
        deserializeClaimedRewards(tag);
    }

    private void deserializeClaimedRewards(CompoundTag tag) {
        claimedRewards.clear();
        ListTag rewardsList = tag.getList("ClaimedRewards", Tag.TAG_STRING);
        for (int i = 0; i < rewardsList.size(); i++) {
            claimedRewards.add(rewardsList.getString(i));
        }
    }
}

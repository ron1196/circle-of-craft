package io.github.ron1196.circleofcraft.data;

import com.mojang.serialization.Codec;
import io.github.ron1196.circleofcraft.registry.ModAttachments;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;

public class PlayerData implements ValueIOSerializable {

    public static PlayerData get(Player player) {
        return player.getData(ModAttachments.PLAYER_DATA.get());
    }

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

    // ── ValueIO ─────────────────────────────────────────────────────────────────

    @Override
    public void serialize(@NotNull ValueOutput output) {
        output.putBoolean("ReceivedQuestBook", receivedQuestBook);
        output.putInt("HomePortalX", homePortalX);
        output.putInt("HomePortalY", homePortalY);
        output.putInt("HomePortalZ", homePortalZ);
        output.putBoolean("HasSimba", hasSimba);
        output.putBoolean("EnteredPrideLands", enteredPrideLands);
        output.putBoolean("EnteredOutlands", enteredOutlands);
        output.putBoolean("EnteredUpendi", enteredUpendi);
        ValueOutput.TypedOutputList<String> rewardsList = output.list("ClaimedRewards", Codec.STRING);
        for (String reward : claimedRewards) {
            rewardsList.add(reward);
        }
    }

    @Override
    public void deserialize(@NotNull ValueInput input) {
        receivedQuestBook = input.getBooleanOr("ReceivedQuestBook", false);
        homePortalX = input.getIntOr("HomePortalX", 0);
        homePortalY = input.getIntOr("HomePortalY", 0);
        homePortalZ = input.getIntOr("HomePortalZ", 0);
        hasSimba = input.getBooleanOr("HasSimba", false);
        enteredPrideLands = input.getBooleanOr("EnteredPrideLands", false);
        enteredOutlands = input.getBooleanOr("EnteredOutlands", false);
        enteredUpendi = input.getBooleanOr("EnteredUpendi", false);
        claimedRewards.clear();
        for (String reward : input.listOrEmpty("ClaimedRewards", Codec.STRING)) {
            claimedRewards.add(reward);
        }
    }
}

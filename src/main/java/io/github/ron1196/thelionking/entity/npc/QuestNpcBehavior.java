package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Composition component for quest NPC tick mechanics: home position leash with teleport, periodic
 * quest state fallback, talk cooldown, and homePos NBT serialization. Shared by NPCs with different
 * superclasses (PathfinderMob, Monster) that cannot share a base class.
 */
public final class QuestNpcBehavior {

    @FunctionalInterface
    public interface QuestTickCallback {
        /**
         * Called every {@code QUEST_CHECK_INTERVAL} ticks on the server. Perform entity-specific
         * quest state checks (e.g., ensureWorldState).
         *
         * @return true if the owning entity was discarded (caller should bail out of tick)
         */
        boolean onQuestCheck(@NotNull ServerLevel level, @NotNull QuestlineManager quests);
    }

    private static final int LEASH_CHECK_INTERVAL = 100;
    private static final int QUEST_CHECK_INTERVAL = 100;

    private final Mob owner;
    private final int maxWanderDistance;

    @Nullable
    private final QuestTickCallback questTickCallback;

    private int talkCooldown = 0;
    private BlockPos homePos = null;
    private int leashCheckTimer = 0;
    private int questCheckTimer = 0;

    public QuestNpcBehavior(@NotNull Mob owner, int maxWanderDistance, @Nullable QuestTickCallback questTickCallback) {
        this.owner = owner;
        this.maxWanderDistance = maxWanderDistance;
        this.questTickCallback = questTickCallback;
    }

    /**
     * Returns true if the NPC is on talk cooldown.
     */
    public boolean isOnCooldown() {
        return talkCooldown > 0;
    }

    /**
     * Starts a talk cooldown for the given number of ticks.
     */
    public void startCooldown(int ticks) {
        this.talkCooldown = ticks;
    }

    /**
     * Called from the entity's {@code tick()} method. Handles cooldown decrement, home position
     * leash teleport, and periodic quest state checks.
     *
     * @return true if the owning entity was discarded (caller should return immediately)
     */
    public boolean tick() {
        if (talkCooldown > 0) talkCooldown--;

        Level level = owner.level();
        if (level.isClientSide) return false;
        if (!(level instanceof ServerLevel serverLevel)) return false;

        // Initialize home position on first server tick
        if (homePos == null) {
            homePos = owner.blockPosition();
        } else if (++leashCheckTimer >= LEASH_CHECK_INTERVAL) {
            leashCheckTimer = 0;
            if (owner.blockPosition().distSqr(homePos) > (long) maxWanderDistance * maxWanderDistance) {
                owner.moveTo(
                        homePos.getX() + 0.5, homePos.getY(), homePos.getZ() + 0.5, owner.getYRot(), owner.getXRot());
            }
        }

        // Periodic quest state check
        if (questTickCallback != null && ++questCheckTimer >= QUEST_CHECK_INTERVAL) {
            questCheckTimer = 0;
            QuestlineManager quests = WorldData.get(serverLevel).getQuestManager();
            return questTickCallback.onQuestCheck(serverLevel, quests);
        }

        return false;
    }

    /**
     * Saves homePos to the entity's NBT data.
     */
    public void saveToNbt(@NotNull CompoundTag tag) {
        if (homePos != null) {
            tag.putInt("HomeX", homePos.getX());
            tag.putInt("HomeY", homePos.getY());
            tag.putInt("HomeZ", homePos.getZ());
        }
    }

    /**
     * Loads homePos from the entity's NBT data.
     */
    public void loadFromNbt(@NotNull CompoundTag tag) {
        if (tag.contains("HomeX")) {
            homePos = new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"));
        }
    }
}

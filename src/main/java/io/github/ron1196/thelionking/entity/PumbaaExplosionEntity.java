package io.github.ron1196.thelionking.entity;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.network.FlatulencePacket;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

/**
 * Transient invisible entity spawned at the Pumbaa Box position when it explodes.
 * Handles the timed flatulence explosion sequence over several seconds.
 * Does not persist across restarts — quest stage recovery handles that case.
 */
public class PumbaaExplosionEntity extends Entity {

    private static final int TOTAL_EXPLOSIONS = 10;
    private static final int DESPAWN_THRESHOLD = 3;
    private static final int TICK_CHANCE = 16;
    private static final double SPREAD = 5.0;
    private static final int GAS_CLOUD_PARTICLES = 8;

    private int explosionsRemaining = TOTAL_EXPLOSIONS;
    private boolean questAdvanced = false;

    public PumbaaExplosionEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setInvisible(true);
    }

    public PumbaaExplosionEntity(ServerLevel level, double x, double y, double z) {
        this(EntityTypes.PUMBAA_EXPLOSION.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel serverLevel)) return;

        if (explosionsRemaining <= 0) {
            discard();
            return;
        }

        if (serverLevel.players().isEmpty()) return;
        if (serverLevel.random.nextInt(TICK_CHANCE) != 0) return;

        spawnExplosionEffects(serverLevel);
        explosionsRemaining--;

        // At 3/4 mark, advance the quest if applicable
        if (!questAdvanced && explosionsRemaining <= DESPAWN_THRESHOLD) {
            tryAdvanceQuest(serverLevel);
        }
    }

    private void spawnExplosionEffects(ServerLevel level) {
        double x = getX() + level.random.nextGaussian() * SPREAD;
        double y = getY() + 2.0 + level.random.nextFloat() * 4;
        double z = getZ() + level.random.nextGaussian() * SPREAD;

        // Explosion particle
        level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 0);

        // Green overlay for all players in this level
        for (ServerPlayer sp : level.players()) {
            Networking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new FlatulencePacket());
        }

        // Green gas cloud
        for (int i = 0; i < GAS_CLOUD_PARTICLES; i++) {
            level.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x + level.random.nextGaussian() * 2,
                    y + level.random.nextFloat() * 2,
                    z + level.random.nextGaussian() * 2,
                    1,
                    0,
                    0.05,
                    0,
                    0.02);
        }

        // Fart sound
        level.playSound(
                null,
                BlockPos.containing(x, y, z),
                LionKingSoundEvents.FLATULENCE.get(),
                SoundSource.BLOCKS,
                4.0F,
                (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
    }

    private void tryAdvanceQuest(ServerLevel level) {
        WorldData data = WorldData.get(level);
        QuestlineManager qm = data.getQuestManager();
        OutlandsQuestline.Stage stage = qm.getStage(OutlandsQuestline.QUEST_ID, OutlandsQuestline.Stage.class);

        if (stage != OutlandsQuestline.Stage.PUMBAA_BOX_EXPLODING) return;
        if (level.players().isEmpty()) return;

        ServerPlayer player = level.players().get(0);
        qm.tryAdvance(OutlandsQuestline.QUEST_ID, player, QuestTrigger.EXPLOSIONS_DONE);
        questAdvanced = true;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        // No synched data needed
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        // Not saved
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        // Not saved
    }
}

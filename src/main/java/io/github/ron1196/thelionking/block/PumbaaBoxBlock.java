package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.PumbaaExplosionEntity;
import io.github.ron1196.thelionking.network.FlatulencePacket;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class PumbaaBoxBlock extends Block {

    public PumbaaBoxBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        if (level.isClientSide()) {
            spawnSmokeParticles(level, pos);
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

        ServerLevel serverLevel = (ServerLevel) level;
        QuestlineManager qm = WorldData.get(serverLevel.getServer().overworld()).getQuestManager();
        OutlandsQuestline.Stage stageKey = qm.getStage("outlands", OutlandsQuestline.Stage.class);

        if (stageKey == OutlandsQuestline.Stage.USE_PUMBAA_BOX) {
            explode(level, pos, serverPlayer, qm);
            return InteractionResult.CONSUME;
        }

        spawnSmokeParticles(level, pos);
        return InteractionResult.SUCCESS;
    }

    private void explode(
            @NotNull Level level, @NotNull BlockPos pos, @NotNull ServerPlayer player, @NotNull QuestlineManager qm) {
        level.removeBlock(pos, false);

        // First immediate explosion + green gas cloud
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    pos.getX() + 0.5,
                    pos.getY() + 2.0,
                    pos.getZ() + 0.5,
                    1,
                    0,
                    0,
                    0,
                    0);
            // Green gas cloud (campfire smoke particles in a burst)
            for (int i = 0; i < 40; i++) {
                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        pos.getX() + 0.5 + level.random.nextGaussian() * 3,
                        pos.getY() + 1.0 + level.random.nextFloat() * 4,
                        pos.getZ() + 0.5 + level.random.nextGaussian() * 3,
                        1,
                        0,
                        0.05,
                        0,
                        0.02);
            }
        }

        level.playSound(
                null,
                pos,
                LionKingSoundEvents.FLATULENCE.get(),
                SoundSource.BLOCKS,
                4.0F,
                (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);

        // Send green overlay to all nearby players
        if (level instanceof ServerLevel serverLevel) {
            for (ServerPlayer nearby : serverLevel.players()) {
                if (nearby.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 50 * 50) {
                    Networking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> nearby), new FlatulencePacket());
                }
            }
        }

        // Spawn transient explosion entity for the timed effect sequence
        if (level instanceof ServerLevel serverLevel) {
            PumbaaExplosionEntity explosion =
                    new PumbaaExplosionEntity(serverLevel, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5);
            serverLevel.addFreshEntity(explosion);
        }

        qm.tryAdvance("outlands", player, QuestTrigger.PUMBAA_BOX_USED);
        LionKingCriteriaTriggers.TRADE_PUMBAA.trigger(player);
    }

    private static void spawnSmokeParticles(@NotNull Level level, @NotNull BlockPos pos) {
        for (int i = 0; i < 8; i++) {
            double dx = level.random.nextGaussian() * 0.02;
            double dy = level.random.nextGaussian() * 0.02;
            double dz = level.random.nextGaussian() * 0.02;
            level.addParticle(
                    ParticleTypes.SMOKE,
                    pos.getX() + 0.5 + (level.random.nextFloat() - 0.5) * 1.5,
                    pos.getY() + 0.9 + level.random.nextFloat(),
                    pos.getZ() + 0.5 + (level.random.nextFloat() - 0.5) * 1.5,
                    dx,
                    dy,
                    dz);
        }
    }
}

package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.StageTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
            @NotNull BlockHitResult hit
    ) {
        if (level.isClientSide()) {
            spawnSmokeParticles(level, pos);
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

        ServerLevel serverLevel = (ServerLevel) level;
        QuestlineManager qm =
                WorldData.get(serverLevel.getServer().overworld()).getQuestManager();
        OutlandsQuestline.Stage stage =
                qm.getStage("outlands", OutlandsQuestline.Stage.class);

        if (stage == OutlandsQuestline.Stage.USE_PUMBAA_BOX) {
            explode(level, pos, serverPlayer, qm);
            return InteractionResult.CONSUME;
        }

        spawnSmokeParticles(level, pos);
        return InteractionResult.SUCCESS;
    }

    private void explode(
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull ServerPlayer player,
            @NotNull QuestlineManager qm
    ) {
        level.removeBlock(pos, false);
        level.addParticle(
                ParticleTypes.EXPLOSION_EMITTER,
                pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5,
                0.0, 0.0, 0.0);
        level.playSound(
                null, pos,
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,
                4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
        qm.tryAdvance("outlands", player, StageTrigger.PUMBAA_BOX_USED);
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
                    dx, dy, dz);
        }
    }
}

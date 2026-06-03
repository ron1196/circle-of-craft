package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.entity.animal.BugEntity;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallenLogBlock extends RotatedPillarBlock {

    private static final float BASE_CHANCE = 0.25F;
    private static final float FORTUNE_BONUS = 0.15F;

    public FallenLogBlock(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public void playerDestroy(
            @NotNull Level level,
            @NotNull Player player,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @Nullable BlockEntity blockEntity,
            @NotNull ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!(level instanceof ServerLevel serverLevel)) return;

        int fortune = EnchantmentHelper.getItemEnchantmentLevel(
                serverLevel
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.FORTUNE),
                tool);
        float chance = BASE_CHANCE + FORTUNE_BONUS * fortune;
        if (level.random.nextFloat() >= chance) return;

        BugEntity bug = EntityTypes.BUG.get().create(serverLevel);
        if (bug == null) return;
        bug.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360.0F, 0.0F);
        bug.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
        bug.startPanic();
        serverLevel.addFreshEntity(bug);
    }
}

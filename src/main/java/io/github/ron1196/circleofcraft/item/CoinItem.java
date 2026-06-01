package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.util.LevelHelper;
import io.github.ron1196.circleofcraft.world.structure.StructureSearch;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CoinItem extends Item {

    private static final int USE_DURATION_TICKS = 32;
    private static final int TELEPORT_AERIAL_Y_OFFSET = 1;

    private final ResourceKey<Level> targetDimension;
    private final String targetStructureId;

    public CoinItem(ResourceKey<Level> targetDimension, String targetStructureId, Properties properties) {
        super(properties);
        this.targetDimension = targetDimension;
        this.targetStructureId = targetStructureId;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.dimension() != targetDimension) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("The coin feels inert here."), true);
            }
            return InteractionResultHolder.fail(stack);
        }
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARMOR_EQUIP_GOLD,
                SoundSource.PLAYERS,
                0.4F,
                1.0F);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player) || !(level instanceof ServerLevel serverLevel)) {
            return stack;
        }

        BlockPos target = StructureSearch.findNearest(serverLevel, targetStructureId, player.blockPosition());
        if (target == null) {
            player.displayClientMessage(Component.literal("The coin's magic fades — no destination found."), true);
            return stack;
        }

        int x = target.getX();
        int z = target.getZ();
        int y = LevelHelper.surfaceY(serverLevel, x, z) + TELEPORT_AERIAL_Y_OFFSET;
        player.teleportTo(serverLevel, x + 0.5, y, z + 0.5, player.getYRot(), player.getXRot());
        serverLevel.playSound(null, x, y, z, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return USE_DURATION_TICKS;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }
}

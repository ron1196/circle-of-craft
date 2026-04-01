package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.entity.RugEntity;
import io.github.ron1196.thelionking.registry.LionKingSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class RugItem extends Item {

    private final int rugType;

    public RugItem(int rugType, Properties properties) {
        super(properties);
        this.rugType = rugType;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos abovePos = clickedPos.relative(context.getClickedFace());

        if (context.getPlayer() == null) {
            return InteractionResult.FAIL;
        }

        if (!context.getPlayer().mayUseItemAt(abovePos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.FAIL;
        }

        // Require solid surface below
        if (!level.getBlockState(clickedPos).isFaceSturdy(level, clickedPos, context.getClickedFace())) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            RugEntity rug = new RugEntity(level, rugType);
            float yaw = (context.getPlayer().getYRot() % 360.0F) + 180.0F;
            rug.absMoveTo(abovePos.getX() + 0.5D, abovePos.getY(), abovePos.getZ() + 0.5D, yaw, 0.0F);

            AABB aabb = rug.getBoundingBox();
            if (level.noCollision(rug, aabb) && !level.containsAnyLiquid(aabb)) {
                level.addFreshEntity(rug);
                level.playSound(
                        null,
                        rug,
                        LionKingSoundEvents.LION_AMBIENT.get(),
                        SoundSource.NEUTRAL,
                        1.0F,
                        (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
                context.getItemInHand().shrink(1);
                return InteractionResult.CONSUME;
            } else {
                rug.discard();
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return Rarity.UNCOMMON;
    }
}

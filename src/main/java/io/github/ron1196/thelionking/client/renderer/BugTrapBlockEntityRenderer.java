package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BugTrapBlockEntityRenderer implements BlockEntityRenderer<BugTrapBlockEntity> {

    private static final float HOVER_HEIGHT = 0.4F;
    private static final float OFFSET = 0.22F;
    private static final float ITEM_SCALE = 0.4F;

    public BugTrapBlockEntityRenderer(@NotNull BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(
            @NotNull BugTrapBlockEntity be,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = be.getInventory().getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            float ox = (slot == 1 || slot == 3) ? 0.5F + OFFSET : 0.5F - OFFSET;
            float oz = (slot == 2 || slot == 3) ? 0.5F + OFFSET : 0.5F - OFFSET;
            float angle =
                    switch (slot) {
                        case 0 -> 135.0F;
                        case 1 -> 225.0F;
                        case 2 -> 45.0F;
                        case 3 -> 315.0F;
                        default -> 0.0F;
                    };

            poseStack.pushPose();
            poseStack.translate(ox, HOVER_HEIGHT, oz);
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    buffer,
                    be.getLevel(),
                    0);
            poseStack.popPose();
        }
    }
}

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

    private static final float HOVER_HEIGHT = 0.5F;
    private static final float WALL_INSET = 0.2F;
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

            float ox;
            float oz;
            float angle;
            switch (slot) {
                case 0 -> { // NORTH face
                    ox = 0.5F;
                    oz = WALL_INSET;
                    angle = 180.0F;
                }
                case 1 -> { // WEST face
                    ox = WALL_INSET;
                    oz = 0.5F;
                    angle = 270.0F;
                }
                case 2 -> { // EAST face
                    ox = 1.0F - WALL_INSET;
                    oz = 0.5F;
                    angle = 90.0F;
                }
                case 3 -> { // SOUTH face
                    ox = 0.5F;
                    oz = 1.0F - WALL_INSET;
                    angle = 0.0F;
                }
                default -> {
                    continue;
                }
            }

            poseStack.pushPose();
            poseStack.translate(ox, HOVER_HEIGHT, oz);
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(
                    stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, be.getLevel(), 0);
            poseStack.popPose();
        }
    }
}

package io.github.ron1196.circleofcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.entity.GrindingBowlBlockEntity;
import io.github.ron1196.circleofcraft.client.model.GrindingStickModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Renders a spinning stick and the input item inside the grinding bowl while grinding.
 */
public class GrindingBowlRenderer implements BlockEntityRenderer<GrindingBowlBlockEntity> {

    private static final ResourceLocation STICK_TEXTURE = CircleOfCraftMod.id("textures/entity/grinding_stick.png");

    // Item rendering
    private static final double ITEM_BASE_X = 0.5;
    private static final double ITEM_BASE_Y = 0.611;
    private static final double ITEM_BASE_Z = 0.5;
    private static final double ITEM_STACK_OFFSET_Y = 0.01;
    private static final float ITEM_SCALE_X = 1F;
    private static final float ITEM_SCALE_Y = 1F;
    private static final float ITEM_ROTATION_SPEED = 0.5F;
    private static final float ITEM_SHRINK_FACTOR = 0.7F;
    private static final int MAX_VISIBLE_ITEMS = 4;

    // Stick rendering
    private static final double STICK_X = 0.5;
    private static final double STICK_Y = 1.4;
    private static final double STICK_Z = 0.5;

    private final GrindingStickModel model;

    public GrindingBowlRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new GrindingStickModel(GrindingStickModel.createLayer().bakeRoot());
    }

    @Override
    public void render(
            @NotNull GrindingBowlBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {

        renderItems(blockEntity, partialTick, poseStack, bufferSource, packedLight);
        renderStick(blockEntity, partialTick, poseStack, bufferSource, packedLight);
    }

    private void renderItems(
            GrindingBowlBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight) {
        ItemStack input = blockEntity.getInputItem();
        if (input.isEmpty()) return;

        float progress = blockEntity.getGrindProgress();
        boolean isGrinding = blockEntity.isGrinding();
        float itemRotation = blockEntity.getStickRotation(partialTick) * ITEM_ROTATION_SPEED;
        float shrink = isGrinding ? 1.0F - progress * ITEM_SHRINK_FACTOR : 1.0F;
        int maxStack = input.getMaxStackSize();
        int itemsPerLayer = Math.max(1, maxStack / MAX_VISIBLE_ITEMS);
        int renderCount = Math.min((input.getCount() + itemsPerLayer - 1) / itemsPerLayer, MAX_VISIBLE_ITEMS);
        for (int i = 0; i < renderCount; i++) {
            poseStack.pushPose();

            poseStack.translate(ITEM_BASE_X, ITEM_BASE_Y + i * ITEM_STACK_OFFSET_Y, ITEM_BASE_Z);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(itemRotation + i * 73));

            if (i == renderCount - 1) {
                poseStack.scale(ITEM_SCALE_X * shrink, ITEM_SCALE_Y * shrink, ITEM_SCALE_X * shrink);
            }

            poseStack.translate(0, -0.1, 0);

            Minecraft.getInstance()
                    .getItemRenderer()
                    .renderStatic(
                            input,
                            ItemDisplayContext.GROUND,
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            poseStack,
                            bufferSource,
                            blockEntity.getLevel(),
                            (int) blockEntity.getBlockPos().asLong() + i);
            poseStack.popPose();
        }
    }

    private void renderStick(
            GrindingBowlBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight) {
        if (!blockEntity.isGrinding()) return;

        float rotation = blockEntity.getStickRotation(partialTick);

        poseStack.pushPose();
        poseStack.translate(STICK_X, STICK_Y, STICK_Z);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(RenderType.entitySolid(STICK_TEXTURE)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                -1);

        poseStack.popPose();
    }
}

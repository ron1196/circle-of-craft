package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ron1196.thelionking.block.entity.GrindingBowlBlockEntity;
import io.github.ron1196.thelionking.client.model.GrindingStickModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Renders a spinning stick inside the grinding bowl while grinding.
 */
public class GrindingBowlRenderer implements BlockEntityRenderer<GrindingBowlBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("thelionking", "textures/entity/grinding_stick.png");

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

        if (!blockEntity.isGrinding()) return;

        float rotation = blockEntity.getStickRotation(partialTick);

        poseStack.pushPose();
        poseStack.translate(0.5, 1.4, 0.5);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(RenderType.entitySolid(TEXTURE)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                1.0F);

        poseStack.popPose();
    }
}

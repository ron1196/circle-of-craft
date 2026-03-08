package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.HyenaHeadBlock;
import io.github.ron1196.thelionking.block.entity.HyenaHeadBlockEntity;
import io.github.ron1196.thelionking.event.LKClientEvents;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HyenaHeadBlockEntityRenderer implements BlockEntityRenderer<HyenaHeadBlockEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/hyena.png");

    private final ModelPart head;

    public HyenaHeadBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(LKClientEvents.HYENA_HEAD_LAYER);
        this.head = root.getChild("head");
    }

    public static LayerDefinition createHeadLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Same head geometry as HyenaModel — head with ears
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 6.0F)
                        .texOffs(0, 15).addBox(-3.0F, -5.0F, 1.0F, 1.0F, 2.0F, 2.0F)
                        .texOffs(6, 15).addBox(2.0F, -5.0F, 1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void render(HyenaHeadBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        // Center on block
        poseStack.translate(0.5F, 0.0F, 0.5F);

        // Rotate based on block rotation property
        int rotation = blockEntity.getBlockState().getValue(HyenaHeadBlock.ROTATION);
        float angle = rotation * 22.5F;
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));

        // Flip Y axis — entity models have Y pointing down, block rendering has Y pointing up
        poseStack.scale(1.2F, -1.2F, 1.2F);

        // Move head down (in flipped space, this moves it up visually)
        poseStack.translate(0.0F, -0.25F, 0.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        head.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}

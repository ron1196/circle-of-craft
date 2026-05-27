package io.github.ron1196.circleofcraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MountedShooterModel {

    private static final float LEG_ANGLE = 25F / 180F * (float) Math.PI;

    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;

    public MountedShooterModel(ModelPart root) {
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Body: 4x4x16 tube (the barrel)
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -2F, -8F, 4, 4, 16),
                PartPose.offset(-1F, 16F, 0F));

        // Left leg: 1x8x1, angled outward
        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0F, -0.5F, 1, 8, 1),
                PartPose.offsetAndRotation(-1.5F, 17F, -1F, 0F, 0F, LEG_ANGLE));

        // Right leg: 1x8x1, angled outward (mirrored)
        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0F, -0.5F, 1, 8, 1),
                PartPose.offsetAndRotation(1.5F, 17F, -1F, 0F, 0F, -LEG_ANGLE));

        return LayerDefinition.create(mesh, 64, 32);
    }

    public void renderToBuffer(
            PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float fireAngle) {
        body.xRot = -fireAngle / 180F * (float) Math.PI;
        body.render(poseStack, buffer, packedLight, packedOverlay);
        leg1.render(poseStack, buffer, packedLight, packedOverlay);
        leg2.render(poseStack, buffer, packedLight, packedOverlay);
    }
}

package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BugModel<T extends LionKingAnimal> extends EntityModel<T> {

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart legLeft1;
    private final ModelPart legRight1;
    private final ModelPart legLeft2;
    private final ModelPart legRight2;

    public BugModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.legLeft1 = root.getChild("leg_left1");
        this.legRight1 = root.getChild("leg_right1");
        this.legLeft2 = root.getChild("leg_left2");
        this.legRight2 = root.getChild("leg_right2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 6.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 22.0F, -3.0F));

        root.addOrReplaceChild(
                "leg_left1",
                CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(-2.0F, 22.0F, -1.0F));
        root.addOrReplaceChild(
                "leg_right1",
                CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(2.0F, 22.0F, -1.0F));
        root.addOrReplaceChild(
                "leg_left2",
                CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(-2.0F, 22.0F, 1.0F));
        root.addOrReplaceChild(
                "leg_right2",
                CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(2.0F, 22.0F, 1.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.legLeft1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legRight1.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.legLeft2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.legRight2.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

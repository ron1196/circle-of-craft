package io.github.ron1196.circleofcraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.circleofcraft.entity.animal.ModAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ZazuModel<T extends ModAnimal> extends EntityModel<T> {

    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart bill;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart tail;

    public ZazuModel(ModelPart root) {
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.bill = root.getChild("bill");
        this.body = root.getChild("body");
        this.rightLeg = root.getChild("rightLeg");
        this.leftLeg = root.getChild("leftLeg");
        this.rightWing = root.getChild("rightWing");
        this.leftWing = root.getChild("leftWing");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 5.0F, 3.0F),
                PartPose.offset(0.0F, 16.0F, -4.0F));

        root.addOrReplaceChild(
                "headwear",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(-2.0F, -6.25F, -0.75F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 16.0F, -4.0F));

        root.addOrReplaceChild(
                "bill",
                CubeListBuilder.create().texOffs(46, 25).addBox(-2.0F, -4.0F, -7.0F, 4.0F, 2.0F, 5.0F),
                PartPose.offset(0.0F, 15.0F, -4.0F));

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -4.0F, -3.0F, 5.0F, 7.0F, 5.0F),
                PartPose.offset(0.5F, 16.0F, 0.5F));

        root.addOrReplaceChild(
                "rightLeg",
                CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(-2.0F, 19.0F, 1.0F));

        root.addOrReplaceChild(
                "leftLeg",
                CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(1.0F, 19.0F, 1.0F));

        root.addOrReplaceChild(
                "rightWing",
                CubeListBuilder.create().texOffs(24, 13).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
                PartPose.offset(-3.0F, 15.0F, 0.0F));

        root.addOrReplaceChild(
                "leftWing",
                CubeListBuilder.create().texOffs(24, 13).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
                PartPose.offset(3.0F, 15.0F, 0.0F));

        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(44, 5).addBox(-2.0F, 3.0F, 0.0F, 4.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 2.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = -(headPitch / 57.29578F);
        this.head.yRot = netHeadYaw / 57.29578F;

        this.headwear.xRot = this.head.xRot + 0.2F;
        this.headwear.yRot = this.head.yRot;

        this.bill.xRot = this.head.xRot;
        this.bill.yRot = this.head.yRot;

        this.body.xRot = (float) Math.PI / 2.0F;

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        this.rightWing.zRot = ageInTicks * 0.8F;
        this.leftWing.zRot = -ageInTicks * 0.8F;

        this.tail.xRot = 2.0F;
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
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        headwear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        bill.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightWing.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftWing.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

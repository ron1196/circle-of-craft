package io.github.ron1196.circleofcraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.circleofcraft.entity.animal.ModAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class GemsbokModel<T extends ModAnimal> extends EntityModel<T> {

    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart neck;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leftHorn;
    private final ModelPart rightHorn;

    public GemsbokModel(ModelPart root) {
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.earLeft = root.getChild("ear_left");
        this.earRight = root.getChild("ear_right");
        this.neck = root.getChild("neck");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.leftHorn = root.getChild("left_horn");
        this.rightHorn = root.getChild("right_horn");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(28, 0).addBox(-3.0F, -10.0F, -6.0F, 6.0F, 7.0F, 12.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 3.0F, 11.0F, 0.2967059F, 0.0F, 0.0F));

        root.addOrReplaceChild(
                "ear_left",
                CubeListBuilder.create().texOffs(28, 19).addBox(-3.8F, -12.0F, 3.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "ear_right",
                CubeListBuilder.create().texOffs(34, 19).addBox(2.8F, -12.0F, 3.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(0, 14).addBox(-2.5F, -6.0F, -5.0F, 5.0F, 8.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -9.0F, -1.064650F, 0.0F, 0.0F));

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 31).addBox(-7.0F, -10.0F, -7.0F, 13.0F, 10.0F, 23.0F),
                PartPose.offset(0.5F, 12.0F, -3.0F));

        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 12.0F, 10.0F));

        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 12.0F, 10.0F));

        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 12.0F, -7.0F));

        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 12.0F, -7.0F));

        root.addOrReplaceChild(
                "left_horn",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.8F, -9.5F, 5.8F, 1.0F, 1.0F, 13.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "right_horn",
                CubeListBuilder.create().texOffs(0, 0).addBox(1.8F, -9.5F, 5.8F, 1.0F, 1.0F, 13.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch / 57.29578F + 0.4014257F;
        this.head.yRot = netHeadYaw / 57.29578F;

        this.neck.xRot = -1.064650F;
        this.neck.yRot = this.head.yRot * 0.7F;

        this.earLeft.xRot = this.head.xRot;
        this.earLeft.yRot = this.head.yRot;
        this.earRight.xRot = this.head.xRot;
        this.earRight.yRot = this.head.yRot;

        this.leftHorn.xRot = this.head.xRot;
        this.leftHorn.yRot = this.head.yRot;
        this.rightHorn.xRot = this.head.xRot;
        this.rightHorn.yRot = this.head.yRot;

        this.tail.xRot = 0.2967059F;

        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack poseStack,
            @NotNull VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        earLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        earRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        neck.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftHorn.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightHorn.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

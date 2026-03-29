package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class CrocodileModel<T extends Mob> extends EntityModel<T> {

    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart jaw;
    private final ModelPart head;
    private final ModelPart legFrontLeft;
    private final ModelPart legBackLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legBackRight;
    private final ModelPart spines;

    public CrocodileModel(ModelPart root) {
        this.body = root.getChild("body");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.jaw = root.getChild("jaw");
        this.head = root.getChild("head");
        this.legFrontLeft = root.getChild("leg_front_left");
        this.legBackLeft = root.getChild("leg_back_left");
        this.legFrontRight = root.getChild("leg_front_right");
        this.legBackRight = root.getChild("leg_back_right");
        this.spines = root.getChild("spines");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(18, 83).addBox(-8.0F, -5.0F, 0.0F, 16.0F, 9.0F, 36.0F),
                PartPose.offset(0.0F, 17.0F, -16.0F));

        root.addOrReplaceChild(
                "tail1",
                CubeListBuilder.create().texOffs(0, 28).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 7.0F, 19.0F),
                PartPose.offset(0.0F, 13.0F, 18.0F));

        root.addOrReplaceChild(
                "tail2",
                CubeListBuilder.create().texOffs(0, 55).addBox(-6.0F, 1.5F, 17.0F, 12.0F, 5.0F, 16.0F),
                PartPose.offset(0.0F, 13.0F, 18.0F));

        root.addOrReplaceChild(
                "tail3",
                CubeListBuilder.create().texOffs(0, 77).addBox(-5.0F, 3.0F, 31.0F, 10.0F, 3.0F, 14.0F),
                PartPose.offset(0.0F, 13.0F, 18.0F));

        root.addOrReplaceChild(
                "jaw",
                CubeListBuilder.create().texOffs(58, 18).addBox(-6.5F, 0.3F, -19.0F, 13.0F, 4.0F, 19.0F),
                PartPose.offset(0.0F, 17.0F, -16.0F));

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -6.0F, -21.0F, 15.0F, 6.0F, 21.0F),
                PartPose.offset(0.0F, 18.5F, -16.0F));

        // Legs splay outward (default Z rotation set in setupAnim)
        root.addOrReplaceChild(
                "leg_front_left",
                CubeListBuilder.create().texOffs(2, 104).addBox(0.0F, 0.0F, -3.0F, 16.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, -11.0F, 0.0F, 0.0F, 0.4363F));

        root.addOrReplaceChild(
                "leg_back_left",
                CubeListBuilder.create().texOffs(2, 104).addBox(0.0F, 0.0F, -3.0F, 16.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(6.0F, 15.0F, 15.0F, 0.0F, 0.0F, 0.4363F));

        root.addOrReplaceChild(
                "leg_front_right",
                CubeListBuilder.create().texOffs(2, 104).mirror().addBox(-16.0F, 0.0F, -3.0F, 16.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, -11.0F, 0.0F, 0.0F, -0.4363F));

        root.addOrReplaceChild(
                "leg_back_right",
                CubeListBuilder.create().texOffs(2, 104).mirror().addBox(-16.0F, 0.0F, -3.0F, 16.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(-6.0F, 15.0F, 15.0F, 0.0F, 0.0F, -0.4363F));

        root.addOrReplaceChild(
                "spines",
                CubeListBuilder.create().texOffs(46, 45).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 4.0F, 32.0F),
                PartPose.offsetAndRotation(0.0F, 9.5F, -14.0F, -0.0349F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Spines tilt
        this.spines.xRot = ((float) Math.PI / 180F) * -2.0F;

        // Leg splay (Z) stays fixed from default pose; Y drives walk wobble
        float legWalk = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;

        this.legBackRight.yRot = legWalk;
        this.legBackLeft.yRot = legWalk;
        this.legFrontRight.yRot = legWalk;
        this.legFrontLeft.yRot = legWalk;

        // Tail sway
        this.tail1.yRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.5F;
        this.tail2.yRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.5625F;
        this.tail3.yRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.59375F;

        // Head pitch defaults to 0 (snap animation handled via entity data if needed)
        this.head.xRot = 0.0F;
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
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        jaw.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legFrontLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legBackLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legFrontRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legBackRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        spines.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

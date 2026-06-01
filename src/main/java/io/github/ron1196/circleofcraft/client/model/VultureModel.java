package io.github.ron1196.circleofcraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class VultureModel<T extends Mob> extends EntityModel<T> {

    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;

    public VultureModel(ModelPart root) {
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.wingLeft = root.getChild("wing_left");
        this.wingRight = root.getChild("wing_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 46).addBox(0.0F, 0.0F, 0.0F, 8.0F, 10.0F, 8.0F),
                PartPose.offset(-4.0F, 8.0F, -4.0F));

        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(44, 50).addBox(0.0F, 0.0F, 0.0F, 6.0F, 10.0F, 4.0F),
                PartPose.offset(-3.0F, 13.0F, 4.0F));

        root.addOrReplaceChild(
                "leg_left",
                CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offset(1.0F, 12.0F, 2.0F));

        root.addOrReplaceChild(
                "leg_right",
                CubeListBuilder.create().texOffs(40, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 12.0F, 2.0F),
                PartPose.offset(-3.0F, 12.0F, 2.0F));

        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offset(-2.0F, 3.0F, -4.0F));

        // Head with beak parts
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -2.0F, -3.0F, 6.0F, 6.0F, 6.0F)
                        .texOffs(28, 0)
                        .addBox(-1.5F, 1.5F, -6.9F, 3.0F, 2.0F, 2.0F)
                        .texOffs(46, 29)
                        .addBox(-2.0F, 0.5F, -7.0F, 4.0F, 2.0F, 5.0F),
                PartPose.offset(0.5F, 0.0F, -2.5F));

        root.addOrReplaceChild(
                "wing_left",
                CubeListBuilder.create().texOffs(0, 26).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 7.0F),
                PartPose.offset(4.0F, 5.0F, -2.0F));

        root.addOrReplaceChild(
                "wing_right",
                CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 10.0F, 7.0F),
                PartPose.offset(-4.0F, 5.0F, -2.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = -(headPitch * ((float) Math.PI / 180F));
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

        this.body.xRot = 0.698131F;
        this.tail.xRot = this.body.xRot;
        this.neck.xRot = 0.4364323F;

        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        float flap = Mth.cos(ageInTicks * 0.3F) * 0.5F;
        this.wingRight.zRot = flap;
        this.wingLeft.zRot = -flap;
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
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        neck.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        wingLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        wingRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

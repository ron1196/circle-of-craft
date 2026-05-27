package io.github.ron1196.circleofcraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.circleofcraft.entity.animal.ModAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class FlamingoModel<T extends ModAnimal> extends EntityModel<T> {

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart legLeft;
    private final ModelPart legRight;

    public FlamingoModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.wingLeft = root.getChild("wing_left");
        this.wingRight = root.getChild("wing_right");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head with bill, bill tip, and neck column all on the same part
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(8, 24)
                        .addBox(-2.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F)
                        .texOffs(24, 27)
                        .addBox(-1.5F, -16.0F, -5.0F, 3.0F, 2.0F, 3.0F)
                        .texOffs(36, 30)
                        .addBox(-1.0F, -14.0F, -5.0F, 2.0F, 1.0F, 1.0F)
                        .texOffs(0, 16)
                        .addBox(-1.0F, -15.0F, -1.0F, 2.0F, 14.0F, 2.0F),
                PartPose.offset(0.0F, 5.0F, -2.0F));

        // Body
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 7.0F, 8.0F),
                PartPose.offset(0.0F, 3.0F, 0.0F));

        // Tail
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(42, 23).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, -0.25F, 0.0F, 0.0F));

        // Wings
        root.addOrReplaceChild(
                "wing_left",
                CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 8.0F, 6.0F),
                PartPose.offset(-3.0F, 3.0F, 0.0F));

        root.addOrReplaceChild(
                "wing_right",
                CubeListBuilder.create().texOffs(50, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 8.0F, 6.0F),
                PartPose.offset(3.0F, 3.0F, 0.0F));

        // Legs with feet
        root.addOrReplaceChild(
                "leg_left",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F)
                        .texOffs(30, 17)
                        .addBox(-1.5F, 14.9F, -3.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(-2.0F, 8.0F, 0.0F));

        root.addOrReplaceChild(
                "leg_right",
                CubeListBuilder.create()
                        .texOffs(30, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F)
                        .texOffs(30, 17)
                        .addBox(-1.5F, 14.9F, -3.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(2.0F, 8.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Head follows look angles
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);

        // Legs walk with 0.9F multiplier
        this.legLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 0.9F * limbSwingAmount;
        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.9F * limbSwingAmount;

        // Wings flap on Z axis using ageInTicks for idle flap
        this.wingLeft.zRot = ageInTicks * 0.4F;
        this.wingRight.zRot = -ageInTicks * 0.4F;

        // Tail fixed angle
        this.tail.xRot = -0.25F;
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
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        wingLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        wingRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

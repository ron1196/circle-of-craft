package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class LionModel<T extends LionKingAnimal> extends EntityModel<T> {

    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart mane;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LionModel(ModelPart root) {
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.mane = root.getChild("mane");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -7.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(52, 34)
                        .addBox(-2.0F, 0.0F, -9.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "headwear",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-4.0F, -4.0F, -7.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "mane",
                CubeListBuilder.create().texOffs(0, 36).addBox(-7.0F, -7.0F, -5.0F, 14.0F, 14.0F, 9.0F),
                PartPose.offset(0.0F, 4.0F, -9.0F));

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 68).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F),
                PartPose.offset(0.0F, 5.0F, 2.0F));

        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 12.0F, 7.0F));
        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 12.0F, 7.0F));
        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 12.0F, -5.0F));
        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 12.0F, -5.0F));

        return LayerDefinition.create(mesh, 64, 96);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.headwear.xRot = this.head.xRot;
        this.headwear.yRot = this.head.yRot;
        this.mane.xRot = this.head.xRot;
        this.mane.yRot = this.head.yRot;
        this.body.xRot = ((float) Math.PI / 2F);
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
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
        mane.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

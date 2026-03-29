package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ZebraModel<T extends LionKingAnimal> extends EntityModel<T> {

    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart mane;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tail;

    public ZebraModel(ModelPart root) {
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.mane = root.getChild("mane");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(98, 26).addBox(-5.0F, -4.0F, -6.0F, 6.0F, 8.0F, 9.0F),
                PartPose.offsetAndRotation(2.0F, 3.0F, -6.0F, -1.064651F, 0.0F, 0.0F));

        PartDefinition headPart = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(84, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 14.0F)
                        .texOffs(72, 0)
                        .addBox(1.0F, -3.0F, 10.0F, 1.0F, 3.0F, 2.0F)
                        .texOffs(78, 0)
                        .addBox(6.0F, -3.0F, 10.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, -3.0F, -19.0F, 0.4014257F, 0.0F, 0.0F));

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 33).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 10.0F, 21.0F),
                PartPose.offset(0.0F, 12.0F, -1.0F));

        root.addOrReplaceChild(
                "mane",
                CubeListBuilder.create().texOffs(92, 47).addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 14.0F),
                PartPose.offsetAndRotation(-2.0F, 5.0F, -1.0F, 2.111848F, 0.0F, 0.0F));

        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, 10.0F));

        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, 10.0F));

        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, -5.0F));

        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, -5.0F));

        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, 13.5F, 2.0F, 12.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -1.0F, 0.296706F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.neck.xRot = -1.064651F;
        this.head.xRot = 0.4014257F;
        this.mane.xRot = 2.111848F;
        this.tail.xRot = 0.296706F;
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
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        mane.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        neck.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

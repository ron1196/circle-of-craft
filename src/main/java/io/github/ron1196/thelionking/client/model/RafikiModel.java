package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class RafikiModel extends EntityModel<Mob> {

    private final ModelPart body;
    private final ModelPart rightarm;
    private final ModelPart leftarm;
    private final ModelPart rightleg;
    private final ModelPart leftleg;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart head;
    private final ModelPart hair;

    public RafikiModel(ModelPart root) {
        this.body = root.getChild("body");
        this.rightarm = root.getChild("rightarm");
        this.leftarm = root.getChild("leftarm");
        this.rightleg = root.getChild("rightleg");
        this.leftleg = root.getChild("leftleg");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.head = root.getChild("head");
        this.hair = root.getChild("hair");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16).addBox(0.0F, 0.0F, 0.0F, 8, 12, 4),
                PartPose.offsetAndRotation(-4.0F, 1.0F, -2.825F, 0.3F, 0.0F, 0.0F));

        root.addOrReplaceChild("rightarm",
                CubeListBuilder.create().texOffs(40, 17).addBox(0.0F, 0.0F, 0.0F, 3, 11, 3),
                PartPose.offset(-7.0F, 2.0F, -1.5F));

        root.addOrReplaceChild("leftarm",
                CubeListBuilder.create().texOffs(40, 17).addBox(0.0F, 0.0F, 0.0F, 3, 11, 3),
                PartPose.offset(4.0F, 2.0F, -1.5F));

        root.addOrReplaceChild("rightleg",
                CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 4, 13, 4),
                PartPose.offset(-4.1F, 11.0F, 0.0F));

        root.addOrReplaceChild("leftleg",
                CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 4, 13, 4),
                PartPose.offset(0.1F, 11.0F, 0.0F));

        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, 0.0F, 0.0F, 1, 1, 5),
                PartPose.offsetAndRotation(-0.5F, 11.0F, 3.0F, 1.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(12, 59).addBox(0.0F, 0.0F, 0.0F, 1, 1, 4),
                PartPose.offset(-0.5F, 7.0F, 6.0F));

        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(22, 60).addBox(0.0F, 0.0F, 0.0F, 1, 1, 3),
                PartPose.offsetAndRotation(-0.5F, 7.0F, 10.2F, -1.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(30, 56).addBox(0.0F, 0.0F, 0.0F, 1, 1, 7),
                PartPose.offsetAndRotation(-0.5F, 9.5F, 11.7F, -2.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -1.0F, -5.0F, 7, 7, 6),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("hair",
                CubeListBuilder.create().texOffs(28, 0).addBox(-5.0F, -2.0F, -3.0F, 10, 10, 5),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(Mob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.hair.yRot = this.head.yRot;
        this.hair.xRot = this.head.xRot + (-11.0F / 180.0F * (float) Math.PI);

        this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightarm.zRot = 0.0F;
        this.leftarm.zRot = 0.0F;
        this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        this.rightarm.yRot = 0.0F;
        this.leftarm.yRot = 0.0F;

        this.rightarm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftarm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightarm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftarm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightarm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftarm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightleg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftleg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hair.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

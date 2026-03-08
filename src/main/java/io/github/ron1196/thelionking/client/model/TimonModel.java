package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class TimonModel extends EntityModel<Mob> {

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightarm;
    private final ModelPart leftarm;
    private final ModelPart rightleg;
    private final ModelPart leftleg;
    private final ModelPart tail;
    private final ModelPart leftear;
    private final ModelPart rightear;

    public TimonModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightarm = root.getChild("rightarm");
        this.leftarm = root.getChild("leftarm");
        this.rightleg = root.getChild("rightleg");
        this.leftleg = root.getChild("leftleg");
        this.tail = root.getChild("tail");
        this.leftear = root.getChild("leftear");
        this.rightear = root.getChild("rightear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition headPart = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 7, 6)
                        .texOffs(46, 7).addBox(-3.5F, -9.0F, -2.0F, 7, 1, 2)
                        .texOffs(0, 0).addBox(-1.0F, -5.0F, -5.0F, 2, 2, 1),
                PartPose.offset(0.0F, 1.0F, 1.0F));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(40, 12).addBox(-4.0F, 0.0F, -2.0F, 8, 15, 4),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("rightarm",
                CubeListBuilder.create().texOffs(20, 13).addBox(-3.0F, -2.0F, -2.0F, 3, 12, 3),
                PartPose.offset(-4.0F, 3.0F, 0.5F));

        root.addOrReplaceChild("leftarm",
                CubeListBuilder.create().texOffs(20, 13).addBox(-1.0F, -2.0F, -2.0F, 3, 12, 3),
                PartPose.offset(5.0F, 3.0F, 0.5F));

        root.addOrReplaceChild("rightleg",
                CubeListBuilder.create()
                        .texOffs(0, 13).addBox(-2.0F, 0.0F, -2.0F, 3, 9, 3)
                        .texOffs(44, 0).addBox(-2.7F, 8.0F, -3.9F, 4, 1, 6),
                PartPose.offset(-1.7F, 15.0F, 0.5F));

        root.addOrReplaceChild("leftleg",
                CubeListBuilder.create()
                        .texOffs(0, 13).addBox(-2.0F, 0.0F, -2.0F, 3, 9, 3)
                        .texOffs(44, 0).addBox(-2.3F, 8.0F, -3.9F, 4, 1, 6),
                PartPose.offset(2.7F, 15.0F, 0.5F));

        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, 0.0F, 0.0F, 2, 2, 16),
                PartPose.offset(0.0F, 9.0F, 1.0F));

        root.addOrReplaceChild("leftear",
                CubeListBuilder.create().texOffs(36, 0).addBox(-7.0F, -5.0F, -1.5F, 3, 5, 1),
                PartPose.offset(0.0F, 1.0F, 1.0F));

        root.addOrReplaceChild("rightear",
                CubeListBuilder.create().texOffs(36, 0).addBox(4.0F, -5.0F, -1.5F, 3, 5, 1),
                PartPose.offset(0.0F, 1.0F, 1.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(Mob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.tail.xRot = -0.908F + (headPitch * ((float) Math.PI / 180F));
        this.leftear.zRot = 0.38397F;
        this.leftear.yRot = this.head.yRot;
        this.leftear.xRot = this.head.xRot;
        this.rightear.zRot = -0.38397F;
        this.rightear.yRot = this.head.yRot;
        this.rightear.xRot = this.head.xRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightarm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftarm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightleg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftleg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

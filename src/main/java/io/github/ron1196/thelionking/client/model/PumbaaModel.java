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

public class PumbaaModel extends EntityModel<Mob> {

    private final ModelPart snout;
    private final ModelPart leftear;
    private final ModelPart rightear;
    private final ModelPart tail;
    private final ModelPart mane;
    private final ModelPart hair;
    private final ModelPart lefthorn;
    private final ModelPart righthorn;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public PumbaaModel(ModelPart root) {
        this.snout = root.getChild("snout");
        this.leftear = root.getChild("leftear");
        this.rightear = root.getChild("rightear");
        this.tail = root.getChild("tail");
        this.mane = root.getChild("mane");
        this.hair = root.getChild("hair");
        this.lefthorn = root.getChild("lefthorn");
        this.righthorn = root.getChild("righthorn");
        this.head = root.getChild("head");
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
                "snout",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.6F, -11.0F, 6, 4, 3),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "leftear",
                CubeListBuilder.create().texOffs(0, 31).addBox(-4.5F, -9.0F, -3.0F, 1, 4, 2),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "rightear",
                CubeListBuilder.create().texOffs(6, 31).addBox(3.5F, -9.0F, -3.0F, 1, 4, 2),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(60, 55).addBox(-0.5F, -1.5F, 1.0F, 1, 8, 1),
                PartPose.offset(0.0F, 11.0F, 8.0F));

        root.addOrReplaceChild(
                "mane",
                CubeListBuilder.create().texOffs(36, 18).addBox(-3.5F, -4.0F, -2.0F, 5, 4, 9),
                PartPose.offset(1.0F, 9.0F, -7.0F));

        root.addOrReplaceChild(
                "hair",
                CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -7.3F, -4.7F, 4, 5, 5),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "lefthorn",
                CubeListBuilder.create().texOffs(54, 31).addBox(-9.0F, -2.0F, -7.0F, 4, 1, 1),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "righthorn",
                CubeListBuilder.create().texOffs(54, 31).addBox(5.0F, -2.0F, -7.0F, 4, 1, 1),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(28, 0).addBox(-5.0F, -6.0F, -8.0F, 10, 10, 8),
                PartPose.offset(0.0F, 9.0F, -5.0F));

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 37).addBox(-5.0F, -10.0F, -7.0F, 12, 17, 10),
                PartPose.offset(-1.0F, 11.0F, 2.0F));

        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, 0.0F, -2.0F, 3, 8, 3),
                PartPose.offset(-3.0F, 16.0F, 6.0F));

        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, -2.0F, 3, 8, 3),
                PartPose.offset(3.0F, 16.0F, 6.0F));

        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, 0.0F, -2.0F, 3, 8, 3),
                PartPose.offset(-3.0F, 16.0F, -4.0F));

        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 0.0F, -2.0F, 3, 8, 3),
                PartPose.offset(3.0F, 16.0F, -4.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            Mob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float headXRot = headPitch * ((float) Math.PI / 180F);
        float headYRot = netHeadYaw * ((float) Math.PI / 180F);

        this.head.xRot = headXRot;
        this.head.yRot = headYRot;
        this.snout.xRot = headXRot;
        this.snout.yRot = headYRot;

        this.body.xRot = ((float) Math.PI / 2F);

        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        this.tail.xRot = 0.698132F;
        this.righthorn.xRot = headXRot;
        this.righthorn.yRot = 0.436332F + headYRot;
        this.lefthorn.xRot = headXRot;
        this.lefthorn.yRot = -0.436332F + headYRot;
        this.mane.xRot = -0.417716F;
        this.hair.xRot = -0.104719F + headXRot;
        this.hair.yRot = headYRot;
        this.rightear.yRot = headYRot;
        this.rightear.xRot = headXRot;
        this.leftear.yRot = headYRot;
        this.leftear.xRot = headXRot;
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
        snout.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        mane.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hair.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        lefthorn.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        righthorn.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

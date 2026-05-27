package io.github.ron1196.circleofcraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.circleofcraft.entity.animal.ModAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class RhinoModel<T extends ModAnimal> extends EntityModel<T> {

    private final ModelPart horn;
    private final ModelPart backhorn;
    private final ModelPart leftear;
    private final ModelPart rightear;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public RhinoModel(ModelPart root) {
        this.horn = root.getChild("horn");
        this.backhorn = root.getChild("backhorn");
        this.leftear = root.getChild("leftear");
        this.rightear = root.getChild("rightear");
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
                "horn",
                CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -9.0F, -4.5F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(0.0F, 9.0F, -11.0F));

        root.addOrReplaceChild(
                "backhorn",
                CubeListBuilder.create().texOffs(60, 8).addBox(-0.5F, -6.5F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 9.0F, -11.0F));

        root.addOrReplaceChild(
                "leftear",
                CubeListBuilder.create().texOffs(44, 0).addBox(-4.6F, -6.0F, 3.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 9.0F, -11.0F));

        root.addOrReplaceChild(
                "rightear",
                CubeListBuilder.create().texOffs(50, 0).addBox(3.6F, -6.0F, 3.0F, 1.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 9.0F, -11.0F));

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offset(0.0F, 9.0F, -11.0F));

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 32).addBox(-5.466667F, -10.0F, -8.0F, 13.0F, 22.0F, 10.0F),
                PartPose.offsetAndRotation(-1.0F, 9.0F, 1.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));

        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(-3.0F, 16.0F, 10.0F));

        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(3.0F, 16.0F, 10.0F));

        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(-3.0F, 16.0F, -5.0F));

        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(3.0F, 16.0F, -5.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch / 57.29578F + 0.279256F;
        this.head.yRot = netHeadYaw / 57.29578F;

        this.horn.xRot = this.head.xRot;
        this.horn.yRot = this.head.yRot;
        this.backhorn.xRot = this.head.xRot;
        this.backhorn.yRot = this.head.yRot;
        this.leftear.xRot = this.head.xRot;
        this.leftear.yRot = this.head.yRot;
        this.rightear.xRot = this.head.xRot;
        this.rightear.yRot = this.head.yRot;

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
        horn.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        backhorn.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

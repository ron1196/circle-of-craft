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

public class TermiteModel<T extends Mob> extends EntityModel<T> {

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg5;
    private final ModelPart leg6;
    private final ModelPart rightFeeler;
    private final ModelPart leftFeeler;

    public TermiteModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.leg5 = root.getChild("leg5");
        this.leg6 = root.getChild("leg6");
        this.rightFeeler = root.getChild("right_feeler");
        this.leftFeeler = root.getChild("left_feeler");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(10, 5).addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 21.0F),
                PartPose.offset(-3.0F, 17.0F, -5.0F));

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 7.0F),
                PartPose.offset(-4.0F, 14.0F, -10.0F));

        // Right-side legs (positive Z rotation = splay right)
        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, 0.0F, -2.0F, 13.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, -1.0F, 0.0F, 0.311111F, 0.311111F));

        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, 0.0F, 0.0F, 13.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.0F, 0.0F, 0.311111F));

        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, 0.0F, 0.0F, 13.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 4.0F, 0.0F, -0.6222222F, 0.311111F));

        // Left-side legs (negative Z rotation = splay left)
        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, 0.0F, 0.0F, 13.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, -1.0F, 0.0F, 2.85F, -0.311111F));

        root.addOrReplaceChild(
                "leg5",
                CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, 0.0F, -2.0F, 13.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.0F, 3.11F, -0.311111F));

        root.addOrReplaceChild(
                "leg6",
                CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, 0.0F, -2.0F, 13.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 19.0F, 4.0F, 0.0F, 3.75F, -0.311111F));

        root.addOrReplaceChild(
                "right_feeler",
                CubeListBuilder.create().texOffs(50, 18).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-3.0F, 15.0F, -8.0F, 0.0F, 0.0F, -0.1F));

        root.addOrReplaceChild(
                "left_feeler",
                CubeListBuilder.create().texOffs(50, 18).addBox(0.0F, 0.0F, -8.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(2.0F, 15.0F, -8.0F, 0.0F, 0.0F, 0.1F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float walkCycle = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;

        // Right-side legs
        this.leg1.zRot = 0.311111F + walkCycle * 0.25F;
        this.leg2.zRot = 0.311111F + walkCycle * -0.25F;
        this.leg3.zRot = 0.311111F + walkCycle * 0.25F;

        // Left-side legs
        this.leg4.zRot = -0.311111F + walkCycle * 0.25F;
        this.leg5.zRot = -0.311111F + walkCycle * -0.25F;
        this.leg6.zRot = -0.311111F + walkCycle * 0.25F;
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
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg5.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg6.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightFeeler.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftFeeler.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

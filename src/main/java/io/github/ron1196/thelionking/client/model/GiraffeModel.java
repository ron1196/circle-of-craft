package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GiraffeModel<T extends LionKingAnimal> extends EntityModel<T> {

    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public GiraffeModel(ModelPart root) {
        this.body = root.getChild("body");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Body
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -8.0F, -13.0F, 12.0F, 16.0F, 26.0F),
                PartPose.offset(0.0F, -11.0F, 0.0F));

        // Neck (includes long neck column)
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .addBox(-4.5F, -13.0F, -4.5F, 9.0F, 11.0F, 9.0F)
                        .texOffs(78, 0)
                        .addBox(-3.0F, -37.0F, -3.0F, 6.0F, 40.0F, 6.0F),
                PartPose.offset(0.0F, -14.0F, -7.0F));

        // Head (includes ears, ossicones, snout)
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(96, 48)
                        .addBox(-3.0F, -43.0F, -6.0F, 6.0F, 6.0F, 10.0F)
                        // Left ear
                        .texOffs(10, 0)
                        .addBox(-4.0F, -45.0F, 1.5F, 1.0F, 3.0F, 2.0F)
                        // Right ear
                        .texOffs(17, 0)
                        .addBox(3.0F, -45.0F, 1.5F, 1.0F, 3.0F, 2.0F)
                        // Left ossicone
                        .texOffs(0, 0)
                        .addBox(-2.5F, -47.0F, 0.0F, 1.0F, 4.0F, 1.0F)
                        // Right ossicone
                        .texOffs(5, 0)
                        .addBox(1.5F, -47.0F, 0.0F, 1.0F, 4.0F, 1.0F)
                        // Snout
                        .texOffs(76, 56)
                        .addBox(-2.0F, -41.0F, -11.0F, 4.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, -14.0F, -7.0F));

        // Tail
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(104, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 24.0F, 1.0F),
                PartPose.offset(0.0F, -12.0F, 13.0F));

        // Legs
        root.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create().texOffs(112, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 27.0F, 4.0F),
                PartPose.offset(-3.9F, -3.0F, 8.0F));
        root.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create().texOffs(112, 0).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 27.0F, 4.0F),
                PartPose.offset(3.9F, -3.0F, 8.0F));
        root.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create().texOffs(112, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 27.0F, 4.0F),
                PartPose.offset(-3.9F, -3.0F, -7.0F));
        root.addOrReplaceChild(
                "leg4",
                CubeListBuilder.create().texOffs(112, 0).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 27.0F, 4.0F),
                PartPose.offset(3.9F, -3.0F, -7.0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Legs — 0.5x multiplier for slower gait
        this.leg1.xRot = 0.5F * Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = 0.5F * Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = 0.5F * Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = 0.5F * Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        // Tail sway
        this.tail.zRot = 0.2F * Mth.cos(ageInTicks * 0.1F);

        // Neck and head — slight forward tilt + head tracking
        float neckBaseAngle = 10.0F / 180.0F * (float) Math.PI;
        this.neck.xRot = neckBaseAngle + headPitch / 57.29578F;
        this.neck.yRot = netHeadYaw / 57.29578F;
        this.head.xRot = neckBaseAngle + headPitch / 57.29578F;
        this.head.yRot = netHeadYaw / 57.29578F;
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
        neck.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

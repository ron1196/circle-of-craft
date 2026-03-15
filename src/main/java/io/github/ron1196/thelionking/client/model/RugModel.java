package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.thelionking.entity.RugEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class RugModel extends EntityModel<RugEntity> {

    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;

    private final ModelPart body;
    private final ModelPart mane;
    private final ModelPart head;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tail;

    public RugModel(ModelPart root) {
        this.body = root.getChild("body");
        this.mane = root.getChild("mane");
        this.head = root.getChild("head");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, 0.0F, 0.0F, 20, 25, 2),
                PartPose.offsetAndRotation(-10.0F, 24.0F, -10.0F, DEG_TO_RAD * 90.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("mane",
                CubeListBuilder.create().texOffs(0, 43).addBox(0.0F, 0.0F, 0.0F, 14, 12, 9),
                PartPose.offset(-7.0F, 12.0F, -18.0F));

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(32, 27).addBox(0.0F, 0.0F, 0.0F, 8, 8, 8)
                        .texOffs(52, 45).addBox(2.0F, 4.0F, -2.0F, 4, 4, 2),
                PartPose.offset(-4.0F, 15.0F, -20.0F));

        root.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -4.0F, 2, 12, 4),
                PartPose.offsetAndRotation(-8.0F, 22.1F, 14.0F, DEG_TO_RAD * 22.0F, 0.0F, DEG_TO_RAD * 90.0F));

        root.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -4.0F, 2, 12, 4),
                PartPose.offsetAndRotation(8.0F, 22.1F, 14.0F, DEG_TO_RAD * 22.0F, 0.0F, DEG_TO_RAD * -90.0F));

        root.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2, 12, 4),
                PartPose.offsetAndRotation(-8.0F, 22.1F, -10.0F, DEG_TO_RAD * -22.0F, 0.0F, DEG_TO_RAD * 90.0F));

        root.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, 0.0F, 2, 12, 4),
                PartPose.offsetAndRotation(8.0F, 22.1F, -10.0F, DEG_TO_RAD * -22.0F, 0.0F, DEG_TO_RAD * -90.0F));

        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 12),
                PartPose.offsetAndRotation(0.0F, 22.05F, 14.0F, DEG_TO_RAD * -4.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull RugEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Static rug - no animation needed
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        mane.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

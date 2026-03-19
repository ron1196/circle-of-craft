package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class DikDikModel<T extends LionKingAnimal> extends EntityModel<T> {

  private final ModelPart head;
  private final ModelPart body;
  private final ModelPart leg1;
  private final ModelPart leg2;
  private final ModelPart leg3;
  private final ModelPart leg4;

  public DikDikModel(ModelPart root) {
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

    // Head with snout, ears, horns, and neck as sub-cubes on the same part
    root.addOrReplaceChild(
        "head",
        CubeListBuilder.create()
            // head
            .texOffs(42, 23)
            .addBox(-2.0F, -9.0F, -3.0F, 4.0F, 4.0F, 5.0F)
            // snout
            .texOffs(18, 28)
            .addBox(-1.0F, -7.3F, -5.0F, 2.0F, 2.0F, 2.0F)
            // earL
            .texOffs(0, 27)
            .addBox(-2.8F, -11.0F, 0.5F, 1.0F, 3.0F, 2.0F)
            // earR
            .texOffs(8, 27)
            .addBox(1.8F, -11.0F, 0.5F, 1.0F, 3.0F, 2.0F)
            // hornL
            .texOffs(0, 21)
            .addBox(-1.5F, -11.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            // hornR
            .texOffs(0, 21)
            .addBox(0.5F, -11.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            // neck
            .texOffs(28, 22)
            .addBox(-1.5F, -8.0F, -2.0F, 3.0F, 7.0F, 3.0F),
        PartPose.offset(0.0F, 11.0F, -4.5F));

    root.addOrReplaceChild(
        "body",
        CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 6.0F, 14.0F),
        PartPose.offset(0.0F, 9.0F, -7.0F));

    root.addOrReplaceChild(
        "leg1",
        CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
        PartPose.offset(-1.7F, 14.0F, 5.0F));

    root.addOrReplaceChild(
        "leg2",
        CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
        PartPose.offset(1.7F, 14.0F, 5.0F));

    root.addOrReplaceChild(
        "leg3",
        CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
        PartPose.offset(-1.7F, 14.0F, -5.0F));

    root.addOrReplaceChild(
        "leg4",
        CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
        PartPose.offset(1.7F, 14.0F, -5.0F));

    return LayerDefinition.create(mesh, 64, 32);
  }

  @Override
  public void setupAnim(
      T entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    this.head.xRot = headPitch / 57.29578F;
    this.head.yRot = netHeadYaw / 57.29578F;
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
    leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
  }
}

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

public class HyenaModel<T extends Mob> extends EntityModel<T> {

  private final ModelPart head;
  private final ModelPart body;
  private final ModelPart leg1;
  private final ModelPart leg2;
  private final ModelPart leg3;
  private final ModelPart leg4;
  private final ModelPart tail;

  public HyenaModel(ModelPart root) {
    this.head = root.getChild("head");
    this.body = root.getChild("body");
    this.leg1 = root.getChild("leg1");
    this.leg2 = root.getChild("leg2");
    this.leg3 = root.getChild("leg3");
    this.leg4 = root.getChild("leg4");
    this.tail = root.getChild("tail");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();

    // Head with ears
    root.addOrReplaceChild(
        "head",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 6.0F)
            .texOffs(0, 15)
            .addBox(-3.0F, -5.0F, 1.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(6, 15)
            .addBox(2.0F, -5.0F, 1.0F, 1.0F, 2.0F, 2.0F),
        PartPose.offset(-1.0F, 13.5F, -9.0F));

    // Body with mane ridge
    root.addOrReplaceChild(
        "body",
        CubeListBuilder.create()
            .texOffs(28, 11)
            .addBox(-4.0F, -8.0F, -3.0F, 6.0F, 15.0F, 6.0F)
            .texOffs(16, 20)
            .addBox(-2.0F, -8.0F, 3.0F, 2.0F, 11.0F, 1.0F),
        PartPose.offset(0.0F, 14.0F, 2.0F));

    root.addOrReplaceChild(
        "leg1",
        CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
        PartPose.offset(-2.5F, 16.0F, 7.0F));

    root.addOrReplaceChild(
        "leg2",
        CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
        PartPose.offset(0.5F, 16.0F, 7.0F));

    root.addOrReplaceChild(
        "leg3",
        CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
        PartPose.offset(-2.5F, 16.0F, -4.0F));

    root.addOrReplaceChild(
        "leg4",
        CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
        PartPose.offset(0.5F, 16.0F, -4.0F));

    root.addOrReplaceChild(
        "tail",
        CubeListBuilder.create().texOffs(16, 20).addBox(-1.0F, 1.5F, -1.0F, 2.0F, 9.0F, 1.0F),
        PartPose.offset(-1.0F, 12.0F, 8.0F));

    return LayerDefinition.create(mesh, 64, 32);
  }

  @Override
  public void setupAnim(
      @NotNull T entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    this.head.xRot = headPitch * ((float) Math.PI / 180F);
    this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

    this.body.xRot = ((float) Math.PI / 2F);

    this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
    this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
    this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

    this.tail.xRot = 1.2F;
    this.tail.yRot = 0.0F;
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
    head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    leg4.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
  }

  /** Allow renderers to hide head for headless skeletal hyena variant. */
  public ModelPart getHead() {
    return head;
  }
}

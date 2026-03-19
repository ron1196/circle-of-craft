package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.RugModel;
import io.github.ron1196.thelionking.entity.RugEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RugRenderer extends EntityRenderer<RugEntity> {

  private static final ResourceLocation TEXTURE_SCAR =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/rug_scar.png");
  private static final ResourceLocation TEXTURE_ZIRA =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/rug_zira.png");

  private final RugModel model;

  public RugRenderer(EntityRendererProvider.Context context, RugModel model) {
    super(context);
    this.model = model;
  }

  @Override
  public void render(
      @NotNull RugEntity entity,
      float entityYaw,
      float partialTick,
      @NotNull PoseStack poseStack,
      @NotNull MultiBufferSource bufferSource,
      int packedLight) {
    poseStack.pushPose();

    // Match old renderer: translate up 1.5, flip, rotate by yaw
    poseStack.translate(0.0F, 1.5F, 0.0F);
    poseStack.scale(-1.0F, -1.0F, 1.0F);
    poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() % 360.0F));

    var renderType = model.renderType(getTextureLocation(entity));
    var buffer = bufferSource.getBuffer(renderType);
    model.renderToBuffer(
        poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

    poseStack.popPose();

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(@NotNull RugEntity entity) {
    return entity.getRugType() == RugEntity.TYPE_ZIRA ? TEXTURE_ZIRA : TEXTURE_SCAR;
  }
}

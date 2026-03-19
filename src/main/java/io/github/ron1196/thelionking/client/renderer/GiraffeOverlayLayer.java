package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.GiraffeModel;
import io.github.ron1196.thelionking.entity.animal.GiraffeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Renders saddle and tie overlay textures on giraffes. Uses the same model geometry — just swaps
 * the texture to an overlay that's mostly transparent.
 */
public class GiraffeOverlayLayer extends RenderLayer<GiraffeEntity, GiraffeModel<GiraffeEntity>> {

  private static final ResourceLocation SADDLE_TEXTURE =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/giraffe/saddle.png");

  private static final String[] TIE_NAMES = {
    "tie", "tie_white", "tie_blue", "tie_yellow",
    "tie_red", "tie_purple", "tie_green", "tie_black"
  };

  public GiraffeOverlayLayer(RenderLayerParent<GiraffeEntity, GiraffeModel<GiraffeEntity>> parent) {
    super(parent);
  }

  @Override
  public void render(
      @NotNull PoseStack poseStack,
      @NotNull MultiBufferSource buffer,
      int packedLight,
      @NotNull GiraffeEntity giraffe,
      float limbSwing,
      float limbSwingAmount,
      float partialTick,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Render saddle overlay
    if (giraffe.isSaddled()) {
      renderColoredCutoutModel(
          getParentModel(),
          SADDLE_TEXTURE,
          poseStack,
          buffer,
          packedLight,
          giraffe,
          1.0F,
          1.0F,
          1.0F);
    }

    // Render tie overlay
    int tie = giraffe.getTie();
    if (tie >= 0 && tie < TIE_NAMES.length) {
      ResourceLocation tieTexture =
          new ResourceLocation(
              TheLionKingMod.MOD_ID, "textures/entity/giraffe/" + TIE_NAMES[tie] + ".png");
      renderColoredCutoutModel(
          getParentModel(), tieTexture, poseStack, buffer, packedLight, giraffe, 1.0F, 1.0F, 1.0F);
    }
  }
}

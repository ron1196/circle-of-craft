package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AnimalRenderer<T extends LionKingAnimal, M extends EntityModel<T>>
    extends MobRenderer<T, M> {

  private final ResourceLocation texture;

  public AnimalRenderer(
      EntityRendererProvider.Context context, M model, String textureName, float shadowRadius) {
    super(context, model, shadowRadius);
    this.texture =
        new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
    return texture;
  }
}

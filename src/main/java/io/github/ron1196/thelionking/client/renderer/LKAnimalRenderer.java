package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.LKAnimalModel;
import io.github.ron1196.thelionking.entity.animal.LKAnimal;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LKAnimalRenderer<T extends LKAnimal> extends MobRenderer<T, LKAnimalModel<T>> {

    private final ResourceLocation texture;

    public LKAnimalRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer,
                            String textureName, float shadowRadius) {
        super(context, new LKAnimalModel<>(context.bakeLayer(layer)), shadowRadius);
        this.texture = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}

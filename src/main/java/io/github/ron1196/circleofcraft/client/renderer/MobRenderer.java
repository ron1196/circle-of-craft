package io.github.ron1196.circleofcraft.client.renderer;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class MobRenderer<T extends Mob, M extends EntityModel<T>>
        extends net.minecraft.client.renderer.entity.MobRenderer<T, M> {

    private final ResourceLocation texture;

    public MobRenderer(EntityRendererProvider.Context context, M model, String textureName, float shadowRadius) {
        super(context, model, shadowRadius);
        this.texture = CircleOfCraftMod.id("textures/entity/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}

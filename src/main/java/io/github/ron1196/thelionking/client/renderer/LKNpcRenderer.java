package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

/**
 * Renderer for NPC entities that reuses existing animal models as placeholders.
 * Uses raw EntityModel type to bypass generic bounds between model and entity.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LKNpcRenderer<T extends Mob> extends MobRenderer<T, EntityModel<T>> {

    private final ResourceLocation texture;

    public LKNpcRenderer(EntityRendererProvider.Context context, EntityModel model,
                         String textureName, float shadowRadius) {
        super(context, (EntityModel<T>) model, shadowRadius);
        this.texture = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}

package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.NpcPlaceholderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class LKNpcRenderer extends MobRenderer<Mob, NpcPlaceholderModel> {

    private final ResourceLocation texture;

    public LKNpcRenderer(EntityRendererProvider.Context context, NpcPlaceholderModel model,
                         String textureName, float shadowRadius) {
        super(context, model, shadowRadius);
        this.texture = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        return texture;
    }
}

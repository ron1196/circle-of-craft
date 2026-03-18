package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class NpcRenderer extends MobRenderer<Mob, EntityModel<Mob>> {

    private final ResourceLocation texture;
    private final float scale;

    public NpcRenderer(
            EntityRendererProvider.Context context,
            EntityModel<Mob> model,
            String textureName,
            float shadowRadius,
            float scale) {
        super(context, model, shadowRadius);
        this.texture = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
        this.scale = scale;
    }

    public NpcRenderer(
            EntityRendererProvider.Context context, EntityModel<Mob> model, String textureName, float shadowRadius) {
        this(context, model, textureName, shadowRadius, 1.0F);
    }

    @Override
    protected void scale(Mob entity, PoseStack poseStack, float partialTick) {
        if (scale != 1.0F) {
            poseStack.scale(scale, scale, scale);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        return texture;
    }
}

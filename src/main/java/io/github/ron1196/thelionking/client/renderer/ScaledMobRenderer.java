package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class ScaledMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation texture;
    private final float scale;

    public ScaledMobRenderer(EntityRendererProvider.Context context, M model,
                             String textureName, float shadowRadius, float scale) {
        super(context, model, shadowRadius);
        this.texture = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
        this.scale = scale;
    }

    @Override
    protected void scale(@NotNull T entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return texture;
    }
}

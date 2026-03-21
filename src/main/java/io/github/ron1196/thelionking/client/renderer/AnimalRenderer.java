package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AnimalRenderer<T extends LionKingAnimal, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation texture;
    private final float babyScale;

    public AnimalRenderer(
            EntityRendererProvider.Context context, M model, String textureName, float shadowRadius, float babyScale) {
        super(context, model, shadowRadius);
        this.texture = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/" + textureName + ".png");
        this.babyScale = babyScale;
    }

    @Override
    protected void scale(@NotNull T entity, @NotNull PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(babyScale, babyScale, babyScale);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return texture;
    }
}

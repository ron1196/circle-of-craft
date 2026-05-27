package io.github.ron1196.circleofcraft.client.renderer;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.hostile.HyenaEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HyenaRenderer extends MobRenderer<HyenaEntity, EntityModel<HyenaEntity>> {

    private static final ResourceLocation[] TEXTURES = {
        CircleOfCraftMod.id("textures/entity/hyena_0.png"),
        CircleOfCraftMod.id("textures/entity/hyena_1.png"),
        CircleOfCraftMod.id("textures/entity/hyena_2.png")
    };

    public HyenaRenderer(EntityRendererProvider.Context context, EntityModel<HyenaEntity> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HyenaEntity entity) {
        int variant = entity.getVariant();
        if (variant < 0 || variant >= TEXTURES.length) variant = 0;
        return TEXTURES[variant];
    }
}

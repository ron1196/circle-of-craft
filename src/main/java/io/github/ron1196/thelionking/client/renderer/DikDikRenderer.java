package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.DikDikEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DikDikRenderer extends MobRenderer<DikDikEntity, EntityModel<DikDikEntity>> {

    private static final ResourceLocation[] TEXTURES = {
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dikdik_0.png"),
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dikdik_1.png"),
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dikdik_2.png")
    };

    public DikDikRenderer(EntityRendererProvider.Context context, EntityModel<DikDikEntity> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(DikDikEntity entity) {
        int variant = entity.getVariant();
        if (variant < 0 || variant >= TEXTURES.length) variant = 0;
        return TEXTURES[variant];
    }
}

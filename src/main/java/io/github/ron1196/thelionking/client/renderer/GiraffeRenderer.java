package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.GiraffeModel;
import io.github.ron1196.thelionking.entity.animal.GiraffeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GiraffeRenderer extends MobRenderer<GiraffeEntity, GiraffeModel<GiraffeEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            TheLionKingMod.MOD_ID, "textures/entity/giraffe.png");

    public GiraffeRenderer(EntityRendererProvider.Context context, GiraffeModel<GiraffeEntity> model) {
        super(context, model, 0.8F);
        this.addLayer(new GiraffeOverlayLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GiraffeEntity entity) {
        return TEXTURE;
    }
}

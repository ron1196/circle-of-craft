package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.LionModel;
import io.github.ron1196.thelionking.entity.animal.LionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LionRenderer extends AnimalRenderer<LionEntity, LionModel<LionEntity>> {

    private static final ResourceLocation MANED_TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/lion.png");
    private static final ResourceLocation MANELESS_TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/lioness.png");

    public LionRenderer(
            EntityRendererProvider.Context context, LionModel<LionEntity> model, float shadowRadius, float babyScale) {
        super(context, model, "lion", shadowRadius, babyScale);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LionEntity entity) {
        return entity.shouldShowMane() ? MANED_TEXTURE : MANELESS_TEXTURE;
    }
}

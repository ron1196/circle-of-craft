package io.github.ron1196.circleofcraft.client.renderer;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.client.model.LionModel;
import io.github.ron1196.circleofcraft.entity.animal.LionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LionRenderer extends AnimalRenderer<LionEntity, LionModel<LionEntity>> {

    private static final ResourceLocation MANED_TEXTURE = CircleOfCraftMod.id("textures/entity/lion.png");
    private static final ResourceLocation MANELESS_TEXTURE = CircleOfCraftMod.id("textures/entity/lioness.png");

    public LionRenderer(
            EntityRendererProvider.Context context, LionModel<LionEntity> model, float shadowRadius, float babyScale) {
        super(context, model, "lion", shadowRadius, babyScale);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LionEntity entity) {
        return entity.shouldShowMane() ? MANED_TEXTURE : MANELESS_TEXTURE;
    }
}

package io.github.ron1196.circleofcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.animal.DikDikEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DikDikRenderer extends MobRenderer<DikDikEntity, EntityModel<DikDikEntity>> {

    private static final ResourceLocation[] TEXTURES = {
        CircleOfCraftMod.id("textures/entity/dikdik_0.png"),
        CircleOfCraftMod.id("textures/entity/dikdik_1.png"),
        CircleOfCraftMod.id("textures/entity/dikdik_2.png")
    };

    private final float babyScale;

    public DikDikRenderer(
            EntityRendererProvider.Context context,
            EntityModel<DikDikEntity> model,
            float shadowRadius,
            float babyScale) {
        super(context, model, shadowRadius);
        this.babyScale = babyScale;
    }

    @Override
    protected void scale(@NotNull DikDikEntity entity, @NotNull PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(babyScale, babyScale, babyScale);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DikDikEntity entity) {
        int variant = entity.getVariant();
        if (variant < 0 || variant >= TEXTURES.length) variant = 0;
        return TEXTURES[variant];
    }
}

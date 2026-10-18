package io.github.ron1196.circleofcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.circleofcraft.client.model.SimbaModel;
import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SimbaRenderer extends GeoEntityRenderer<SimbaEntity> {

    // The adult is authored about 1.9 blocks tall and fits the 1.6-block hitbox at 0.85. The cub model is shorter:
    // at 0.55 a newborn is half the adult's height, and it grows to 0.77 (70%) before turning into the adult.
    private static final float ADULT_SCALE = 0.85F;
    private static final float CUB_START_SCALE = 0.55F;
    private static final float CUB_END_SCALE = 0.77F;

    public SimbaRenderer(EntityRendererProvider.Context context) {
        super(context, new SimbaModel());
        shadowRadius = 0.5F;
    }

    @Override
    public void scaleModelForRender(
            float widthScale,
            float heightScale,
            @NotNull PoseStack poseStack,
            @NotNull SimbaEntity animatable,
            @NotNull BakedGeoModel model,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        float scale = animatable.isBaby()
                ? CUB_START_SCALE + (CUB_END_SCALE - CUB_START_SCALE) * animatable.growth()
                : ADULT_SCALE;
        super.scaleModelForRender(
                scale, scale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}

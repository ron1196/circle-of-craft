package io.github.ron1196.circleofcraft.client.renderer;

import io.github.ron1196.circleofcraft.client.model.TermiteQueenModel;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteQueenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TermiteQueenRenderer extends GeoEntityRenderer<TermiteQueenEntity> {

    public TermiteQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new TermiteQueenModel());
    }

    @Override
    public float getMotionAnimThreshold(TermiteQueenEntity animatable) {
        return 0.001f;
    }

    @Override
    protected float getDeathMaxRotation(@NotNull TermiteQueenEntity entity) {
        return 0.0F;
    }
}

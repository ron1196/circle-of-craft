package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.client.model.TermiteQueenModel;
import io.github.ron1196.thelionking.entity.hostile.TermiteQueenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TermiteQueenRenderer extends GeoEntityRenderer<TermiteQueenEntity> {

    public TermiteQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new TermiteQueenModel());
    }

    @Override
    public float getMotionAnimThreshold(TermiteQueenEntity animatable) {
        return 0.001f;
    }
}

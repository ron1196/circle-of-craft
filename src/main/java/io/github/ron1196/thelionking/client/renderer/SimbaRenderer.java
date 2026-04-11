package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.client.model.SimbaModel;
import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SimbaRenderer extends GeoEntityRenderer<SimbaEntity> {

    public SimbaRenderer(EntityRendererProvider.Context context) {
        super(context, new SimbaModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public float getMotionAnimThreshold(SimbaEntity animatable) {
        return 0.001f;
    }
}

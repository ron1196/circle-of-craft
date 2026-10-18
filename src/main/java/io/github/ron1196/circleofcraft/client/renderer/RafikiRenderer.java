package io.github.ron1196.circleofcraft.client.renderer;

import io.github.ron1196.circleofcraft.client.model.RafikiModel;
import io.github.ron1196.circleofcraft.entity.npc.RafikiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RafikiRenderer extends GeoEntityRenderer<RafikiEntity> {

    // The model is authored at about 2.9 blocks to the top of the head; this fits it to the 1.6-block hitbox.
    private static final float MODEL_SCALE = 0.55F;

    public RafikiRenderer(EntityRendererProvider.Context context) {
        super(context, new RafikiModel());
        shadowRadius = 0.35F;
        withScale(MODEL_SCALE);
    }
}

package io.github.ron1196.thelionking.client.model;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SimbaModel extends DefaultedEntityGeoModel<SimbaEntity> {

    public SimbaModel() {
        super(new ResourceLocation(TheLionKingMod.MOD_ID, "simba"));
    }
}

package io.github.ron1196.thelionking.client.model;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.hostile.TermiteQueenEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TermiteQueenModel extends DefaultedEntityGeoModel<TermiteQueenEntity> {

    public TermiteQueenModel() {
        super(new ResourceLocation(TheLionKingMod.MOD_ID, "termite_queen"));
    }
}

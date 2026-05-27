package io.github.ron1196.circleofcraft.client.model;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteQueenEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TermiteQueenModel extends DefaultedEntityGeoModel<TermiteQueenEntity> {

    public TermiteQueenModel() {
        super(CircleOfCraftMod.id("termite_queen"));
    }
}

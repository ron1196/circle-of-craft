package io.github.ron1196.circleofcraft.client.model;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SimbaModel extends DefaultedEntityGeoModel<SimbaEntity> {

    public SimbaModel() {
        super(CircleOfCraftMod.id(SimbaEntity.REGISTRY_NAME), true);
    }

    @Override
    public @NotNull ResourceLocation getModelResource(@NotNull SimbaEntity simba) {
        return CircleOfCraftMod.id("geo/entity/" + variant(simba) + ".geo.json");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(@NotNull SimbaEntity simba) {
        return CircleOfCraftMod.id("textures/entity/" + variant(simba) + ".png");
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(@NotNull SimbaEntity simba) {
        return CircleOfCraftMod.id("animations/entity/" + variant(simba) + ".animation.json");
    }

    // Every clip keys the head each frame, so tracking is added on top of the animated head instead of replacing it.
    @Override
    public void setCustomAnimations(
            @NotNull SimbaEntity animatable, long instanceId, @NotNull AnimationState<SimbaEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone(headBone);
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(head.getRotX() + entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(head.getRotY() + entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

    private static String variant(SimbaEntity simba) {
        return simba.isBaby() ? "simba_cub" : "simba_adult";
    }
}

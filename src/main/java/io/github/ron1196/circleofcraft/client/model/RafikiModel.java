package io.github.ron1196.circleofcraft.client.model;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.npc.RafikiEntity;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RafikiModel extends DefaultedEntityGeoModel<RafikiEntity> {

    public RafikiModel() {
        super(CircleOfCraftMod.id(RafikiEntity.REGISTRY_NAME), true);
    }

    // Both clips key the head every frame, so tracking is added on top of the animated head instead of replacing it.
    @Override
    public void setCustomAnimations(
            @NotNull RafikiEntity animatable, long instanceId, @NotNull AnimationState<RafikiEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone(headBone);
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(head.getRotX() + entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(head.getRotY() + entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}

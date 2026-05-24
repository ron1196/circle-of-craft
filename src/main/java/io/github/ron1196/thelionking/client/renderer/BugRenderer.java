package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class BugRenderer<T extends LionKingAnimal, M extends EntityModel<T>> extends AnimalRenderer<T, M> {

    private static final float BASE_SCALE = 0.3F;

    public BugRenderer(
            @NotNull EntityRendererProvider.Context context,
            @NotNull M model,
            @NotNull String textureName,
            float shadowRadius,
            float babyScale) {
        super(context, model, textureName, shadowRadius, babyScale);
    }

    @Override
    protected void scale(@NotNull T entity, @NotNull PoseStack poseStack, float partialTick) {
        poseStack.scale(BASE_SCALE, BASE_SCALE, BASE_SCALE);
        super.scale(entity, poseStack, partialTick);
    }
}

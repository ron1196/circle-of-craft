package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.GiraffeModel;
import io.github.ron1196.thelionking.entity.animal.GiraffeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GiraffeRenderer extends MobRenderer<GiraffeEntity, GiraffeModel<GiraffeEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/giraffe.png");
    private static final float BABY_SCALE = 0.5F;

    public GiraffeRenderer(EntityRendererProvider.Context context, GiraffeModel<GiraffeEntity> model) {
        super(context, model, 0.8F);
        this.addLayer(new GiraffeOverlayLayer(this));
    }

    @Override
    protected void scale(@NotNull GiraffeEntity entity, @NotNull PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(BABY_SCALE, BABY_SCALE, BABY_SCALE);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GiraffeEntity entity) {
        return TEXTURE;
    }
}

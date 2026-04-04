package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.hostile.TermiteEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class TermiteRenderer<M extends EntityModel<TermiteEntity>> extends MobRenderer<TermiteEntity, M> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/termite.png");
    private static final float SCALE = 0.4F;

    public TermiteRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected void scale(@NotNull TermiteEntity entity, @NotNull PoseStack poseStack, float partialTick) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }

    @Override
    protected float getWhiteOverlayProgress(@NotNull TermiteEntity entity, float partialTick) {
        float swelling = entity.getSwelling(partialTick);
        return (int) (swelling * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(swelling, 0.5F, 1.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TermiteEntity entity) {
        return TEXTURE;
    }
}

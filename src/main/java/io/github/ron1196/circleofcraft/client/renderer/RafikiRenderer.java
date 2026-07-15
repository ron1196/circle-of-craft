package io.github.ron1196.circleofcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.client.model.RafikiModel;
import io.github.ron1196.circleofcraft.entity.npc.RafikiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class RafikiRenderer extends MobRenderer<Mob, RafikiModel> {

    private static final ResourceLocation TEXTURE =
            CircleOfCraftMod.id("textures/entity/" + RafikiEntity.REGISTRY_NAME + ".png");

    private final float scale;

    public RafikiRenderer(EntityRendererProvider.Context context, RafikiModel model, float shadowRadius, float scale) {
        super(context, model, shadowRadius);
        this.scale = scale;
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected void scale(@NotNull Mob entity, @NotNull PoseStack poseStack, float partialTick) {
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Mob entity) {
        return TEXTURE;
    }
}

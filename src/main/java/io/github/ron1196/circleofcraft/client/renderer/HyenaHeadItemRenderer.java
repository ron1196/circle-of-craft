package io.github.ron1196.circleofcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.event.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class HyenaHeadItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation[] TEXTURES = {
        CircleOfCraftMod.id("textures/entity/hyena_0.png"),
        CircleOfCraftMod.id("textures/entity/hyena_1.png"),
        CircleOfCraftMod.id("textures/entity/hyena_2.png"),
        CircleOfCraftMod.id("textures/entity/skeletal_hyena.png")
    };

    private ModelPart head;

    public HyenaHeadItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    private ModelPart getHead() {
        if (head == null) {
            head = Minecraft.getInstance()
                    .getEntityModels()
                    .bakeLayer(ClientEvents.HYENA_HEAD_LAYER)
                    .getChild("head");
        }
        return head;
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay) {
        CustomData beData = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
        int hyenaType = beData.copyTag().getInt("HyenaType");
        if (hyenaType < 0 || hyenaType >= TEXTURES.length) hyenaType = 0;

        poseStack.pushPose();

        // Position like vanilla skulls — angled to show front + side
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(210.0F));
        poseStack.scale(1.2F, -1.2F, 1.2F);
        poseStack.translate(0.0F, -0.25F, 0.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURES[hyenaType]));
        getHead().render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}

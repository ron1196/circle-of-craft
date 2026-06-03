package io.github.ron1196.circleofcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.PortalBlock;
import io.github.ron1196.circleofcraft.network.ClientWorldState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class HudOverlays {

    private static final ResourceLocation FLATULENCE_TEXTURE = CircleOfCraftMod.id("textures/gui/flatulence.png");
    private static final int FLATULENCE_DURATION = 60;
    private static final float PORTAL_OVERLAY_MAX_ALPHA = 0.8F;
    private static final double OVERLAY_Z_DEPTH = -90.0;

    // TODO(Task 15): migrate RenderGuiEvent.Post -> RegisterGuiLayersEvent
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        renderPortalOverlay();
        renderFlatulenceOverlay(event);
    }

    private static void renderPortalOverlay() {
        if (ClientWorldState.portalOverlayTicks <= 0) return;
        if (ClientWorldState.portalBlockName.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;

        // Reset overlay if server stopped sending updates (player left portal)
        if (mc.level != null && mc.level.getGameTime() - ClientWorldState.portalLastUpdateTick > 2) {
            ClientWorldState.portalOverlayTicks = 0;
            return;
        }

        // Get the block's sprite from the stitched texture atlas (handles animation automatically)
        var texture = CircleOfCraftMod.id("block/" + ClientWorldState.portalBlockName);
        var sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        float alpha = portalOverlayAlpha(ClientWorldState.portalOverlayTicks);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(0, height, OVERLAY_Z_DEPTH).uv(u0, v1).endVertex();
        buffer.vertex(width, height, OVERLAY_Z_DEPTH).uv(u1, v1).endVertex();
        buffer.vertex(width, 0, OVERLAY_Z_DEPTH).uv(u1, v0).endVertex();
        buffer.vertex(0, 0, OVERLAY_Z_DEPTH).uv(u0, v0).endVertex();
        tesselator.end();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    private static float portalOverlayAlpha(int ticks) {
        float progress = Math.min(ticks / (float) PortalBlock.PORTAL_WAIT_TICKS, 1.0F);
        return (float) Math.sqrt(progress) * PORTAL_OVERLAY_MAX_ALPHA;
    }

    private static void renderFlatulenceOverlay(RenderGuiEvent.Post event) {
        if (ClientWorldState.flatulenceTimer <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        float alpha = ClientWorldState.flatulenceTimer / (float) FLATULENCE_DURATION;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        event.getGuiGraphics().blit(FLATULENCE_TEXTURE, 0, 0, 0, 0, width, height, width, height);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        ClientWorldState.flatulenceTimer--;
    }
}

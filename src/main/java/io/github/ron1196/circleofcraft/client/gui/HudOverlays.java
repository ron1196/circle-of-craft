package io.github.ron1196.circleofcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.PortalBlock;
import io.github.ron1196.circleofcraft.network.ClientWorldState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class HudOverlays {

    private static final Identifier FLATULENCE_TEXTURE = CircleOfCraftMod.id("textures/gui/flatulence.png");
    private static final int FLATULENCE_DURATION = 60;
    private static final float PORTAL_OVERLAY_MAX_ALPHA = 0.8F;

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                CircleOfCraftMod.id("portal_overlay"), (guiGraphics, deltaTracker) -> renderPortalOverlay(guiGraphics));
        event.registerAboveAll(
                CircleOfCraftMod.id("flatulence_overlay"),
                (guiGraphics, deltaTracker) -> renderFlatulenceOverlay(guiGraphics));
    }

    private static void renderPortalOverlay(GuiGraphicsExtractor guiGraphics) {
        if (ClientWorldState.portalOverlayTicks <= 0) return;
        if (ClientWorldState.portalBlockName.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;

        if (mc.level != null && mc.level.getGameTime() - ClientWorldState.portalLastUpdateTick > 2) {
            ClientWorldState.portalOverlayTicks = 0;
            return;
        }

        Identifier texture = CircleOfCraftMod.id("block/" + ClientWorldState.portalBlockName);
        TextureAtlasSprite sprite =
                mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float alpha = portalOverlayAlpha(ClientWorldState.portalOverlayTicks);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, width, height, ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
        RenderSystem.disableBlend();
    }

    private static float portalOverlayAlpha(int ticks) {
        float progress = Math.min(ticks / (float) PortalBlock.PORTAL_WAIT_TICKS, 1.0F);
        return (float) Math.sqrt(progress) * PORTAL_OVERLAY_MAX_ALPHA;
    }

    private static void renderFlatulenceOverlay(GuiGraphicsExtractor guiGraphics) {
        if (ClientWorldState.flatulenceTimer <= 0) return;

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float alpha = ClientWorldState.flatulenceTimer / (float) FLATULENCE_DURATION;

        RenderSystem.enableBlend();
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                FLATULENCE_TEXTURE,
                0,
                0,
                0,
                0,
                width,
                height,
                width,
                height,
                ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
        RenderSystem.disableBlend();

        ClientWorldState.flatulenceTimer--;
    }
}

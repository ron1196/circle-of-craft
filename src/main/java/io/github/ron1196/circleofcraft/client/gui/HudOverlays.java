package io.github.ron1196.circleofcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.PortalBlock;
import io.github.ron1196.circleofcraft.network.ClientWorldState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class HudOverlays {

    private static final ResourceLocation FLATULENCE_TEXTURE = CircleOfCraftMod.id("textures/gui/flatulence.png");
    private static final int FLATULENCE_DURATION = 60;
    private static final float PORTAL_OVERLAY_MAX_ALPHA = 0.8F;
    private static final int OVERLAY_BLIT_OFFSET = -90;

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                CircleOfCraftMod.id("portal_overlay"), (guiGraphics, deltaTracker) -> renderPortalOverlay(guiGraphics));
        event.registerAboveAll(
                CircleOfCraftMod.id("flatulence_overlay"),
                (guiGraphics, deltaTracker) -> renderFlatulenceOverlay(guiGraphics));
    }

    private static void renderPortalOverlay(GuiGraphics guiGraphics) {
        if (ClientWorldState.portalOverlayTicks <= 0) return;
        if (ClientWorldState.portalBlockName.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;

        if (mc.level != null && mc.level.getGameTime() - ClientWorldState.portalLastUpdateTick > 2) {
            ClientWorldState.portalOverlayTicks = 0;
            return;
        }

        ResourceLocation texture = CircleOfCraftMod.id("block/" + ClientWorldState.portalBlockName);
        TextureAtlasSprite sprite =
                mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float alpha = portalOverlayAlpha(ClientWorldState.portalOverlayTicks);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(0, 0, OVERLAY_BLIT_OFFSET, width, height, sprite, 1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.disableBlend();
    }

    private static float portalOverlayAlpha(int ticks) {
        float progress = Math.min(ticks / (float) PortalBlock.PORTAL_WAIT_TICKS, 1.0F);
        return (float) Math.sqrt(progress) * PORTAL_OVERLAY_MAX_ALPHA;
    }

    private static void renderFlatulenceOverlay(GuiGraphics guiGraphics) {
        if (ClientWorldState.flatulenceTimer <= 0) return;

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float alpha = ClientWorldState.flatulenceTimer / (float) FLATULENCE_DURATION;

        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(FLATULENCE_TEXTURE, 0, 0, 0, 0, width, height, width, height);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        ClientWorldState.flatulenceTimer--;
    }
}

package io.github.ron1196.thelionking.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.network.ClientWorldState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HudOverlays {

    private static final ResourceLocation FLATULENCE_TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/gui/flatulence.png");
    private static final int FLATULENCE_DURATION = 60;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
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

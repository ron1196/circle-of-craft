package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.LKAnimalModel;
import io.github.ron1196.thelionking.client.renderer.LKAnimalRenderer;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LKClientEvents {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LKAnimalModel.LION_LAYER,
                () -> LKAnimalModel.createBodyLayer(8.0F, 6.0F, 8.0F));
        event.registerLayerDefinition(LKAnimalModel.LIONESS_LAYER,
                () -> LKAnimalModel.createBodyLayer(7.0F, 5.0F, 7.0F));
        event.registerLayerDefinition(LKAnimalModel.ZEBRA_LAYER,
                () -> LKAnimalModel.createBodyLayer(8.0F, 6.0F, 9.0F));
        event.registerLayerDefinition(LKAnimalModel.GIRAFFE_LAYER,
                () -> LKAnimalModel.createBodyLayer(8.0F, 8.0F, 14.0F));
        event.registerLayerDefinition(LKAnimalModel.RHINO_LAYER,
                () -> LKAnimalModel.createBodyLayer(10.0F, 8.0F, 8.0F));
        event.registerLayerDefinition(LKAnimalModel.GEMSBOK_LAYER,
                () -> LKAnimalModel.createBodyLayer(7.0F, 6.0F, 8.0F));
        event.registerLayerDefinition(LKAnimalModel.DIKDIK_LAYER,
                () -> LKAnimalModel.createBodyLayer(4.0F, 4.0F, 5.0F));
        event.registerLayerDefinition(LKAnimalModel.FLAMINGO_LAYER,
                () -> LKAnimalModel.createBodyLayer(4.0F, 5.0F, 8.0F));
        event.registerLayerDefinition(LKAnimalModel.ZAZU_LAYER,
                () -> LKAnimalModel.createBodyLayer(3.0F, 3.0F, 3.0F));
        event.registerLayerDefinition(LKAnimalModel.BUG_LAYER,
                () -> LKAnimalModel.createBodyLayer(2.0F, 2.0F, 2.0F));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LKEntityTypes.LION.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.LION_LAYER, "lion", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.LIONESS.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.LIONESS_LAYER, "lioness", 0.6F));
        event.registerEntityRenderer(LKEntityTypes.ZEBRA.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.ZEBRA_LAYER, "zebra", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.GIRAFFE.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.GIRAFFE_LAYER, "giraffe", 0.8F));
        event.registerEntityRenderer(LKEntityTypes.RHINO.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.RHINO_LAYER, "rhino", 0.9F));
        event.registerEntityRenderer(LKEntityTypes.GEMSBOK.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.GEMSBOK_LAYER, "gemsbok", 0.6F));
        event.registerEntityRenderer(LKEntityTypes.DIKDIK.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.DIKDIK_LAYER, "dikdik", 0.3F));
        event.registerEntityRenderer(LKEntityTypes.FLAMINGO.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.FLAMINGO_LAYER, "flamingo", 0.3F));
        event.registerEntityRenderer(LKEntityTypes.ZAZU.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.ZAZU_LAYER, "zazu", 0.25F));
        event.registerEntityRenderer(LKEntityTypes.BUG.get(),
                ctx -> new LKAnimalRenderer<>(ctx, LKAnimalModel.BUG_LAYER, "bug", 0.15F));
    }
}

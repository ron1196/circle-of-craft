package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.*;
import io.github.ron1196.thelionking.client.renderer.LKAnimalRenderer;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LKClientEvents {

    public static final ModelLayerLocation LION_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "lion"), "main");
    public static final ModelLayerLocation LIONESS_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "lioness"), "main");
    public static final ModelLayerLocation ZEBRA_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "zebra"), "main");
    public static final ModelLayerLocation GIRAFFE_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "giraffe"), "main");
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "rhino"), "main");
    public static final ModelLayerLocation GEMSBOK_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "gemsbok"), "main");
    public static final ModelLayerLocation DIKDIK_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "dikdik"), "main");
    public static final ModelLayerLocation FLAMINGO_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "flamingo"), "main");
    public static final ModelLayerLocation ZAZU_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "zazu"), "main");
    public static final ModelLayerLocation BUG_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "bug_entity"), "main");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LION_LAYER, LionModel::createBodyLayer);
        event.registerLayerDefinition(LIONESS_LAYER, LionModel::createBodyLayer);
        event.registerLayerDefinition(ZEBRA_LAYER, ZebraModel::createBodyLayer);
        event.registerLayerDefinition(GIRAFFE_LAYER, GiraffeModel::createBodyLayer);
        event.registerLayerDefinition(RHINO_LAYER, RhinoModel::createBodyLayer);
        event.registerLayerDefinition(GEMSBOK_LAYER, GemsbokModel::createBodyLayer);
        event.registerLayerDefinition(DIKDIK_LAYER, DikDikModel::createBodyLayer);
        event.registerLayerDefinition(FLAMINGO_LAYER, FlamingoModel::createBodyLayer);
        event.registerLayerDefinition(ZAZU_LAYER, ZazuModel::createBodyLayer);
        event.registerLayerDefinition(BUG_LAYER, BugModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LKEntityTypes.LION.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new LionModel<>(ctx.bakeLayer(LION_LAYER)), "lion", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.LIONESS.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new LionModel<>(ctx.bakeLayer(LIONESS_LAYER)), "lioness", 0.6F));
        event.registerEntityRenderer(LKEntityTypes.ZEBRA.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new ZebraModel<>(ctx.bakeLayer(ZEBRA_LAYER)), "zebra", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.GIRAFFE.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new GiraffeModel<>(ctx.bakeLayer(GIRAFFE_LAYER)), "giraffe", 0.8F));
        event.registerEntityRenderer(LKEntityTypes.RHINO.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new RhinoModel<>(ctx.bakeLayer(RHINO_LAYER)), "rhino", 0.9F));
        event.registerEntityRenderer(LKEntityTypes.GEMSBOK.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new GemsbokModel<>(ctx.bakeLayer(GEMSBOK_LAYER)), "gemsbok", 0.6F));
        event.registerEntityRenderer(LKEntityTypes.DIKDIK.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new DikDikModel<>(ctx.bakeLayer(DIKDIK_LAYER)), "dikdik", 0.3F));
        event.registerEntityRenderer(LKEntityTypes.FLAMINGO.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new FlamingoModel<>(ctx.bakeLayer(FLAMINGO_LAYER)), "flamingo", 0.3F));
        event.registerEntityRenderer(LKEntityTypes.ZAZU.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new ZazuModel<>(ctx.bakeLayer(ZAZU_LAYER)), "zazu", 0.25F));
        event.registerEntityRenderer(LKEntityTypes.BUG.get(),
                ctx -> new LKAnimalRenderer<>(ctx, new BugModel<>(ctx.bakeLayer(BUG_LAYER)), "bug", 0.15F));
    }
}

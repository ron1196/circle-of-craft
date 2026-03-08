package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.gui.BongoDrumScreen;
import io.github.ron1196.thelionking.client.gui.BugTrapScreen;
import io.github.ron1196.thelionking.client.gui.GrindingBowlScreen;
import io.github.ron1196.thelionking.client.model.*;
import io.github.ron1196.thelionking.client.renderer.LKAnimalRenderer;
import io.github.ron1196.thelionking.client.renderer.LKMobRenderer;
import io.github.ron1196.thelionking.client.renderer.LKNpcRenderer;
import io.github.ron1196.thelionking.client.renderer.LKScaledMobRenderer;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LKClientEvents {

    // Passive animal layers
    public static final ModelLayerLocation LION_LAYER = layer("lion");
    public static final ModelLayerLocation LIONESS_LAYER = layer("lioness");
    public static final ModelLayerLocation ZEBRA_LAYER = layer("zebra");
    public static final ModelLayerLocation GIRAFFE_LAYER = layer("giraffe");
    public static final ModelLayerLocation RHINO_LAYER = layer("rhino");
    public static final ModelLayerLocation GEMSBOK_LAYER = layer("gemsbok");
    public static final ModelLayerLocation DIKDIK_LAYER = layer("dikdik");
    public static final ModelLayerLocation FLAMINGO_LAYER = layer("flamingo");
    public static final ModelLayerLocation ZAZU_LAYER = layer("zazu");
    public static final ModelLayerLocation BUG_LAYER = layer("bug_entity");

    // Hostile entity layers
    public static final ModelLayerLocation HYENA_LAYER = layer("hyena");
    public static final ModelLayerLocation SKELETAL_HYENA_LAYER = layer("skeletal_hyena");
    public static final ModelLayerLocation OUTLANDER_LAYER = layer("outlander");
    public static final ModelLayerLocation OUTLANDESS_LAYER = layer("outlandess");
    public static final ModelLayerLocation VULTURE_LAYER = layer("vulture");
    public static final ModelLayerLocation CROCODILE_LAYER = layer("crocodile");
    public static final ModelLayerLocation TERMITE_LAYER = layer("termite");

    // NPC layers (reuse animal mesh definitions)
    public static final ModelLayerLocation RAFIKI_LAYER = layer("rafiki");
    public static final ModelLayerLocation SIMBA_LAYER = layer("simba");
    public static final ModelLayerLocation TIMON_LAYER = layer("timon");
    public static final ModelLayerLocation PUMBAA_LAYER = layer("pumbaa");
    public static final ModelLayerLocation SCAR_LAYER = layer("scar");
    public static final ModelLayerLocation ZIRA_LAYER = layer("zira");

    // Block entity layers
    public static final ModelLayerLocation HYENA_HEAD_LAYER = layer("hyena_head");

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(new ResourceLocation(TheLionKingMod.MOD_ID, name), "main");
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Passive
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

        // Hostile
        event.registerLayerDefinition(HYENA_LAYER, HyenaModel::createBodyLayer);
        event.registerLayerDefinition(SKELETAL_HYENA_LAYER, HyenaModel::createBodyLayer);
        event.registerLayerDefinition(OUTLANDER_LAYER, OutlanderModel::createBodyLayer);
        event.registerLayerDefinition(OUTLANDESS_LAYER, OutlanderModel::createBodyLayer);
        event.registerLayerDefinition(VULTURE_LAYER, VultureModel::createBodyLayer);
        event.registerLayerDefinition(CROCODILE_LAYER, CrocodileModel::createBodyLayer);
        event.registerLayerDefinition(TERMITE_LAYER, TermiteModel::createBodyLayer);

        // NPC layers (proper models ported from original mod)
        event.registerLayerDefinition(RAFIKI_LAYER, RafikiModel::createBodyLayer);
        event.registerLayerDefinition(SIMBA_LAYER, SimbaModel::createBodyLayer);
        event.registerLayerDefinition(TIMON_LAYER, TimonModel::createBodyLayer);
        event.registerLayerDefinition(PUMBAA_LAYER, PumbaaModel::createBodyLayer);
        event.registerLayerDefinition(SCAR_LAYER, LionModel::createBodyLayer);
        event.registerLayerDefinition(ZIRA_LAYER, OutlanderModel::createBodyLayer);

        // Block entity layers
        event.registerLayerDefinition(HYENA_HEAD_LAYER,
                io.github.ron1196.thelionking.client.renderer.HyenaHeadBlockEntityRenderer::createHeadLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Passive
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

        // Hostile
        event.registerEntityRenderer(LKEntityTypes.HYENA.get(),
                ctx -> new LKMobRenderer<>(ctx, new HyenaModel<>(ctx.bakeLayer(HYENA_LAYER)), "hyena", 0.5F));
        event.registerEntityRenderer(LKEntityTypes.SKELETAL_HYENA.get(),
                ctx -> new LKMobRenderer<>(ctx, new HyenaModel<>(ctx.bakeLayer(SKELETAL_HYENA_LAYER)), "skeletal_hyena", 0.5F));
        event.registerEntityRenderer(LKEntityTypes.OUTLANDER.get(),
                ctx -> new LKMobRenderer<>(ctx, new OutlanderModel<>(ctx.bakeLayer(OUTLANDER_LAYER)), "outlander", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.OUTLANDESS.get(),
                ctx -> new LKMobRenderer<>(ctx, new OutlanderModel<>(ctx.bakeLayer(OUTLANDESS_LAYER)), "outlandess", 0.6F));
        event.registerEntityRenderer(LKEntityTypes.VULTURE.get(),
                ctx -> new LKMobRenderer<>(ctx, new VultureModel<>(ctx.bakeLayer(VULTURE_LAYER)), "vulture", 0.5F));
        event.registerEntityRenderer(LKEntityTypes.CROCODILE.get(),
                ctx -> new LKMobRenderer<>(ctx, new CrocodileModel<>(ctx.bakeLayer(CROCODILE_LAYER)), "crocodile", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.TERMITE.get(),
                ctx -> new LKScaledMobRenderer<>(ctx, new TermiteModel<>(ctx.bakeLayer(TERMITE_LAYER)), "termite", 0.15F, 0.4F));

        // NPCs — proper models ported from original mod (shadow, scale from old code)
        event.registerEntityRenderer(LKEntityTypes.RAFIKI.get(),
                ctx -> new LKNpcRenderer(ctx, new RafikiModel(ctx.bakeLayer(RAFIKI_LAYER)), "rafiki", 0.35F, 0.5F));
        event.registerEntityRenderer(LKEntityTypes.SIMBA.get(),
                ctx -> new LKNpcRenderer(ctx, new SimbaModel(ctx.bakeLayer(SIMBA_LAYER)), "simba", 0.5F, 0.5F));
        event.registerEntityRenderer(LKEntityTypes.TIMON.get(),
                ctx -> new LKNpcRenderer(ctx, new TimonModel(ctx.bakeLayer(TIMON_LAYER)), "timon", 0.2F, 0.5F));
        event.registerEntityRenderer(LKEntityTypes.PUMBAA.get(),
                ctx -> new LKNpcRenderer(ctx, new PumbaaModel(ctx.bakeLayer(PUMBAA_LAYER)), "pumbaa", 0.6F));
        event.registerEntityRenderer(LKEntityTypes.SCAR.get(),
                ctx -> new LKNpcRenderer(ctx, new NpcPlaceholderModel(ctx.bakeLayer(SCAR_LAYER)), "scar", 0.7F));
        event.registerEntityRenderer(LKEntityTypes.ZIRA.get(),
                ctx -> new LKNpcRenderer(ctx, new NpcPlaceholderModel(ctx.bakeLayer(ZIRA_LAYER)), "zira", 0.5F, 0.5F));

        // Projectiles
        event.registerEntityRenderer(LKEntityTypes.DART.get(),
                ctx -> new net.minecraft.client.renderer.entity.ArrowRenderer<>(ctx) {
                    private final ResourceLocation BLUE = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dart_blue.png");
                    private final ResourceLocation RED = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dart_red.png");
                    private final ResourceLocation YELLOW = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dart_yellow.png");
                    private final ResourceLocation PINK = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dart_pink.png");
                    private final ResourceLocation BLACK = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/dart_black.png");
                    @Override
                    public ResourceLocation getTextureLocation(io.github.ron1196.thelionking.entity.projectile.DartEntity entity) {
                        return switch (entity.getDartType()) {
                            case RED -> RED;
                            case YELLOW -> YELLOW;
                            case PINK -> PINK;
                            case BLACK -> BLACK;
                            default -> BLUE;
                        };
                    }
                });
        event.registerEntityRenderer(LKEntityTypes.SPEAR.get(),
                ctx -> new net.minecraft.client.renderer.entity.ArrowRenderer<>(ctx) {
                    private final ResourceLocation SPEAR = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/spear.png");
                    @Override
                    public ResourceLocation getTextureLocation(io.github.ron1196.thelionking.entity.projectile.SpearEntity entity) {
                        return SPEAR;
                    }
                });
        event.registerEntityRenderer(LKEntityTypes.PUMBAA_BOMB.get(), ThrownItemRenderer::new);

        // Block entity renderers
        event.registerBlockEntityRenderer(io.github.ron1196.thelionking.registry.LKBlockEntityTypes.HYENA_HEAD.get(),
                io.github.ron1196.thelionking.client.renderer.HyenaHeadBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(LKMenuTypes.GRINDING_BOWL_MENU.get(), GrindingBowlScreen::new);
            MenuScreens.register(LKMenuTypes.BUG_TRAP_MENU.get(), BugTrapScreen::new);
            MenuScreens.register(LKMenuTypes.BONGO_DRUM_MENU.get(), BongoDrumScreen::new);
        });
    }
}

package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.gui.BongoDrumScreen;
import io.github.ron1196.thelionking.client.gui.BugTrapScreen;
import io.github.ron1196.thelionking.client.gui.GrindingBowlScreen;
import io.github.ron1196.thelionking.client.gui.QuiverScreen;
import io.github.ron1196.thelionking.client.gui.SimbaInventoryScreen;
import io.github.ron1196.thelionking.client.gui.TimonMerchantScreen;
import io.github.ron1196.thelionking.client.model.*;
import io.github.ron1196.thelionking.client.renderer.*;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LKMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

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
    public static final ModelLayerLocation BUG_LAYER = layer("bug");

    // Hostile entity layers
    public static final ModelLayerLocation HYENA_LAYER = layer("hyena");
    public static final ModelLayerLocation SKELETAL_HYENA_LAYER = layer("skeletal_hyena");
    public static final ModelLayerLocation OUTLANDER_LAYER = layer("outlander");
    public static final ModelLayerLocation OUTLANDESS_LAYER = layer("outlandess");
    public static final ModelLayerLocation VULTURE_LAYER = layer("vulture");
    public static final ModelLayerLocation CROCODILE_LAYER = layer("crocodile");
    public static final ModelLayerLocation TERMITE_LAYER = layer("termite");
    public static final ModelLayerLocation TERMITE_QUEEN_LAYER = layer("termite_queen");

    // NPC layers (reuse animal mesh definitions)
    public static final ModelLayerLocation RAFIKI_LAYER = layer("rafiki");
    public static final ModelLayerLocation SIMBA_LAYER = layer("simba");
    public static final ModelLayerLocation TIMON_LAYER = layer("timon");
    public static final ModelLayerLocation PUMBAA_LAYER = layer("pumbaa");
    public static final ModelLayerLocation SCAR_LAYER = layer("scar");
    public static final ModelLayerLocation ZIRA_LAYER = layer("zira");

    // Ticket Lion (uses lion mesh)
    public static final ModelLayerLocation TICKET_LION_LAYER = layer("ticket_lion");

    // Interactive entity layers
    public static final ModelLayerLocation RUG_LAYER = layer("rug");

    // Skeletal Hyena Head
    public static final ModelLayerLocation SKELETAL_HYENA_HEAD_LAYER = layer("skeletal_hyena_head");

    // Block entity layers
    public static final ModelLayerLocation HYENA_HEAD_LAYER = layer("hyena_head");

    // Keybinds
    public static final KeyMapping SIMBA_SIT_KEY = new KeyMapping(
            "key.thelionking.simba_sit",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.thelionking"
    );

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
        event.registerLayerDefinition(TERMITE_QUEEN_LAYER, TermiteModel::createBodyLayer);

        // NPC layers (proper models ported from original mod)
        event.registerLayerDefinition(RAFIKI_LAYER, RafikiModel::createBodyLayer);
        event.registerLayerDefinition(SIMBA_LAYER, SimbaModel::createBodyLayer);
        event.registerLayerDefinition(TIMON_LAYER, TimonModel::createBodyLayer);
        event.registerLayerDefinition(PUMBAA_LAYER, PumbaaModel::createBodyLayer);
        event.registerLayerDefinition(SCAR_LAYER, LionModel::createBodyLayer);
        event.registerLayerDefinition(ZIRA_LAYER, OutlanderModel::createBodyLayer);

        // Ticket Lion (lion mesh)
        event.registerLayerDefinition(TICKET_LION_LAYER, LionModel::createBodyLayer);

        // Interactive entity layers
        event.registerLayerDefinition(RUG_LAYER, RugModel::createBodyLayer);

        // Skeletal Hyena Head
        event.registerLayerDefinition(SKELETAL_HYENA_HEAD_LAYER,
                io.github.ron1196.thelionking.client.model.SkeletalHyenaHeadModel::createBodyLayer);

        // Block entity layers
        event.registerLayerDefinition(HYENA_HEAD_LAYER,
                io.github.ron1196.thelionking.client.renderer.HyenaHeadBlockEntityRenderer::createHeadLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Passive
        event.registerEntityRenderer(EntityTypes.LION.get(),
                ctx -> new AnimalRenderer<>(ctx, new LionModel<>(ctx.bakeLayer(LION_LAYER)), "lion", 0.7F));
        event.registerEntityRenderer(EntityTypes.LIONESS.get(),
                ctx -> new AnimalRenderer<>(ctx, new LionModel<>(ctx.bakeLayer(LIONESS_LAYER)), "lioness", 0.6F));
        event.registerEntityRenderer(EntityTypes.ZEBRA.get(),
                ctx -> new AnimalRenderer<>(ctx, new ZebraModel<>(ctx.bakeLayer(ZEBRA_LAYER)), "zebra", 0.7F));
        event.registerEntityRenderer(EntityTypes.GIRAFFE.get(),
                ctx -> new GiraffeRenderer(ctx, new GiraffeModel<>(ctx.bakeLayer(GIRAFFE_LAYER))));
        event.registerEntityRenderer(EntityTypes.RHINO.get(),
                ctx -> new AnimalRenderer<>(ctx, new RhinoModel<>(ctx.bakeLayer(RHINO_LAYER)), "rhino", 0.9F));
        event.registerEntityRenderer(EntityTypes.GEMSBOK.get(),
                ctx -> new AnimalRenderer<>(ctx, new GemsbokModel<>(ctx.bakeLayer(GEMSBOK_LAYER)), "gemsbok", 0.6F));
        event.registerEntityRenderer(EntityTypes.DIKDIK.get(),
                ctx -> new io.github.ron1196.thelionking.client.renderer.DikDikRenderer(ctx, new DikDikModel<>(ctx.bakeLayer(DIKDIK_LAYER)), 0.3F));
        event.registerEntityRenderer(EntityTypes.FLAMINGO.get(),
                ctx -> new AnimalRenderer<>(ctx, new FlamingoModel<>(ctx.bakeLayer(FLAMINGO_LAYER)), "flamingo", 0.3F));
        event.registerEntityRenderer(EntityTypes.ZAZU.get(),
                ctx -> new AnimalRenderer<>(ctx, new ZazuModel<>(ctx.bakeLayer(ZAZU_LAYER)), "zazu", 0.25F));
        event.registerEntityRenderer(EntityTypes.BUG.get(),
                ctx -> new AnimalRenderer<>(ctx, new BugModel<>(ctx.bakeLayer(BUG_LAYER)), "bug", 0.15F));

        // Hostile
        event.registerEntityRenderer(EntityTypes.HYENA.get(),
                ctx -> new HyenaRenderer(ctx, new HyenaModel<>(ctx.bakeLayer(HYENA_LAYER)), 0.5F));
        event.registerEntityRenderer(EntityTypes.SKELETAL_HYENA.get(),
                ctx -> new MobRenderer<>(ctx, new HyenaModel<>(ctx.bakeLayer(SKELETAL_HYENA_LAYER)), "skeletal_hyena", 0.5F));
        event.registerEntityRenderer(EntityTypes.OUTLANDER.get(),
                ctx -> new MobRenderer<>(ctx, new OutlanderModel<>(ctx.bakeLayer(OUTLANDER_LAYER)), "outlander", 0.7F));
        event.registerEntityRenderer(EntityTypes.OUTLANDESS.get(),
                ctx -> new MobRenderer<>(ctx, new OutlanderModel<>(ctx.bakeLayer(OUTLANDESS_LAYER)), "outlandess", 0.6F));
        event.registerEntityRenderer(EntityTypes.VULTURE.get(),
                ctx -> new MobRenderer<>(ctx, new VultureModel<>(ctx.bakeLayer(VULTURE_LAYER)), "vulture", 0.5F));
        event.registerEntityRenderer(EntityTypes.CROCODILE.get(),
                ctx -> new MobRenderer<>(ctx, new CrocodileModel<>(ctx.bakeLayer(CROCODILE_LAYER)), "crocodile", 0.7F));
        event.registerEntityRenderer(EntityTypes.TERMITE.get(),
                ctx -> new ScaledMobRenderer<>(ctx, new TermiteModel<>(ctx.bakeLayer(TERMITE_LAYER)), "termite", 0.15F, 0.4F));
        event.registerEntityRenderer(EntityTypes.TERMITE_QUEEN.get(),
                ctx -> new ScaledMobRenderer<>(ctx, new TermiteModel<>(ctx.bakeLayer(TERMITE_QUEEN_LAYER)), "termite", 0.7F, 1.7F));

        // NPCs — proper models ported from original mod (shadow, scale from old code)
        event.registerEntityRenderer(EntityTypes.RAFIKI.get(),
                ctx -> new NpcRenderer(ctx, new RafikiModel(ctx.bakeLayer(RAFIKI_LAYER)), "rafiki", 0.35F, 0.5F));
        event.registerEntityRenderer(EntityTypes.SIMBA.get(),
                ctx -> new NpcRenderer(ctx, new SimbaModel(ctx.bakeLayer(SIMBA_LAYER)), "simba", 0.5F, 0.5F));
        event.registerEntityRenderer(EntityTypes.TIMON.get(),
                ctx -> new NpcRenderer(ctx, new TimonModel(ctx.bakeLayer(TIMON_LAYER)), "timon", 0.2F, 0.5F));
        event.registerEntityRenderer(EntityTypes.PUMBAA.get(),
                ctx -> new NpcRenderer(ctx, new PumbaaModel(ctx.bakeLayer(PUMBAA_LAYER)), "pumbaa", 0.6F));
        event.registerEntityRenderer(EntityTypes.SCAR.get(),
                ctx -> new NpcRenderer(ctx, new NpcLionModel(ctx.bakeLayer(SCAR_LAYER)), "scar", 0.7F));
        event.registerEntityRenderer(EntityTypes.ZIRA.get(),
                ctx -> new NpcRenderer(ctx, new NpcLionModel(ctx.bakeLayer(ZIRA_LAYER)), "zira", 0.5F, 0.5F));

        // Ticket Lion
        event.registerEntityRenderer(EntityTypes.TICKET_LION.get(),
                ctx -> new NpcRenderer(ctx, new NpcLionModel(ctx.bakeLayer(TICKET_LION_LAYER)), "ticket_lion", 0.7F));

        // Scar Rug
        event.registerEntityRenderer(EntityTypes.RUG.get(),
                ctx -> new RugRenderer(ctx, new RugModel(ctx.bakeLayer(RUG_LAYER))));

        // Skeletal Hyena Head
        event.registerEntityRenderer(EntityTypes.SKELETAL_HYENA_HEAD.get(),
                ctx -> new MobRenderer<>(ctx,
                        new io.github.ron1196.thelionking.client.model.SkeletalHyenaHeadModel(ctx.bakeLayer(SKELETAL_HYENA_HEAD_LAYER)),
                        "hyena_skeleton", 0.3F));

        // Projectiles
        event.registerEntityRenderer(EntityTypes.DART.get(),
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
        event.registerEntityRenderer(EntityTypes.SPEAR.get(),
                ctx -> new net.minecraft.client.renderer.entity.ArrowRenderer<>(ctx) {
                    private final ResourceLocation SPEAR = new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/spear.png");

                    @Override
                    public ResourceLocation getTextureLocation(io.github.ron1196.thelionking.entity.projectile.SpearEntity entity) {
                        return SPEAR;
                    }
                });
        event.registerEntityRenderer(EntityTypes.PUMBAA_BOMB.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.THROWN_TERMITE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.COIN.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypes.ZAZU_EGG.get(), ThrownItemRenderer::new);

        // Weather effects — uses vanilla lightning renderer since LKLightningBoltEntity extends LightningBolt
        event.registerEntityRenderer(EntityTypes.LK_LIGHTNING_BOLT.get(),
                net.minecraft.client.renderer.entity.LightningBoltRenderer::new);

        // Block entity renderers
        event.registerBlockEntityRenderer(io.github.ron1196.thelionking.registry.LKBlockEntityTypes.HYENA_HEAD.get(),
                io.github.ron1196.thelionking.client.renderer.HyenaHeadBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(io.github.ron1196.thelionking.registry.LKBlockEntityTypes.PRIDE_BED.get(),
                io.github.ron1196.thelionking.client.renderer.PrideBedRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SIMBA_SIT_KEY);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(LKMenuTypes.GRINDING_BOWL_MENU.get(), GrindingBowlScreen::new);
            MenuScreens.register(LKMenuTypes.BUG_TRAP_MENU.get(), BugTrapScreen::new);
            MenuScreens.register(LKMenuTypes.BONGO_DRUM_MENU.get(), BongoDrumScreen::new);
            MenuScreens.register(LKMenuTypes.QUIVER_MENU.get(), QuiverScreen::new);
            MenuScreens.register(LKMenuTypes.TIMON_MERCHANT_MENU.get(), TimonMerchantScreen::new);
            MenuScreens.register(LKMenuTypes.SIMBA_INVENTORY_MENU.get(), SimbaInventoryScreen::new);

            // Hyena head item variant property
            net.minecraft.client.renderer.item.ItemProperties.register(
                    io.github.ron1196.thelionking.registry.LKItems.HYENA_HEAD_ITEM.get(),
                    new ResourceLocation(TheLionKingMod.MOD_ID, "hyena_type"),
                    (stack, level, entity, seed) -> {
                        net.minecraft.nbt.CompoundTag tag = stack.getTag();
                        if (tag != null && tag.contains("BlockEntityTag")) {
                            return tag.getCompound("BlockEntityTag").getInt("HyenaType");
                        }
                        return 0.0F;
                    });
        });
    }
}

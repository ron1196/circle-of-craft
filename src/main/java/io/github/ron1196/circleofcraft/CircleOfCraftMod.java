package io.github.ron1196.circleofcraft;

import com.mojang.logging.LogUtils;
import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.network.Networking;
import io.github.ron1196.circleofcraft.registry.*;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.world.structure.StructureTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(CircleOfCraftMod.MOD_ID)
public class CircleOfCraftMod {

    public static final String MOD_ID = "circleofcraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public CircleOfCraftMod() {
        GeckoLib.initialize();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        BlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        EntityTypes.ENTITY_TYPES.register(modEventBus);
        MenuTypes.MENU_TYPES.register(modEventBus);
        Enchantments.ENCHANTMENTS.register(modEventBus);
        Features.FEATURES.register(modEventBus);
        StructureTypes.STRUCTURE_TYPES.register(modEventBus);
        StructureTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
        ParticleTypes.PARTICLE_TYPES.register(modEventBus);
        RecipeTypes.RECIPE_TYPES.register(modEventBus);
        RecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        CreativeTabs.TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Circle of Craft is loading!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Networking.register();
            ModCriteriaTriggers.register();
        });
    }
}

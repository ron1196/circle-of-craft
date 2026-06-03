package io.github.ron1196.circleofcraft;

import com.mojang.logging.LogUtils;
import io.github.ron1196.circleofcraft.network.Networking;
import io.github.ron1196.circleofcraft.registry.*;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.world.structure.StructureTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CircleOfCraftMod.MOD_ID)
public class CircleOfCraftMod {

    public static final String MOD_ID = "circleofcraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public CircleOfCraftMod(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        io.github.ron1196.circleofcraft.item.tier.ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        BlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        EntityTypes.ENTITY_TYPES.register(modEventBus);
        MenuTypes.MENU_TYPES.register(modEventBus);
        Features.FEATURES.register(modEventBus);
        StructureTypes.STRUCTURE_TYPES.register(modEventBus);
        StructureTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
        ParticleTypes.PARTICLE_TYPES.register(modEventBus);
        RecipeTypes.RECIPE_TYPES.register(modEventBus);
        RecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        CreativeTabs.TABS.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModAttachments.ATTACHMENTS.register(modEventBus);
        modEventBus.addListener(Networking::register);

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("Circle of Craft is loading!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            io.github.ron1196.circleofcraft.data.ModCriteriaTriggers.register();
        });
    }
}

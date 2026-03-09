package io.github.ron1196.thelionking;

import com.mojang.logging.LogUtils;
import io.github.ron1196.thelionking.data.LKCriteriaTriggers;
import io.github.ron1196.thelionking.registry.LKBlockEntityTypes;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKCreativeTabs;
import io.github.ron1196.thelionking.registry.LKEnchantments;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKFeatures;
import io.github.ron1196.thelionking.registry.LKItems;
import io.github.ron1196.thelionking.registry.LKMenuTypes;
import io.github.ron1196.thelionking.registry.LKSoundEvents;
import io.github.ron1196.thelionking.world.structure.LKStructureTypes;
import io.github.ron1196.thelionking.network.LKNetworking;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TheLionKingMod.MOD_ID)
public class TheLionKingMod {

    public static final String MOD_ID = "thelionking";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TheLionKingMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LKBlocks.BLOCKS.register(modEventBus);
        LKItems.ITEMS.register(modEventBus);
        LKBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        LKEntityTypes.ENTITY_TYPES.register(modEventBus);
        LKMenuTypes.MENU_TYPES.register(modEventBus);
        LKEnchantments.ENCHANTMENTS.register(modEventBus);
        LKFeatures.FEATURES.register(modEventBus);
        LKStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        LKStructureTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
        LKSoundEvents.SOUND_EVENTS.register(modEventBus);
        LKCreativeTabs.TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("The Lion King Mod is loading!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LKNetworking.register();
            LKCriteriaTriggers.register();
        });
    }
}

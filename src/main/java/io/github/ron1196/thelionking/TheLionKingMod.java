package io.github.ron1196.thelionking;

import com.mojang.logging.LogUtils;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKCreativeTabs;
import io.github.ron1196.thelionking.registry.LKEnchantments;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
        LKEntityTypes.ENTITY_TYPES.register(modEventBus);
        LKEnchantments.ENCHANTMENTS.register(modEventBus);
        LKCreativeTabs.TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("The Lion King Mod is loading!");
    }
}

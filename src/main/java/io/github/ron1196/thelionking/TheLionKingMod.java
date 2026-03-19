package io.github.ron1196.thelionking;

import com.mojang.logging.LogUtils;
import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import io.github.ron1196.thelionking.registry.CreativeTabs;
import io.github.ron1196.thelionking.registry.Enchantments;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.Features;
import io.github.ron1196.thelionking.registry.Items;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.MenuTypes;
import io.github.ron1196.thelionking.registry.ParticleTypes;
import io.github.ron1196.thelionking.registry.SoundEvents;
import io.github.ron1196.thelionking.world.structure.StructureTypes;
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

        LionKingBlocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        BlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        EntityTypes.ENTITY_TYPES.register(modEventBus);
        MenuTypes.MENU_TYPES.register(modEventBus);
        Enchantments.ENCHANTMENTS.register(modEventBus);
        Features.FEATURES.register(modEventBus);
        StructureTypes.STRUCTURE_TYPES.register(modEventBus);
        StructureTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
        SoundEvents.SOUND_EVENTS.register(modEventBus);
        ParticleTypes.PARTICLE_TYPES.register(modEventBus);
        CreativeTabs.TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("The Lion King Mod is loading!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Networking.register();
            LionKingCriteriaTriggers.register();
        });
    }
}

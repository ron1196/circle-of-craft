package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.*;
import io.github.ron1196.thelionking.entity.hostile.*;
import io.github.ron1196.thelionking.entity.npc.*;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LKCommonEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(LKEntityTypes.LION.get(), LionEntity.createAttributes().build());
        event.put(LKEntityTypes.LIONESS.get(), LionessEntity.createAttributes().build());
        event.put(LKEntityTypes.ZEBRA.get(), ZebraEntity.createAttributes().build());
        event.put(LKEntityTypes.GIRAFFE.get(), GiraffeEntity.createAttributes().build());
        event.put(LKEntityTypes.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(LKEntityTypes.GEMSBOK.get(), GemsbokEntity.createAttributes().build());
        event.put(LKEntityTypes.DIKDIK.get(), DikDikEntity.createAttributes().build());
        event.put(LKEntityTypes.FLAMINGO.get(), FlamingoEntity.createAttributes().build());
        event.put(LKEntityTypes.ZAZU.get(), ZazuEntity.createAttributes().build());
        event.put(LKEntityTypes.BUG.get(), BugEntity.createAttributes().build());

        // Hostile entities
        event.put(LKEntityTypes.HYENA.get(), HyenaEntity.createAttributes().build());
        event.put(LKEntityTypes.SKELETAL_HYENA.get(), SkeletalHyenaEntity.createAttributes().build());
        event.put(LKEntityTypes.OUTLANDER.get(), OutlanderEntity.createAttributes().build());
        event.put(LKEntityTypes.OUTLANDESS.get(), OutlanderEntity.createAttributes().build());
        event.put(LKEntityTypes.VULTURE.get(), VultureEntity.createAttributes().build());
        event.put(LKEntityTypes.CROCODILE.get(), CrocodileEntity.createAttributes().build());
        event.put(LKEntityTypes.TERMITE.get(), TermiteEntity.createAttributes().build());

        // Ticket Lion
        event.put(LKEntityTypes.TICKET_LION.get(), io.github.ron1196.thelionking.entity.npc.TicketLionEntity.createAttributes().build());

        // NPC entities
        event.put(LKEntityTypes.RAFIKI.get(), RafikiEntity.createAttributes().build());
        event.put(LKEntityTypes.SIMBA.get(), SimbaEntity.createAttributes().build());
        event.put(LKEntityTypes.TIMON.get(), TimonEntity.createAttributes().build());
        event.put(LKEntityTypes.PUMBAA.get(), PumbaaEntity.createAttributes().build());
        event.put(LKEntityTypes.SCAR.get(), ScarEntity.createAttributes().build());
        event.put(LKEntityTypes.ZIRA.get(), ZiraEntity.createAttributes().build());
    }
}

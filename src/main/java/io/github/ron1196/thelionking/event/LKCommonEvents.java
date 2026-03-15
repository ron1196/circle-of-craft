package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.*;
import io.github.ron1196.thelionking.entity.hostile.*;
import io.github.ron1196.thelionking.entity.npc.*;
import io.github.ron1196.thelionking.registry.EntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LKCommonEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.LION.get(), LionEntity.createAttributes().build());
        event.put(EntityTypes.LIONESS.get(), LionessEntity.createAttributes().build());
        event.put(EntityTypes.ZEBRA.get(), ZebraEntity.createAttributes().build());
        event.put(EntityTypes.GIRAFFE.get(), GiraffeEntity.createAttributes().build());
        event.put(EntityTypes.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(EntityTypes.GEMSBOK.get(), GemsbokEntity.createAttributes().build());
        event.put(EntityTypes.DIKDIK.get(), DikDikEntity.createAttributes().build());
        event.put(EntityTypes.FLAMINGO.get(), FlamingoEntity.createAttributes().build());
        event.put(EntityTypes.ZAZU.get(), ZazuEntity.createAttributes().build());
        event.put(EntityTypes.BUG.get(), BugEntity.createAttributes().build());

        // Hostile entities
        event.put(EntityTypes.HYENA.get(), HyenaEntity.createAttributes().build());
        event.put(EntityTypes.SKELETAL_HYENA.get(), SkeletalHyenaEntity.createAttributes().build());
        event.put(EntityTypes.OUTLANDER.get(), OutlanderEntity.createAttributes().build());
        event.put(EntityTypes.VULTURE.get(), VultureEntity.createAttributes().build());
        event.put(EntityTypes.CROCODILE.get(), CrocodileEntity.createAttributes().build());
        event.put(EntityTypes.TERMITE.get(), TermiteEntity.createAttributes().build());
        event.put(EntityTypes.TERMITE_QUEEN.get(), TermiteQueenEntity.createAttributes().build());

        // Skeletal Hyena Head
        event.put(EntityTypes.SKELETAL_HYENA_HEAD.get(),
                io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaHeadEntity.createAttributes().build());

        // Ticket Lion
        event.put(EntityTypes.TICKET_LION.get(), io.github.ron1196.thelionking.entity.npc.TicketLionEntity.createAttributes().build());

        // NPC entities
        event.put(EntityTypes.RAFIKI.get(), RafikiEntity.createAttributes().build());
        event.put(EntityTypes.SIMBA.get(), SimbaEntity.createAttributes().build());
        event.put(EntityTypes.TIMON.get(), TimonEntity.createAttributes().build());
        event.put(EntityTypes.PUMBAA.get(), PumbaaEntity.createAttributes().build());
        event.put(EntityTypes.SCAR.get(), ScarEntity.createAttributes().build());
        event.put(EntityTypes.ZIRA.get(), ZiraEntity.createAttributes().build());
    }
}

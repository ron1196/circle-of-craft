package io.github.ron1196.circleofcraft.event;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import io.github.ron1196.circleofcraft.network.SimbaSitPacket;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Game bus listener for client key input events. Handles the Simba sit toggle keybind. */
@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientKeyHandler {

    private static final double SEARCH_RANGE = 64.0;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!ClientEvents.SIMBA_SIT_KEY.consumeClick()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return;

        AABB searchBox = player.getBoundingBox().inflate(SEARCH_RANGE);
        List<SimbaEntity> simbas =
                level.getEntitiesOfClass(SimbaEntity.class, searchBox, simba -> simba.isOwnedBy(player));

        for (SimbaEntity simba : simbas) {
            PacketDistributor.sendToServer(new SimbaSitPacket(simba.getId()));
        }
    }
}

package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.entity.LKLightningBoltEntity;
import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import io.github.ron1196.thelionking.quest.LKQuests;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Lion Dust — used on a Star Altar to summon Simba.
 * Spawns a visual lightning bolt, an explosion effect, and a baby Simba.
 */
public class RafikiDustItem extends Item {

    public RafikiDustItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        // Block if Rafiki quest stage 5 is delayed
        if (LKQuests.RAFIKI_QUEST.getQuestStage() == 5 && LKQuests.RAFIKI_QUEST.isDelayed()) {
            return InteractionResult.PASS;
        }

        // Block if player already has a Simba
        LKLevelData data = LKLevelData.get((ServerLevel) level);
        if (data.hasSimba(player)) {
            return InteractionResult.PASS;
        }

        // Must be used on a Star Altar
        if (!level.getBlockState(context.getClickedPos()).is(LKBlocks.STAR_ALTAR.get())) {
            return InteractionResult.PASS;
        }

        double x = context.getClickedPos().getX();
        double y = context.getClickedPos().getY();
        double z = context.getClickedPos().getZ();

        // Consume one dust
        context.getItemInHand().shrink(1);

        // Visual lightning bolt (power 0 = no damage)
        level.addFreshEntity(new LKLightningBoltEntity(level, x, y, z, 0, player));

        // Harmless explosion for particles/sound
        level.explode(player, x, y + 1, z, 0F, Level.ExplosionInteraction.NONE);

        // Spawn baby Simba
        SimbaEntity simba = LKEntityTypes.SIMBA.get().create(level);
        if (simba != null) {
            simba.moveTo(x + 0.5, y + 1, z + 0.5, 0F, 0F);
            simba.setBaby(true);
            simba.setHealth(15.0F);
            level.addFreshEntity(simba);
        }

        // Progress Rafiki quest if at stage 6
        if (LKQuests.RAFIKI_QUEST.getQuestStage() == 6) {
            LKQuests.RAFIKI_QUEST.progress(7);
            broadcastMessage(level, "\u00a7e<Rafiki> \u00a7fYou see? He lives in you! Ohohoho!");
        }

        return InteractionResult.SUCCESS;
    }

    private void broadcastMessage(Level level, String message) {
        for (Player p : level.players()) {
            p.sendSystemMessage(Component.literal(message));
        }
    }
}

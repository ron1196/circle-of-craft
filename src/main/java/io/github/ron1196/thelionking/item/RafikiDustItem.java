package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.PlayerDataProvider;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.quest.stage.StageTrigger;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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

    // Block if player already has a Simba
    PlayerData playerData = PlayerDataProvider.get(player);
    if (playerData.hasSimba()) {
      return InteractionResult.PASS;
    }

    // Must be used on a Star Altar
    if (!level.getBlockState(context.getClickedPos()).is(LionKingBlocks.STAR_ALTAR.get())) {
      return InteractionResult.PASS;
    }

    double x = context.getClickedPos().getX();
    double y = context.getClickedPos().getY();
    double z = context.getClickedPos().getZ();

    // Consume one dust
    context.getItemInHand().shrink(1);

    // Visual lightning bolt (power 0 = no damage)
    level.addFreshEntity(new LightningBoltEntity(level, x, y, z, 0, player));

    // Harmless explosion for particles/sound
    level.explode(player, x, y + 1, z, 0F, Level.ExplosionInteraction.NONE);

    // Spawn baby Simba
    SimbaEntity simba = EntityTypes.SIMBA.get().create(level);
    if (simba != null) {
      simba.moveTo(x + 0.5, y + 1, z + 0.5, 0F, 0F);
      simba.setBaby(true);
      simba.setHealth(15.0F);
      level.addFreshEntity(simba);
      playerData.setHasSimba(true);
    }

    // Progress Rafiki quest via star altar usage
    if (player instanceof ServerPlayer serverPlayer) {
      WorldData data = WorldData.get((ServerLevel) level);
      if (data.getQuestManager().tryAdvance("rafiki", serverPlayer, StageTrigger.STAR_ALTAR_USED)) {
        broadcastMessage(level, "\u00a7e<Rafiki> \u00a7fYou see? He lives in you! Ohohoho!");
      }
    }

    return InteractionResult.SUCCESS;
  }

  private void broadcastMessage(Level level, String message) {
    for (Player p : level.players()) {
      p.sendSystemMessage(Component.literal(message));
    }
  }
}

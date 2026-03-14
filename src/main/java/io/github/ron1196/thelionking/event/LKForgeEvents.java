package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.command.LKCommands;
import io.github.ron1196.thelionking.data.LKPlayerData;
import io.github.ron1196.thelionking.data.LKPlayerDataProvider;
import io.github.ron1196.thelionking.data.LKWorldData;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.entity.hostile.HyenaEntity;
import io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaEntity;
import io.github.ron1196.thelionking.entity.npc.RafikiEntity;
import io.github.ron1196.thelionking.entity.npc.TicketLionEntity;
import io.github.ron1196.thelionking.entity.npc.TimonEntity;
import io.github.ron1196.thelionking.entity.npc.ZiraEntity;
import io.github.ron1196.thelionking.entity.RugEntity;
import io.github.ron1196.thelionking.network.LKNetworking;
import io.github.ron1196.thelionking.network.LoginSyncPacket;
import io.github.ron1196.thelionking.registry.LKEnchantments;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LKItems;
import io.github.ron1196.thelionking.world.dimension.LKDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LKForgeEvents {

    // ── Commands ──────────────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LKCommands.register(event.getDispatcher());
    }

    // ── Player Login — sync world state and quest data to client ───────────────

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ServerLevel overworld = serverPlayer.server.overworld();
            LKWorldData worldData = LKWorldData.get(overworld);
            LKPlayerData playerData = LKPlayerDataProvider.get(serverPlayer);
            LKNetworking.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new LoginSyncPacket(worldData, playerData)
            );
        }
    }

    // ── AttackEntityEvent (punch Scar Rug to pick it up) ───────────────────────

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof RugEntity rug) {
            rug.dropAsItem();
        }
    }

    // ── LivingHurtEvent ─────────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        // Scourge of Hyenas enchantment bonus damage
        if (attacker instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            int scourgeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    LKEnchantments.SCOURGE_OF_HYENAS.get(), weapon);

            if (scourgeLevel > 0 && (target instanceof HyenaEntity || target instanceof SkeletalHyenaEntity)) {
                event.setAmount(event.getAmount() + 2.5F * scourgeLevel);
            }
        }

        // Peacock boots negate fall damage
        if (event.getSource().is(DamageTypes.FALL)) {
            ItemStack boots = target.getItemBySlot(EquipmentSlot.FEET);
            if (boots.is(LKItems.PEACOCK_BOOTS.get())) {
                event.setCanceled(true);
            }
        }
    }

    // ── LivingDeathEvent ────────────────────────────────────────────────────────

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Entity killer = event.getSource().getEntity();

        // Hyena special drop: hyena head with looting
        if (entity instanceof HyenaEntity && killer instanceof Player player) {
            int lootingLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    Enchantments.MOB_LOOTING, player.getMainHandItem());

            float dropChance = 0.05F + 0.03F * lootingLevel;
            if (entity.level().random.nextFloat() < dropChance) {
                entity.spawnAtLocation(new ItemStack(LKItems.HYENA_HEAD_ITEM.get()));
            }
        }
    }

    // ── PlayerInteractEvent.EntityInteract ──────────────────────────────────────

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (player.level().isClientSide()) {
            return;
        }

        if (target instanceof RafikiEntity) {
            // Quest book giving and dialogue handled in RafikiEntity.mobInteract()
        } else if (target instanceof TimonEntity) {
            // TODO: Placeholder for Timon trading — tracked in docs/TODO_WORKAROUNDS.md
            player.sendSystemMessage(Component.literal("Timon is ready to trade with you!"));
        } else if (target instanceof TicketLionEntity) {
            player.sendSystemMessage(Component.literal("The Ticket Lion can sell you passage to the Pride Lands!"));
        }
    }

    // ── TickEvent.PlayerTickEvent ────────────────────────────────────────────────

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.player.level().isClientSide()) {
            return;
        }

        Player player = event.player;

        // Check if player is in the Pride Lands dimension
        if (player.level().dimension() == LKDimensions.PRIDE_LANDS_LEVEL) {
            // Dimension-entry logic can be added here
        }
    }

    // ── TickEvent.LevelTickEvent ────────────────────────────────────────────────

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!(event.level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Save level data every 100 ticks if dirty
        if (serverLevel.getGameTime() % 100 == 0) {
            LKWorldData data = LKWorldData.get(serverLevel);
            if (data.isDirty()) {
                data.setDirty();
            }
        }

        // Outlands: Zira stage 22 — spawn Zira with dramatic lightning when player is on surface
        if (serverLevel.dimension() == LKDimensions.OUTLANDS_LEVEL) {
            handleZiraSpawnEvent(serverLevel);
        }
    }

    /**
     * When ziraStage == 22 and a player is on the surface of the Outlands,
     * spawn Zira nearby with a visual lightning bolt.
     */
    private static void handleZiraSpawnEvent(ServerLevel level) {
        LKWorldData data = LKWorldData.get(level);
        if (data.ziraStage != 22) return;
        if (level.players().isEmpty()) return;

        Player player = level.players().get(0);
        int px = Mth.floor(player.getX());
        int py = Mth.floor(player.getBoundingBox().minY);
        int pz = Mth.floor(player.getZ());

        // Player must be on the surface (can see sky and at heightmap level)
        int surfaceY = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(px, 0, pz)).getY();
        if (!level.canSeeSky(new BlockPos(px, py, pz)) || py != surfaceY) return;

        // Spawn Zira at a random nearby position
        int spawnX = px - 8 + level.random.nextInt(17);
        int spawnZ = pz - 8 + level.random.nextInt(17);
        int spawnY = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(spawnX, 0, spawnZ)).getY();

        ZiraEntity zira = EntityTypes.ZIRA.get().create(level);
        if (zira != null) {
            zira.moveTo(spawnX, spawnY, spawnZ, 0.0F, 0.0F);
            zira.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ(), 10.0F, 40.0F);
            level.addFreshEntity(zira);

            // Visual lightning bolt at Zira's spawn position
            level.addFreshEntity(new LightningBoltEntity(level, spawnX, spawnY, spawnZ, 0, player));

            data.ziraStage = 23;
            data.setDirty();
        }
    }
}

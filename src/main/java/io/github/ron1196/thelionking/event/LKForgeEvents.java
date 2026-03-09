package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.entity.hostile.HyenaEntity;
import io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaEntity;
import io.github.ron1196.thelionking.entity.npc.RafikiEntity;
import io.github.ron1196.thelionking.entity.npc.TicketLionEntity;
import io.github.ron1196.thelionking.entity.npc.TimonEntity;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import io.github.ron1196.thelionking.registry.LKEnchantments;
import io.github.ron1196.thelionking.registry.LKItems;
import io.github.ron1196.thelionking.world.dimension.LKDimensions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LKForgeEvents {

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
            // Give quest book if the player doesn't already have one
            boolean hasQuestBook = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).is(LKItems.QUEST_BOOK.get())) {
                    hasQuestBook = true;
                    break;
                }
            }
            if (!hasQuestBook) {
                player.getInventory().add(new ItemStack(LKItems.QUEST_BOOK.get()));
            }
            player.sendSystemMessage(Component.literal("Rafiki greets you!"));
        } else if (target instanceof TimonEntity) {
            // TODO: Placeholder for Timon trading — tracked in TODO_WORKAROUNDS.md
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

        // Update quests every 20 ticks (1 second)
        if (player.tickCount % 20 == 0) {
            LKQuestBase.updateAllQuests();
        }

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
            LKLevelData data = LKLevelData.get(serverLevel);
            if (data.isDirty()) {
                data.setDirty();
            }
        }
    }
}

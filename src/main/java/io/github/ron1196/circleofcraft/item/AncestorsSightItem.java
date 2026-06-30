package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.entity.npc.ScarEntity;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

/**
 * Ancestors' Sight — Rafiki's gourd brew. Drinking it lets the great kings reveal Scar: if he is
 * within reach he is outlined (Glowing) through the terrain, the way Mufasa appears in the clouds to
 * guide Simba. The vision blurs (brief nausea) as the spirits depart.
 *
 * <p>Reveal is range-limited so it stays a "get close first" tool — the roar homes you in, this
 * pinpoints him through the rock.
 */
public class AncestorsSightItem extends Item {

    private static final int DRINK_DURATION = 32;
    private static final double REVEAL_RADIUS = 128.0;
    private static final int GLOW_TICKS = 600;
    private static final int FADE_TICKS = 40;

    public AncestorsSightItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            reveal(player);
        }
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }

    private void reveal(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        AABB box = player.getBoundingBox().inflate(REVEAL_RADIUS);
        List<ScarEntity> scars = level.getEntitiesOfClass(ScarEntity.class, box, ScarEntity::isAlive);

        for (ScarEntity scar : scars) {
            scar.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_TICKS, 0, false, false));
        }
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, FADE_TICKS, 0, false, false));

        ChatHelper.sendNpcMessage(
                player,
                "Rafiki",
                scars.isEmpty()
                        ? "De great kings see no danger near you... go closer, and drink again."
                        : "De great kings light your path! Remember who you are — and find dat coward!");
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.7F, 1.4F);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return DRINK_DURATION;
    }
}

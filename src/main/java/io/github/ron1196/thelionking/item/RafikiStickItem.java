package io.github.ron1196.thelionking.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.registry.LKEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Rafiki's Stick — quest reward item.
 * - Right-click saplings/crops to grow them (like bone meal)
 * - Right-click grass/sand to spread vegetation (in LK dimensions)
 * - Hold right-click (bow-style) with Rafiki Thunder enchantment to aim, release to strike lightning
 * - 5 attack damage, can shear leaves, uncommon rarity
 */
public class RafikiStickItem extends Item {

    private static final int MAX_DAMAGE = 850;
    private static final String TAG_THUNDER_COOLDOWN = "ThunderCooldown";
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public RafikiStickItem(Properties properties) {
        super(properties.stacksTo(1).durability(MAX_DAMAGE).rarity(Rarity.UNCOMMON));

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0D, AttributeModifier.Operation.ADDITION)
        );
        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION)
        );
        this.defaultModifiers = builder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return false;
    }

    // ── Right-click on block: grow saplings, crops, spread vegetation ──

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        // Grow saplings and crops (anything bonemealable)
        if (block instanceof BonemealableBlock bonemealable) {
            if (bonemealable.isValidBonemealTarget(level, pos, state, false)) {
                if (bonemealable.isBonemealSuccess(level, level.random, pos, state)) {
                    bonemealable.performBonemeal((net.minecraft.server.level.ServerLevel) level, level.random, pos, state);
                }
                damageRafikiStick(stack, 4, player);
                return InteractionResult.SUCCESS;
            }
        }

        // Spread vegetation on grass blocks
        if (block == Blocks.GRASS_BLOCK) {
            net.minecraft.world.item.BoneMealItem.growCrop(stack, level, pos);
            damageRafikiStick(stack, 3, player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    // ── Right-click in air: start charging thunder ──

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        int thunderLevel = EnchantmentHelper.getItemEnchantmentLevel(LKEnchantments.RAFIKI_THUNDER.get(), stack);
        if (thunderLevel <= 0) {
            return InteractionResultHolder.pass(stack);
        }

        // Cooldown stored in NBT (ticks remaining)
        int cooldown = stack.getOrCreateTag().getInt(TAG_THUNDER_COOLDOWN);
        if (cooldown > 0) {
            return InteractionResultHolder.pass(stack);
        }

        // Only start charging if not looking at a nearby block (close-range is useOn)
        HitResult hit = player.pick(5.0D, 1.0F, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        int thunderLevel = EnchantmentHelper.getItemEnchantmentLevel(LKEnchantments.RAFIKI_THUNDER.get(), stack);
        if (thunderLevel <= 0) return;

        // Range increases with power: 2 + 4^(level+1)
        double range = 2.0D + Math.pow(4, thunderLevel + 1);
        HitResult farHit = player.pick(range, 1.0F, false);
        HitResult nearHit = player.pick(5.0D, 1.0F, false);

        // Must aim at a distant block (not close-range)
        if (farHit instanceof BlockHitResult blockHit && farHit.getType() == HitResult.Type.BLOCK && nearHit.getType() != HitResult.Type.BLOCK) {
            BlockPos target = blockHit.getBlockPos();

            if (level.getBlockState(target).canOcclude() && level.isEmptyBlock(target.above())) {
                if (!level.isClientSide) {
                    level.addFreshEntity(new LightningBoltEntity(level,
                            target.getX(), target.getY(), target.getZ(), thunderLevel, player));
                }
                damageRafikiStick(stack, 10, player);
                stack.getOrCreateTag().putInt(TAG_THUNDER_COOLDOWN, 12);
                return;
            }
        }

        // Failed to aim — show smoke particles
        for (int i = 0; i < 7; i++) {
            double dx = level.random.nextGaussian() * 0.02D;
            double dy = level.random.nextGaussian() * 0.02D;
            double dz = level.random.nextGaussian() * 0.02D;
            level.addParticle(
                    ParticleTypes.SMOKE,
                    player.getX() + (level.random.nextFloat() * player.getBbWidth() * 2.0F) - player.getBbWidth(),
                    player.getY() - 0.5D + (level.random.nextFloat() * (player.getBbHeight() / 2)),
                    player.getZ() + (level.random.nextFloat() * player.getBbWidth() * 2.0F) - player.getBbWidth(),
                    dx, dy, dz
            );
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull net.minecraft.world.entity.Entity entity,
                              int slotId, boolean isSelected) {
        if (!level.isClientSide && stack.hasTag()) {
            int cooldown = stack.getTag().getInt(TAG_THUNDER_COOLDOWN);
            if (cooldown > 0) {
                stack.getTag().putInt(TAG_THUNDER_COOLDOWN, cooldown - 1);
            }
        }
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    // ── Combat ──

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        damageRafikiStick(stack, 2, attacker);
        return true;
    }

    // ── Leaves shearing speed bonus ──

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        if (state.is(net.minecraft.tags.BlockTags.LEAVES) || state.getBlock() == Blocks.TALL_GRASS
                || state.getBlock() == Blocks.DEAD_BUSH) {
            return 15.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(
            @NotNull ItemStack stack,
            @NotNull Level level,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull LivingEntity entity
    ) {
        if (state.is(net.minecraft.tags.BlockTags.LEAVES)) {
            damageRafikiStick(stack, 1, entity);
            return true;
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    // ── Custom durability with Rafiki Durability enchantment ──

    private void damageRafikiStick(ItemStack stack, int amount, LivingEntity entity) {
        if (!stack.isDamageableItem()) return;

        if (amount > 0 && entity instanceof Player) {
            int durabilityLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    LKEnchantments.RAFIKI_DURABILITY.get(), stack);
            if (durabilityLevel > 0 && entity.level().random.nextInt(durabilityLevel + 1) > 0) {
                return; // Durability enchantment prevented damage
            }
        }

        stack.hurtAndBreak(amount, entity, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

}

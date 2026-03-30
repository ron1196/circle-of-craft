package io.github.ron1196.thelionking.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.npc.ScarEntity;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline;
import io.github.ron1196.thelionking.registry.Enchantments;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Rafiki's Stick — quest reward item. - Right-click saplings/crops to grow them (like bone meal) -
 * Right-click grass/sand to spread vegetation (in LK dimensions) - Hold right-click (bow-style)
 * with Rafiki Thunder enchantment to aim, release to strike lightning - 5 attack damage, can shear
 * leaves, uncommon rarity
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
                new AttributeModifier(
                        BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0D, AttributeModifier.Operation.ADDITION));
        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            @NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return false;
    }

    // ── Right-click on block: grow saplings, crops, spread vegetation ──

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        // Grow saplings and crops (anything bonemealable)
        if (block instanceof BonemealableBlock bonemealable) {
            if (bonemealable.isValidBonemealTarget(level, pos, state, false)) {
                if (!level.isClientSide) {
                    if (bonemealable.isBonemealSuccess(level, level.random, pos, state)) {
                        bonemealable.performBonemeal((ServerLevel) level, level.random, pos, state);
                    }
                    damageRafikiStick(stack, 4, player);
                }
                return InteractionResult.SUCCESS;
            }
        }

        // Spread vegetation on grass blocks (GrassBlock implements BonemealableBlock)
        if (block instanceof BonemealableBlock grassBonemealable && block == Blocks.GRASS_BLOCK) {
            if (level instanceof ServerLevel serverLevel) {
                BlockState grassState = level.getBlockState(pos);
                if (grassBonemealable.isBonemealSuccess(serverLevel, serverLevel.random, pos, grassState)) {
                    grassBonemealable.performBonemeal(serverLevel, serverLevel.random, pos, grassState);
                }
                damageRafikiStick(stack, 3, player);
            }
            return InteractionResult.SUCCESS;
        }

        // Non-special block — start thunder charge if enchanted
        int thunderLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.RAFIKI_THUNDER.get(), stack);
        int cooldown = stack.getOrCreateTag().getInt(TAG_THUNDER_COOLDOWN);
        if (thunderLevel > 0 && cooldown <= 0) {
            player.startUsingItem(context.getHand());
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    // ── Right-click in air: start charging thunder ──

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        int thunderLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.RAFIKI_THUNDER.get(), stack);
        if (thunderLevel <= 0) {
            return InteractionResultHolder.pass(stack);
        }

        int cooldown = stack.getOrCreateTag().getInt(TAG_THUNDER_COOLDOWN);
        if (cooldown > 0) {
            return InteractionResultHolder.pass(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        int thunderLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.RAFIKI_THUNDER.get(), stack);
        if (thunderLevel <= 0) return;

        double range = 2.0D + Math.pow(4, thunderLevel + 1);

        // Check for entity hit first
        Vec3 eyePos = player.getEyePosition();
        Vec3 endPos = eyePos.add(player.getLookAngle().scale(range));
        AABB searchBox = player.getBoundingBox()
                .expandTowards(player.getLookAngle().scale(range))
                .inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player,
                eyePos,
                endPos,
                searchBox,
                e -> !e.isSpectator() && e.isPickable() && e != player,
                range * range);

        if (entityHit != null) {
            Entity target = entityHit.getEntity();
            if (!level.isClientSide) {
                level.addFreshEntity(new LightningBoltEntity(
                        level, target.getX(), target.getY(), target.getZ(), thunderLevel, player));
            }
            damageRafikiStick(stack, 10, player);
            stack.getOrCreateTag().putInt(TAG_THUNDER_COOLDOWN, 12);
            return;
        }

        // Fall back to block hit
        HitResult farHit = player.pick(range, 1.0F, false);

        if (farHit instanceof BlockHitResult blockHit) {
            BlockPos target = blockHit.getBlockPos();
            if (!level.isClientSide) {
                level.addFreshEntity(new LightningBoltEntity(
                        level, target.getX(), target.getY(), target.getZ(), thunderLevel, player));
            }
            damageRafikiStick(stack, 10, player);
            stack.getOrCreateTag().putInt(TAG_THUNDER_COOLDOWN, 12);
            return;
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
                    dx,
                    dy,
                    dz);
        }
    }

    private static final double SCAR_DETECT_RANGE = 250.0;
    private static final int SCAR_HINT_INTERVAL = 60;
    private static final double SCAR_NEAR_DISTANCE = 100.0;
    private static final double SCAR_CLOSE_DISTANCE = 50.0;
    private static final double SCAR_VERY_CLOSE_DISTANCE = 25.0;

    @Override
    public void inventoryTick(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;

        // Thunder cooldown
        if (stack.hasTag()) {
            int cooldown = stack.getOrCreateTag().getInt(TAG_THUNDER_COOLDOWN);
            if (cooldown > 0) {
                stack.getOrCreateTag().putInt(TAG_THUNDER_COOLDOWN, cooldown - 1);
            }
        }

        // Scar tracking — only when held in hand during DEFEAT_SCAR quest stage
        if (!isSelected || !(entity instanceof Player player)) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (entity.tickCount % SCAR_HINT_INTERVAL != 0) return;

        QuestlineManager quests = WorldData.get(serverLevel).getQuestManager();
        RafikiQuestline.Stage stage = quests.getStage("rafiki", RafikiQuestline.Stage.class);
        if (stage != RafikiQuestline.Stage.DEFEAT_SCAR) return;

        List<ScarEntity> scars = level.getEntitiesOfClass(
                ScarEntity.class, entity.getBoundingBox().inflate(SCAR_DETECT_RANGE));
        if (scars.isEmpty()) return;

        ScarEntity scar = scars.get(0);
        double distance = entity.distanceTo(scar);

        String message;
        if (distance < SCAR_VERY_CLOSE_DISTANCE) {
            message = "§c§lThe stick shakes wildly! Scar is right here!";
        } else if (distance < SCAR_CLOSE_DISTANCE) {
            message = "§6§lThe stick shakes violently! Scar is very close!";
        } else if (distance < SCAR_NEAR_DISTANCE) {
            message = "§6The stick trembles strongly...";
        } else {
            message = "§7The stick trembles faintly...";
        }
        player.displayClientMessage(net.minecraft.network.chat.Component.literal(message), true);

        // Set glint tag for nearby Scar
        stack.getOrCreateTag().putBoolean(TAG_SCAR_NEARBY, distance < SCAR_NEAR_DISTANCE);
    }

    private static final String TAG_SCAR_NEARBY = "ScarNearby";

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        if (super.isFoil(stack)) return true;
        return stack.hasTag() && stack.getOrCreateTag().getBoolean(TAG_SCAR_NEARBY);
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
        if (state.is(BlockTags.LEAVES)
                || state.getBlock() == Blocks.TALL_GRASS
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
            @NotNull LivingEntity entity) {
        if (state.is(BlockTags.LEAVES)) {
            damageRafikiStick(stack, 1, entity);
            return true;
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    // ── Custom durability with Rafiki Durability enchantment ──

    private void damageRafikiStick(ItemStack stack, int amount, LivingEntity entity) {
        if (!stack.isDamageableItem()) return;

        if (amount > 0 && entity instanceof Player) {
            int durabilityLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.RAFIKI_DURABILITY.get(), stack);
            if (durabilityLevel > 0 && entity.level().random.nextInt(durabilityLevel + 1) > 0) {
                return; // Durability enchantment prevented damage
            }
        }

        stack.hurtAndBreak(amount, entity, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }
}

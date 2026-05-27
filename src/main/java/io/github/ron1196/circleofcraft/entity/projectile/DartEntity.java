package io.github.ron1196.circleofcraft.entity.projectile;

import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class DartEntity extends AbstractArrow implements net.minecraft.world.entity.projectile.ItemSupplier {

    private static final EntityDataAccessor<Integer> DATA_DART_TYPE =
            SynchedEntityData.defineId(DartEntity.class, EntityDataSerializers.INT);

    private boolean silverShooter = false;
    private int stuckTicks = 0;

    public enum DartType {
        BLUE(0, 7.0F),
        RED(1, 6.0F),
        YELLOW(2, 8.0F),
        PINK(3, 7.0F),
        BLACK(4, 7.0F);

        private final int id;
        private final float damage;

        DartType(int id, float damage) {
            this.id = id;
            this.damage = damage;
        }

        public int getId() {
            return id;
        }

        public float getDamage() {
            return damage;
        }

        public static DartType byId(int id) {
            for (DartType type : values()) {
                if (type.id == id) return type;
            }
            return BLUE;
        }
    }

    // Required constructor for EntityType.Builder.of()
    public DartEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    // Convenience constructor for spawning with shooter and type
    public DartEntity(Level level, LivingEntity shooter, DartType dartType) {
        super(EntityTypes.DART.get(), shooter, level);
        setDartType(dartType);
        setBaseDamage(dartType.getDamage());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DART_TYPE, DartType.BLUE.getId());
    }

    public DartType getDartType() {
        return DartType.byId(this.entityData.get(DATA_DART_TYPE));
    }

    public void setDartType(DartType type) {
        this.entityData.set(DATA_DART_TYPE, type.getId());
    }

    public boolean isSilverShooter() {
        return silverShooter;
    }

    public void setSilverShooter(boolean silverShooter) {
        this.silverShooter = silverShooter;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        DartType type = getDartType();
        setBaseDamage(type.getDamage());

        super.onHitEntity(result);

        if (result.getEntity() instanceof LivingEntity target) {
            switch (type) {
                case RED -> {
                    int fireDuration = silverShooter ? 5 : 3;
                    target.setSecondsOnFire(fireDuration);
                }
                case YELLOW -> {
                    // Knockback is applied via the base arrow logic, but we add extra
                    double knockbackStrength = 0.6;
                    target.push(
                            -Math.sin(Math.toRadians(this.getYRot())) * knockbackStrength,
                            0.1,
                            Math.cos(Math.toRadians(this.getYRot())) * knockbackStrength);
                }
                case PINK -> {
                    if (this.getOwner() instanceof LivingEntity shooter) {
                        float healAmount = silverShooter ? 2.0F : 1.0F;
                        float chance = 0.5F;
                        if (this.random.nextFloat() < chance) {
                            shooter.heal(healAmount);
                        }
                    }
                }
                case BLACK -> {
                    if (!this.level().isClientSide) {
                        float explosionRadius = silverShooter ? 4.0F : 3.0F;
                        this.level()
                                .explode(
                                        this,
                                        this.getX(),
                                        this.getY(),
                                        this.getZ(),
                                        explosionRadius,
                                        Level.ExplosionInteraction.MOB);
                    }
                }
                default -> {
                    // BLUE: no special effect beyond damage
                }
            }
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        stuckTicks = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround) {
            stuckTicks++;
            if (stuckTicks >= 1200) {
                this.discard();
            }
        }
    }

    @Override
    public @NotNull ItemStack getItem() {
        return getPickupItem();
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return switch (getDartType()) {
            case BLUE -> new ItemStack(ModItems.DART_BLUE.get());
            case RED -> new ItemStack(ModItems.DART_RED.get());
            case YELLOW -> new ItemStack(ModItems.DART_YELLOW.get());
            case PINK -> new ItemStack(ModItems.DART_PINK.get());
            case BLACK -> new ItemStack(ModItems.DART_BLACK.get());
        };
    }
}

package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.data.LKCriteriaTriggers;
import io.github.ron1196.thelionking.entity.ai.AmbientPanicGoal;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GiraffeEntity extends LionKingAnimal {

    private static final EntityDataAccessor<Boolean> DATA_SADDLED = SynchedEntityData.defineId(
            GiraffeEntity.class,
            EntityDataSerializers.BOOLEAN
    );
    private static final EntityDataAccessor<Integer> DATA_TIE = SynchedEntityData.defineId(
            GiraffeEntity.class,
            EntityDataSerializers.INT
    );

    private static final int NO_TIE = -1;

    public enum TieColor {
        BASE(0, LKItems.GIRAFFE_TIE),
        WHITE(1, LKItems.GIRAFFE_TIE_WHITE),
        BLUE(2, LKItems.GIRAFFE_TIE_BLUE),
        YELLOW(3, LKItems.GIRAFFE_TIE_YELLOW),
        RED(4, LKItems.GIRAFFE_TIE_RED),
        PURPLE(5, LKItems.GIRAFFE_TIE_PURPLE),
        GREEN(6, LKItems.GIRAFFE_TIE_GREEN),
        BLACK(7, LKItems.GIRAFFE_TIE_BLACK);

        private final int id;
        private final Supplier<Item> item;

        TieColor(int id, Supplier<Item> item) {
            this.id = id;
            this.item = item;
        }

        public int getId() {
            return id;
        }

        public Item getItem() {
            return item.get();
        }

        public static @Nullable TieColor fromId(int id) {
            for (TieColor color : values()) {
                if (color.id == id) return color;
            }
            return null;
        }

        public static @Nullable TieColor fromItem(Item item) {
            for (TieColor color : values()) {
                if (color.getItem() == item) return color;
            }
            return null;
        }
    }

    public GiraffeEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLED, false);
        this.entityData.define(DATA_TIE, NO_TIE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AmbientPanicGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(
                this,
                LivingEntity.class,
                12.0F, 1.0D, 1.5D,
                e -> e instanceof LionEntity || e instanceof LionessEntity)
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LionKingAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    // ── Saddle & Tie ──

    public boolean isSaddled() {
        return this.entityData.get(DATA_SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(DATA_SADDLED, saddled);
    }

    public int getTie() {
        return this.entityData.get(DATA_TIE);
    }

    public void setTie(int tie) {
        this.entityData.set(DATA_TIE, tie);
    }

    public @Nullable TieColor getTieColor() {
        return TieColor.fromId(getTie());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Apply giraffe saddle
        if (stack.is(LKItems.GIRAFFE_SADDLE.get()) && !isSaddled() && !isBaby()) {
            setSaddled(true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                LKCriteriaTriggers.RIDE_GIRAFFE.trigger(serverPlayer);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        // Apply tie (must be saddled, adult, no existing tie)
        if (!isBaby() && isSaddled() && getTie() == NO_TIE) {
            TieColor tieColor = TieColor.fromItem(stack.getItem());
            if (tieColor == null) {
                return super.mobInteract(player, hand);
            }
            setTie(tieColor.getId());
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 0.5F, 1.0F);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        // Saddled giraffes skip quest interactions (they're a mount now)
        if (isSaddled()) {
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    // ── Save/Load ──

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", isSaddled());
        tag.putInt("Tie", getTie());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("Saddled"));
        setTie(tag.getInt("Tie"));
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(LKItems.GIRAFFE_SADDLE.get(), 1);
    }
}

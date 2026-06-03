package io.github.ron1196.circleofcraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UseGrindingBowlTrigger extends SimpleCriterionTrigger<UseGrindingBowlTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack output) {
        trigger(player, instance -> instance.matches(output));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(in -> in.group(
                        EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item))
                .apply(in, TriggerInstance::new));

        public boolean matches(ItemStack stack) {
            return item.isEmpty() || item.get().test(stack);
        }
    }
}

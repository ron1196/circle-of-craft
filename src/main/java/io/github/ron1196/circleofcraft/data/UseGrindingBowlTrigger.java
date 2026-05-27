package io.github.ron1196.circleofcraft.data;

import com.google.gson.JsonObject;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UseGrindingBowlTrigger extends SimpleCriterionTrigger<UseGrindingBowlTrigger.TriggerInstance> {

    private static final ResourceLocation ID = CircleOfCraftMod.id("use_grinding_bowl");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public @NotNull TriggerInstance createInstance(
            JsonObject json, @NotNull ContextAwarePredicate player, @NotNull DeserializationContext context) {
        ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
        return new TriggerInstance(player, item);
    }

    public void trigger(ServerPlayer player, ItemStack output) {
        trigger(player, instance -> instance.matches(output));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate player, ItemPredicate item) {
            super(ID, player);
            this.item = item;
        }

        public boolean matches(ItemStack stack) {
            return item.matches(stack);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            json.add("item", item.serializeToJson());
            return json;
        }
    }
}

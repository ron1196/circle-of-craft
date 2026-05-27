package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.entity.GrindingBowlBlockEntity;
import io.github.ron1196.circleofcraft.recipe.GrindingBowlRecipe;
import io.github.ron1196.circleofcraft.registry.RecipeTypes;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GrindingBowlProvider implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation UID = CircleOfCraftMod.id("grinding_bowl");

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull BlockAccessor accessor, @NotNull IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof GrindingBowlBlockEntity bowl)) {
            return;
        }
        ItemStack input = bowl.getInputItem();
        if (input.isEmpty()) {
            tooltip.add(Component.translatable("jade.circleofcraft.grinding_bowl.empty")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }
        tooltip.add(Component.translatable("jade.circleofcraft.grinding_bowl.input", input.getHoverName()));

        Optional<GrindingBowlRecipe> recipe = accessor.getLevel()
                .getRecipeManager()
                .getRecipeFor(RecipeTypes.GRINDING_TYPE.get(), new SimpleContainer(input), accessor.getLevel());
        recipe.ifPresent(r -> tooltip.add(Component.translatable(
                "jade.circleofcraft.grinding_bowl.output", r.getResult().getHoverName())));

        if (bowl.isGrinding()) {
            int pct = Math.round(bowl.getGrindProgress() * 100);
            tooltip.add(Component.translatable("jade.circleofcraft.grinding_bowl.progress", pct));
        }
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }
}

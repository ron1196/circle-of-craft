package io.github.ron1196.thelionking.compat.jei;

import io.github.ron1196.thelionking.recipe.GrindingBowlRecipe;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.RecipeTypes;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class LionKingJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation("thelionking", "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(
                new GrindingBowlRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration reg) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        RecipeManager rm = level.getRecipeManager();
        List<GrindingBowlRecipe> recipes = rm.getAllRecipesFor(RecipeTypes.GRINDING_TYPE.get());
        reg.addRecipes(GrindingBowlRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(
                new ItemStack(LionKingItems.GRINDING_BOWL_ITEM.get()), GrindingBowlRecipeCategory.RECIPE_TYPE);
    }
}

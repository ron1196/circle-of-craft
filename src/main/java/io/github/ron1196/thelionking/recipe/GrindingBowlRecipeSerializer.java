package io.github.ron1196.thelionking.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrindingBowlRecipeSerializer implements RecipeSerializer<GrindingBowlRecipe> {

    @Override
    public @NotNull GrindingBowlRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        return new GrindingBowlRecipe(recipeId, ingredient, result);
    }

    @Override
    public @Nullable GrindingBowlRecipe fromNetwork(
            @NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        return new GrindingBowlRecipe(recipeId, ingredient, result);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull GrindingBowlRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getResult());
    }
}

package io.github.ron1196.thelionking.compat.jei;

import io.github.ron1196.thelionking.recipe.GrindingBowlRecipe;
import io.github.ron1196.thelionking.registry.LionKingItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GrindingBowlRecipeCategory implements IRecipeCategory<GrindingBowlRecipe> {

    public static final RecipeType<GrindingBowlRecipe> RECIPE_TYPE =
            RecipeType.create("thelionking", "grinding_bowl", GrindingBowlRecipe.class);

    private static final ResourceLocation FURNACE_TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    private static final int WIDTH = 82;
    private static final int HEIGHT = 26;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;

    public GrindingBowlRecipeCategory(@NotNull IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(LionKingItems.GRINDING_BOWL_ITEM.get()));
        this.arrow = guiHelper
                .drawableBuilder(FURNACE_TEXTURE, 79, 35, 24, 17)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public @NotNull RecipeType<GrindingBowlRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.thelionking.category.grinding_bowl");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder, @NotNull GrindingBowlRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(
            @NotNull GrindingBowlRecipe recipe,
            @NotNull IRecipeSlotsView slotsView,
            @NotNull GuiGraphics graphics,
            double mouseX,
            double mouseY) {
        arrow.draw(graphics, 28, 4);
    }
}

package io.github.ron1196.circleofcraft.compat.jei;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.registry.ModItems;
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

public class RafikiTradeRecipeCategory implements IRecipeCategory<NpcTradeRecipe> {

    public static final RecipeType<NpcTradeRecipe> RECIPE_TYPE =
            RecipeType.create(CircleOfCraftMod.MOD_ID, "rafiki_trade", NpcTradeRecipe.class);

    private static final ResourceLocation FURNACE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/furnace.png");

    private static final int WIDTH = 100;
    private static final int HEIGHT = 26;
    private static final int INPUT1_X = 1;
    private static final int INPUT2_X = 19;
    private static final int OUTPUT_X = 79;
    private static final int SLOT_Y = 5;
    private static final int ARROW_X = 46;
    private static final int ARROW_Y = 4;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;

    public RafikiTradeRecipeCategory(@NotNull IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.RAFIKI_STICK.get()));
        this.arrow = guiHelper
                .drawableBuilder(FURNACE_TEXTURE, 79, 35, 24, 17)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public @NotNull RecipeType<NpcTradeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.circleofcraft.category.rafiki_trade");
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
            @NotNull IRecipeLayoutBuilder builder, @NotNull NpcTradeRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT1_X, SLOT_Y).addItemStack(recipe.input1());
        if (!recipe.input2().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT2_X, SLOT_Y).addItemStack(recipe.input2());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, SLOT_Y).addItemStack(recipe.output());
    }

    @Override
    public void draw(
            @NotNull NpcTradeRecipe recipe,
            @NotNull IRecipeSlotsView slotsView,
            @NotNull GuiGraphics graphics,
            double mouseX,
            double mouseY) {
        arrow.draw(graphics, ARROW_X, ARROW_Y);
    }
}

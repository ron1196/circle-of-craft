package io.github.ron1196.thelionking.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Walks the loaded recipe list to find recipes that contain or produce a given {@link ItemStack},
 * and renders one match into the quest book's right-page recipe panel. Mirrors the old mod's
 * {@code drawCraftGui} from {@code LKGuiQuests}.
 */
public final class QuestBookRecipeRenderer {

    private static final int SLOT_SIZE = 18;
    private static final int ICON_SIZE = 16;

    private QuestBookRecipeRenderer() {}

    public static List<Recipe<?>> findMatching(@NotNull ItemStack inspected) {
        List<Recipe<?>> matches = new ArrayList<>();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return matches;

        for (Recipe<?> recipe : mc.level.getRecipeManager().getRecipes()) {
            if (!(recipe instanceof ShapedRecipe) && !(recipe instanceof ShapelessRecipe)) continue;
            if (matchesRecipe(recipe, inspected)) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    private static boolean matchesRecipe(@NotNull Recipe<?> recipe, @NotNull ItemStack inspected) {
        ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        if (!result.isEmpty() && ItemStack.isSameItem(result, inspected)) return true;
        for (Ingredient ing : recipe.getIngredients()) {
            if (ing.test(inspected)) return true;
        }
        return false;
    }

    /**
     * Render one recipe inside the right-page area. Caller chooses panel origin (panelX, panelY).
     */
    public static void render(
            @NotNull GuiGraphics graphics,
            @NotNull Recipe<?> recipe,
            @NotNull ItemStack inspected,
            int panelX,
            int panelY) {

        int gridX = panelX + 4;
        int gridY = panelY + 4;
        if (recipe instanceof ShapedRecipe shaped) {
            renderShaped(graphics, shaped, inspected, gridX, gridY);
        } else if (recipe instanceof ShapelessRecipe shapeless) {
            renderShapeless(graphics, shapeless, inspected, gridX, gridY);
        }
        ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        int resultX = panelX + 4 + 3 * SLOT_SIZE + 8;
        int resultY = panelY + 4 + SLOT_SIZE;
        graphics.renderItem(result, resultX, resultY);
        graphics.renderItemDecorations(Minecraft.getInstance().font, result, resultX, resultY);
    }

    private static void renderShaped(
            @NotNull GuiGraphics graphics,
            @NotNull ShapedRecipe recipe,
            @NotNull ItemStack inspected,
            int gridX,
            int gridY) {

        int width = recipe.getWidth();
        int height = recipe.getHeight();
        List<Ingredient> ings = recipe.getIngredients();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                if (idx >= ings.size()) continue;
                ItemStack[] candidates = ings.get(idx).getItems();
                if (candidates.length == 0) continue;
                ItemStack display = candidates[0];
                int x = gridX + col * SLOT_SIZE;
                int y = gridY + row * SLOT_SIZE;
                if (ings.get(idx).test(inspected)) {
                    drawIngredientHighlight(graphics, x, y);
                }
                graphics.renderItem(display, x, y);
            }
        }
    }

    private static void renderShapeless(
            @NotNull GuiGraphics graphics,
            @NotNull ShapelessRecipe recipe,
            @NotNull ItemStack inspected,
            int gridX,
            int gridY) {

        List<Ingredient> ings = recipe.getIngredients();
        for (int idx = 0; idx < ings.size() && idx < 9; idx++) {
            ItemStack[] candidates = ings.get(idx).getItems();
            if (candidates.length == 0) continue;
            ItemStack display = candidates[0];
            int col = idx % 3;
            int row = idx / 3;
            int x = gridX + col * SLOT_SIZE;
            int y = gridY + row * SLOT_SIZE;
            if (ings.get(idx).test(inspected)) {
                drawIngredientHighlight(graphics, x, y);
            }
            graphics.renderItem(display, x, y);
        }
    }

    private static void drawIngredientHighlight(@NotNull GuiGraphics graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + ICON_SIZE + 1, y + ICON_SIZE + 1, 0x4080FF80);
    }
}

package io.github.ron1196.thelionking.compat.jei;

import io.github.ron1196.thelionking.client.gui.GrindingBowlScreen;
import io.github.ron1196.thelionking.menu.GrindingBowlMenu;
import io.github.ron1196.thelionking.recipe.GrindingBowlRecipe;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.MenuTypes;
import io.github.ron1196.thelionking.registry.RecipeTypes;
import java.util.Collection;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
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

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration reg) {
        // Slot 0 is the single recipe input; slots 2..37 are the player inventory + hotbar.
        reg.addRecipeTransferHandler(
                GrindingBowlMenu.class,
                MenuTypes.GRINDING_BOWL_MENU.get(),
                GrindingBowlRecipeCategory.RECIPE_TYPE,
                0,
                1,
                2,
                36);
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration reg) {
        reg.addGuiContainerHandler(GrindingBowlScreen.class, new GrindingBowlGuiHandler());
    }

    /** Exposes the progress-arrow region as a clickable area that opens Grinding Bowl recipes. */
    private static final class GrindingBowlGuiHandler implements IGuiContainerHandler<GrindingBowlScreen> {

        @Override
        public @NotNull Collection<IGuiClickableArea> getGuiClickableAreas(
                @NotNull GrindingBowlScreen screen, double mouseX, double mouseY) {
            return List.of(IGuiClickableArea.createBasic(60, 33, 49, 19, GrindingBowlRecipeCategory.RECIPE_TYPE));
        }
    }
}

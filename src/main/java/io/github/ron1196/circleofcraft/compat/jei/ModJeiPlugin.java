package io.github.ron1196.circleofcraft.compat.jei;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.client.gui.GrindingBowlScreen;
import io.github.ron1196.circleofcraft.entity.npc.RafikiTrades;
import io.github.ron1196.circleofcraft.menu.GrindingBowlMenu;
import io.github.ron1196.circleofcraft.recipe.GrindingBowlRecipe;
import io.github.ron1196.circleofcraft.registry.MenuTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.registry.RecipeTypes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID = CircleOfCraftMod.id("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(
                new GrindingBowlRecipeCategory(reg.getJeiHelpers().getGuiHelper()),
                new RafikiTradeRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
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
        reg.addRecipes(RafikiTradeRecipeCategory.RECIPE_TYPE, rafikiTrades());
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(ModItems.GRINDING_BOWL_ITEM.get()), GrindingBowlRecipeCategory.RECIPE_TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModItems.RAFIKI_STICK.get()), RafikiTradeRecipeCategory.RECIPE_TYPE);
    }

    private static List<NpcTradeRecipe> rafikiTrades() {
        return RafikiTrades.ALL.stream()
                .map(t -> new NpcTradeRecipe(t.input1(), t.input2(), t.output()))
                .toList();
    }

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration reg) {
        reg.addRecipeTransferHandler(new GrindingBowlTransferInfo());
    }

    private static final class GrindingBowlTransferInfo
            implements IRecipeTransferInfo<GrindingBowlMenu, GrindingBowlRecipe> {

        @Override
        public Class<? extends GrindingBowlMenu> getContainerClass() {
            return GrindingBowlMenu.class;
        }

        @Override
        public Optional<MenuType<GrindingBowlMenu>> getMenuType() {
            return Optional.of(MenuTypes.GRINDING_BOWL_MENU.get());
        }

        @Override
        public mezz.jei.api.recipe.RecipeType<GrindingBowlRecipe> getRecipeType() {
            return GrindingBowlRecipeCategory.RECIPE_TYPE;
        }

        @Override
        public boolean canHandle(GrindingBowlMenu container, GrindingBowlRecipe recipe) {
            return true;
        }

        @Override
        public List<Slot> getRecipeSlots(GrindingBowlMenu container, GrindingBowlRecipe recipe) {
            return List.of(container.getSlot(0));
        }

        @Override
        public List<Slot> getInventorySlots(GrindingBowlMenu container, GrindingBowlRecipe recipe) {
            List<Slot> slots = new ArrayList<>(36);
            for (int i = 2; i < 38; i++) {
                slots.add(container.getSlot(i));
            }
            return slots;
        }
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

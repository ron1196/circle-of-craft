package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.menu.BongoDrumMenu;
import io.github.ron1196.circleofcraft.menu.BugTrapMenu;
import io.github.ron1196.circleofcraft.menu.GrindingBowlMenu;
import io.github.ron1196.circleofcraft.menu.QuestBookMenu;
import io.github.ron1196.circleofcraft.menu.QuiverMenu;
import io.github.ron1196.circleofcraft.menu.SimbaInventoryMenu;
import io.github.ron1196.circleofcraft.menu.TimonMerchantMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, CircleOfCraftMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<GrindingBowlMenu>> GRINDING_BOWL_MENU =
            MENU_TYPES.register(
                    "grinding_bowl",
                    () -> IMenuTypeExtension.create(
                            (containerId, playerInv, buf) -> new GrindingBowlMenu(containerId, playerInv)));

    public static final DeferredHolder<MenuType<?>, MenuType<BugTrapMenu>> BUG_TRAP_MENU = MENU_TYPES.register(
            "bug_trap",
            () -> IMenuTypeExtension.create((containerId, playerInv, buf) -> new BugTrapMenu(containerId, playerInv)));

    public static final DeferredHolder<MenuType<?>, MenuType<BongoDrumMenu>> BONGO_DRUM_MENU = MENU_TYPES.register(
            "bongo_drum",
            () -> IMenuTypeExtension.create(
                    (containerId, playerInv, buf) -> new BongoDrumMenu(containerId, playerInv)));

    public static final DeferredHolder<MenuType<?>, MenuType<QuiverMenu>> QUIVER_MENU = MENU_TYPES.register(
            "quiver",
            () -> IMenuTypeExtension.create((containerId, playerInv, buf) -> new QuiverMenu(containerId, playerInv)));

    public static final DeferredHolder<MenuType<?>, MenuType<TimonMerchantMenu>> TIMON_MERCHANT_MENU =
            MENU_TYPES.register(
                    "timon_merchant",
                    () -> IMenuTypeExtension.create(
                            (containerId, playerInv, buf) -> new TimonMerchantMenu(containerId, playerInv)));

    public static final DeferredHolder<MenuType<?>, MenuType<SimbaInventoryMenu>> SIMBA_INVENTORY_MENU =
            MENU_TYPES.register(
                    "simba_inventory",
                    () -> IMenuTypeExtension.create(
                            (containerId, playerInv, buf) -> new SimbaInventoryMenu(containerId, playerInv)));

    public static final DeferredHolder<MenuType<?>, MenuType<QuestBookMenu>> QUEST_BOOK_MENU = MENU_TYPES.register(
            "quest_book",
            () -> IMenuTypeExtension.create(
                    (containerId, playerInv, buf) -> new QuestBookMenu(containerId, playerInv)));
}

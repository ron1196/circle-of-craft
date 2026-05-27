package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.menu.BongoDrumMenu;
import io.github.ron1196.circleofcraft.menu.BugTrapMenu;
import io.github.ron1196.circleofcraft.menu.GrindingBowlMenu;
import io.github.ron1196.circleofcraft.menu.QuestBookMenu;
import io.github.ron1196.circleofcraft.menu.QuiverMenu;
import io.github.ron1196.circleofcraft.menu.SimbaInventoryMenu;
import io.github.ron1196.circleofcraft.menu.TimonMerchantMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, CircleOfCraftMod.MOD_ID);

    public static final RegistryObject<MenuType<GrindingBowlMenu>> GRINDING_BOWL_MENU = MENU_TYPES.register(
            "grinding_bowl",
            () -> IForgeMenuType.create((containerId, playerInv, buf) -> new GrindingBowlMenu(containerId, playerInv)));

    public static final RegistryObject<MenuType<BugTrapMenu>> BUG_TRAP_MENU = MENU_TYPES.register(
            "bug_trap",
            () -> IForgeMenuType.create((containerId, playerInv, buf) -> new BugTrapMenu(containerId, playerInv)));

    public static final RegistryObject<MenuType<BongoDrumMenu>> BONGO_DRUM_MENU = MENU_TYPES.register(
            "bongo_drum",
            () -> IForgeMenuType.create((containerId, playerInv, buf) -> new BongoDrumMenu(containerId, playerInv)));

    public static final RegistryObject<MenuType<QuiverMenu>> QUIVER_MENU = MENU_TYPES.register(
            "quiver",
            () -> IForgeMenuType.create((containerId, playerInv, buf) -> new QuiverMenu(containerId, playerInv)));

    public static final RegistryObject<MenuType<TimonMerchantMenu>> TIMON_MERCHANT_MENU = MENU_TYPES.register(
            "timon_merchant",
            () -> IForgeMenuType.create(
                    (containerId, playerInv, buf) -> new TimonMerchantMenu(containerId, playerInv)));

    public static final RegistryObject<MenuType<SimbaInventoryMenu>> SIMBA_INVENTORY_MENU = MENU_TYPES.register(
            "simba_inventory",
            () -> IForgeMenuType.create(
                    (containerId, playerInv, buf) -> new SimbaInventoryMenu(containerId, playerInv)));

    public static final RegistryObject<MenuType<QuestBookMenu>> QUEST_BOOK_MENU = MENU_TYPES.register(
            "quest_book",
            () -> IForgeMenuType.create((containerId, playerInv, buf) -> new QuestBookMenu(containerId, playerInv)));
}

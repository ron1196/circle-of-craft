package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.menu.GrindingBowlMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LKMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TheLionKingMod.MOD_ID);

    public static final RegistryObject<MenuType<GrindingBowlMenu>> GRINDING_BOWL_MENU =
            MENU_TYPES.register("grinding_bowl",
                    () -> IForgeMenuType.create((containerId, playerInv, buf) ->
                            new GrindingBowlMenu(containerId, playerInv)));
}

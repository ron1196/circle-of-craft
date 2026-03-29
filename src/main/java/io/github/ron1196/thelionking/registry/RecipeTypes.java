package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.recipe.GrindingBowlRecipe;
import io.github.ron1196.thelionking.recipe.GrindingBowlRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TheLionKingMod.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TheLionKingMod.MOD_ID);

    public static final RegistryObject<RecipeType<GrindingBowlRecipe>> GRINDING_TYPE =
            RECIPE_TYPES.register("grinding", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "thelionking:grinding";
                }
            });

    public static final RegistryObject<RecipeSerializer<GrindingBowlRecipe>> GRINDING_SERIALIZER =
            RECIPE_SERIALIZERS.register("grinding", GrindingBowlRecipeSerializer::new);
}

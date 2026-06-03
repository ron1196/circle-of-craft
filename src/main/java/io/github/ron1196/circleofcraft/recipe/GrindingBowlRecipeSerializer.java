package io.github.ron1196.circleofcraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GrindingBowlRecipeSerializer implements RecipeSerializer<GrindingBowlRecipe> {

    public static final MapCodec<GrindingBowlRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(GrindingBowlRecipe::getIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(GrindingBowlRecipe::getResult))
            .apply(inst, GrindingBowlRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrindingBowlRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            GrindingBowlRecipe::getIngredient,
            ItemStack.STREAM_CODEC,
            GrindingBowlRecipe::getResult,
            GrindingBowlRecipe::new);

    @Override
    public MapCodec<GrindingBowlRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GrindingBowlRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}

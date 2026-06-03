package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.resources.ResourceLocation;

/**
 * TODO(jade compat): port to the Jade 15.x (1.21.1) API — see {@link ModJadePlugin} for why this is
 * stubbed. Tracked as a GitHub issue.
 *
 * <p>Intended behaviour (client block tooltip): for a {@code GrindingBowlBlockEntity}, if the input
 * is empty render {@code jade.circleofcraft.grinding_bowl.empty} in GRAY; otherwise render
 * {@code ...input} (input name), look up the recipe via
 * {@code level.getRecipeManager().getRecipeFor(RecipeTypes.GRINDING_TYPE.get(), new SingleRecipeInput(input), level)}
 * (now returns {@code Optional<RecipeHolder<GrindingBowlRecipe>>}; unwrap with {@code .value()}) and
 * render {@code ...output} (result name), plus {@code ...progress} (percent) while grinding.
 */
public final class GrindingBowlProvider {

    public static final ResourceLocation UID = CircleOfCraftMod.id("grinding_bowl");

    private GrindingBowlProvider() {}
}

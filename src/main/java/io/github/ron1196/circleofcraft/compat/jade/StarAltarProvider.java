package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.resources.ResourceLocation;

/**
 * TODO(jade compat): port to the Jade 15.x (1.21.1) API — see {@link ModJadePlugin} for why this is
 * stubbed. Tracked as a GitHub issue.
 *
 * <p>Intended behaviour (client-only block tooltip, no server data): guide the player through the
 * Star Altar ritual. RED when not in {@code Dimensions.PRIDE_LANDS_LEVEL} ({@code ...wrong_dimension})
 * or the sky above is obstructed ({@code ...sky_obstructed}); then, based on the held item and client
 * world state ({@code ClientWorldState.hasSimba}, Rafiki quest stage): GREEN {@code ...use_dust_ready}
 * /{@code ...charm_ready} when ready, GRAY {@code ...already_have_simba}/{@code ...hold_dust}
 * otherwise. Relevant items are {@code ModItems.RAFIKI_DUST} and {@code ModItems.ASTRAL_CHARM} (the
 * latter only when {@code !AstralCharmItem.isActive(stack)}).
 */
public final class StarAltarProvider {

    public static final ResourceLocation UID = CircleOfCraftMod.id("star_altar");

    private StarAltarProvider() {}
}

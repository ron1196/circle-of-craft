package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.resources.ResourceLocation;

/**
 * TODO(jade compat): port to the Jade 15.x (1.21.1) API — see {@link ModJadePlugin} for why this is
 * stubbed. Tracked as a GitHub issue.
 *
 * <p>Intended behaviour (server data + entity tooltip): for a {@code TermiteQueenEntity} render
 * {@code ...hp} (health %), {@code ...minions} (nearby {@code TermiteEntity} count within
 * {@code TERMITE_SEARCH_RADIUS} / {@code MAX_NEARBY_TERMITES}), {@code ...next_spawn} (seconds from
 * the spawn cooldown in server data, only while the queen has a target), and
 * {@code ...zira_mounted} in LIGHT_PURPLE when a {@code ZiraEntity} is riding. Server data carries
 * {@code queen.getSpawnCooldown()}.
 */
public final class TermiteQueenProvider {

    public static final ResourceLocation UID = CircleOfCraftMod.id("termite_queen");

    private TermiteQueenProvider() {}
}

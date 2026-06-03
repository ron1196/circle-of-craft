package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.resources.ResourceLocation;

/**
 * TODO(jade compat): port to the Jade 15.x (1.21.1) API — see {@link ModJadePlugin} for why this is
 * stubbed. Tracked as a GitHub issue.
 *
 * <p>Intended behaviour (server data + block tooltip): read
 * {@code BugTrapBlockEntity.getTicksUntilNextAttract()} into server data; if no bait, render
 * {@code jade.circleofcraft.bug_trap.no_bait} in GRAY, otherwise
 * {@code jade.circleofcraft.bug_trap.next_attract} with seconds = ceil(ticks / 20).
 */
public final class BugTrapProvider {

    public static final ResourceLocation UID = CircleOfCraftMod.id("bug_trap");

    private BugTrapProvider() {}
}

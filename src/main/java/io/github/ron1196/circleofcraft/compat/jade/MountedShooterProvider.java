package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.resources.ResourceLocation;

/**
 * TODO(jade compat): port to the Jade 15.x (1.21.1) API — see {@link ModJadePlugin} for why this is
 * stubbed. Tracked as a GitHub issue.
 *
 * <p>Intended behaviour (client block tooltip): for a {@code MountedShooterBlockEntity}, render the
 * loaded dart stack — {@code jade.circleofcraft.mounted_shooter.empty} in GRAY when empty, otherwise
 * {@code ...ammo} (name, count) — and the current {@code FireMode} via {@code ...mode}.
 */
public final class MountedShooterProvider {

    public static final ResourceLocation UID = CircleOfCraftMod.id("mounted_shooter");

    private MountedShooterProvider() {}
}

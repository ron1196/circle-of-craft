package io.github.ron1196.circleofcraft.compat.jade;

/**
 * Jade (Waila) integration entrypoint.
 *
 * <p>TODO(jade compat): re-enable the providers in this package once Jade ships a compile-usable
 * 1.21.1 API. The only Jade 1.21.1 artifact available on Curse (jade-324717:7545219) is the full
 * obfuscated mod jar — there is no separate {@code -api} artifact, and the jar's
 * {@code snownee.jade.api.*} classes are compiled against a Minecraft ABI that is binary-incompatible
 * with NeoForge 21.1.233's userdev. Adding it to the compile classpath makes ~190 unrelated project
 * classes fail with bogus "abstract method codec()/defineSynchedData(Builder)/isFood(ItemStack)"
 * errors. Per the repo Workaround Policy this integration is stubbed (no-op) and tracked as a GitHub
 * issue. Jade remains a {@code runtimeOnly} dependency so the mod still loads alongside it — it just
 * contributes no tooltips until the providers are ported.
 *
 * <p>This class intentionally does NOT implement {@code snownee.jade.api.IWailaPlugin} (unavailable
 * at compile time). Jade discovers plugins via the {@code @WailaPlugin} annotation; once a usable API
 * is on the classpath, restore the annotation, the {@code IWailaPlugin} {@code register}/
 * {@code registerClient} methods, and the providers {@link AnimalFavorProvider},
 * {@link BugTrapProvider}, {@link GrindingBowlProvider}, {@link MountedShooterProvider},
 * {@link StarAltarProvider} and {@link TermiteQueenProvider}.
 */
public final class ModJadePlugin {

    private ModJadePlugin() {}
}

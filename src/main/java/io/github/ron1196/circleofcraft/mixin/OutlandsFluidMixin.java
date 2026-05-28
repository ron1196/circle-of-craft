package io.github.ron1196.circleofcraft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Fixes lava-based dimensions so that caves behave like the overworld.
 *
 * Vanilla's aquifer system has a hardcoded short-circuit: when the global
 * fluid picker returns lava, it skips all aquifer dry/wet calculations and
 * immediately places lava. Water does not have this short-circuit, which
 * is why overworld caves are mostly dry but lava dimensions flood entirely.
 *
 * This mixin removes the lava short-circuit in the aquifer so that lava
 * goes through the same dry/wet noise calculations as water. This is safe
 * for vanilla dimensions because the Nether uses aquifers_enabled=false
 * (so this code path never runs) and the overworld's deep lava is handled
 * separately by computeFluidType().
 */
@Mixin(Aquifer.NoiseBasedAquifer.class)
public class OutlandsFluidMixin {

    /**
     * Wraps the first {@code BlockState.is(Block)} call in
     * {@code computeSubstance} — the lava short-circuit check — and forces
     * it to return {@code false}. The {@link Operation} is intentionally not
     * invoked: bypassing the original call sends lava through the full
     * aquifer dry/wet calculations, creating dry caves like water does in
     * the overworld.
     *
     * Implemented with MixinExtras' {@code @WrapOperation} (instead of the
     * older {@code @Redirect}) for better compatibility with other mods
     * that target the same call site.
     */
    @WrapOperation(
            method = "computeSubstance",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
                            ordinal = 0))
    private boolean circleofcraft$skipLavaShortCircuit(BlockState state, Block block, Operation<Boolean> original) {
        return false;
    }
}

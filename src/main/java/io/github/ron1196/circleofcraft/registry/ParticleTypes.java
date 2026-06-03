package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleTypes {

    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, CircleOfCraftMod.MOD_ID);

    public static final DeferredHolder<net.minecraft.core.particles.ParticleType<?>, SimpleParticleType>
            PRIDE_LANDS_PORTAL = PARTICLE_TYPES.register("pride_lands_portal", () -> new SimpleParticleType(false));

    public static final DeferredHolder<net.minecraft.core.particles.ParticleType<?>, SimpleParticleType>
            OUTLANDS_PORTAL = PARTICLE_TYPES.register("outlands_portal", () -> new SimpleParticleType(false));
}

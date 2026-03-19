package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleTypes {

    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TheLionKingMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> PRIDE_LANDS_PORTAL =
            PARTICLE_TYPES.register("pride_lands_portal", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> OUTLANDS_PORTAL =
            PARTICLE_TYPES.register("outlands_portal", () -> new SimpleParticleType(false));
}

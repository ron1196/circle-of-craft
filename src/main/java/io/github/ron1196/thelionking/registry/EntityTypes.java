package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.RugEntity;
import io.github.ron1196.thelionking.entity.animal.*;
import io.github.ron1196.thelionking.entity.hostile.*;
import io.github.ron1196.thelionking.entity.npc.*;
import io.github.ron1196.thelionking.entity.projectile.CoinEntity;
import io.github.ron1196.thelionking.entity.projectile.DartEntity;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.entity.projectile.PumbaaBombEntity;
import io.github.ron1196.thelionking.entity.projectile.SpearEntity;
import io.github.ron1196.thelionking.entity.projectile.TermiteThrownEntity;
import io.github.ron1196.thelionking.entity.projectile.ZazuEggEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TheLionKingMod.MOD_ID);

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static <T extends Mob> RegistryObject<EntityType<T>> registerCreature(
            String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, MobCategory.CREATURE)
                .sized(width, height)
                .clientTrackingRange(10)
                .build(name));
    }

    private static <T extends Mob> RegistryObject<EntityType<T>> registerCreature(
            String name, EntityType.EntityFactory<T> factory, float width, float height, int trackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, MobCategory.CREATURE)
                .sized(width, height)
                .clientTrackingRange(trackingRange)
                .build(name));
    }

    private static <T extends Mob> RegistryObject<EntityType<T>> registerMonster(
            String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MONSTER)
                .sized(width, height)
                .clientTrackingRange(10)
                .build(name));
    }

    private static <T extends Mob> RegistryObject<EntityType<T>> registerMonster(
            String name, EntityType.EntityFactory<T> factory, float width, float height, int trackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MONSTER)
                .sized(width, height)
                .clientTrackingRange(trackingRange)
                .build(name));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(
            String name, EntityType.EntityFactory<T> factory, float size, int updateInterval) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.<T>of(factory, MobCategory.MISC)
                .sized(size, size)
                .clientTrackingRange(4)
                .updateInterval(updateInterval)
                .build(name));
    }

    // ── Passive Entities ─────────────────────────────────────────────────────

    public static final RegistryObject<EntityType<LionEntity>> LION =
            registerCreature("lion", LionEntity::new, 1.3F, 1.6F);
    public static final RegistryObject<EntityType<LionessEntity>> LIONESS =
            registerCreature("lioness", LionessEntity::new, 1.2F, 1.3F);
    public static final RegistryObject<EntityType<ZebraEntity>> ZEBRA =
            registerCreature("zebra", ZebraEntity::new, 1.1F, 1.4F);
    public static final RegistryObject<EntityType<GiraffeEntity>> GIRAFFE =
            registerCreature("giraffe", GiraffeEntity::new, 1.7F, 2.8F);
    public static final RegistryObject<EntityType<RhinoEntity>> RHINO =
            registerCreature("rhino", RhinoEntity::new, 1.3F, 1.2F);
    public static final RegistryObject<EntityType<GemsbokEntity>> GEMSBOK =
            registerCreature("gemsbok", GemsbokEntity::new, 0.9F, 1.4F);
    public static final RegistryObject<EntityType<DikDikEntity>> DIKDIK =
            registerCreature("dikdik", DikDikEntity::new, 0.6F, 1.0F);
    public static final RegistryObject<EntityType<FlamingoEntity>> FLAMINGO =
            registerCreature("flamingo", FlamingoEntity::new, 0.6F, 1.8F);
    public static final RegistryObject<EntityType<ZazuEntity>> ZAZU =
            registerCreature("zazu", ZazuEntity::new, 0.6F, 0.7F);
    public static final RegistryObject<EntityType<BugEntity>> BUG =
            registerCreature("bug", BugEntity::new, 0.4F, 0.4F, 8);

    // ── Projectile Entities ──────────────────────────────────────────────────

    public static final RegistryObject<EntityType<DartEntity>> DART =
            registerProjectile("dart", DartEntity::new, 0.5F, 20);
    public static final RegistryObject<EntityType<SpearEntity>> SPEAR =
            registerProjectile("spear", SpearEntity::new, 0.5F, 20);
    public static final RegistryObject<EntityType<PumbaaBombEntity>> PUMBAA_BOMB =
            registerProjectile("pumbaa_bomb", PumbaaBombEntity::new, 0.25F, 10);
    public static final RegistryObject<EntityType<TermiteThrownEntity>> TERMITE_THROWN =
            registerProjectile("termite_thrown", TermiteThrownEntity::new, 0.25F, 10);
    public static final RegistryObject<EntityType<CoinEntity>> COIN =
            registerProjectile("coin", CoinEntity::new, 0.25F, 10);
    public static final RegistryObject<EntityType<ZazuEggEntity>> ZAZU_EGG =
            registerProjectile("zazu_egg", ZazuEggEntity::new, 0.25F, 10);

    // ── Hostile Entities ─────────────────────────────────────────────────────

    public static final RegistryObject<EntityType<HyenaEntity>> HYENA =
            registerMonster("hyena", HyenaEntity::new, 0.6F, 0.8F);
    public static final RegistryObject<EntityType<SkeletalHyenaEntity>> SKELETAL_HYENA =
            registerMonster("skeletal_hyena", SkeletalHyenaEntity::new, 0.6F, 0.8F);
    public static final RegistryObject<EntityType<SkeletalHyenaHeadEntity>> SKELETAL_HYENA_HEAD =
            registerMonster("skeletal_hyena_head", SkeletalHyenaHeadEntity::new, 0.6F, 0.6F);
    public static final RegistryObject<EntityType<OutlanderEntity>> OUTLANDER =
            registerMonster("outlander", OutlanderEntity::new, 1.3F, 1.6F);
    public static final RegistryObject<EntityType<VultureEntity>> VULTURE =
            registerMonster("vulture", VultureEntity::new, 0.8F, 1.5F);
    public static final RegistryObject<EntityType<CrocodileEntity>> CROCODILE =
            registerMonster("crocodile", CrocodileEntity::new, 3.0F, 0.7F);
    public static final RegistryObject<EntityType<TermiteEntity>> TERMITE =
            registerMonster("termite", TermiteEntity::new, 0.5F, 0.4F, 8);
    public static final RegistryObject<EntityType<TermiteQueenEntity>> TERMITE_QUEEN =
            registerMonster("termite_queen", TermiteQueenEntity::new, 2.5F, 2.0F);

    // ── NPC Entities ─────────────────────────────────────────────────────────

    public static final RegistryObject<EntityType<TicketLionEntity>> TICKET_LION =
            registerCreature("ticket_lion", TicketLionEntity::new, 1.3F, 1.6F);
    public static final RegistryObject<EntityType<RafikiEntity>> RAFIKI =
            registerCreature("rafiki", RafikiEntity::new, 0.7F, 1.6F);
    public static final RegistryObject<EntityType<SimbaEntity>> SIMBA =
            registerCreature("simba", SimbaEntity::new, 1.3F, 1.6F);
    public static final RegistryObject<EntityType<TimonEntity>> TIMON =
            registerCreature("timon", TimonEntity::new, 0.4F, 0.9F);
    public static final RegistryObject<EntityType<PumbaaEntity>> PUMBAA =
            registerCreature("pumbaa", PumbaaEntity::new, 1.2F, 1.3F);
    public static final RegistryObject<EntityType<ScarEntity>> SCAR =
            registerMonster("scar", ScarEntity::new, 1.3F, 1.6F);
    public static final RegistryObject<EntityType<ZiraEntity>> ZIRA =
            ENTITY_TYPES.register("zira", () -> EntityType.Builder.of(ZiraEntity::new, MobCategory.MONSTER)
                    .sized(1.2F, 1.3F)
                    .clientTrackingRange(10)
                    .fireImmune()
                    .build("zira"));

    // ── Interactive Entities ─────────────────────────────────────────────────

    public static final RegistryObject<EntityType<RugEntity>> RUG =
            ENTITY_TYPES.register("rug", () -> EntityType.Builder.<RugEntity>of(RugEntity::new, MobCategory.MISC)
                    .sized(1.2F, 0.2F)
                    .clientTrackingRange(10)
                    .build("rug"));

    // ── Weather Effects ──────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public static final RegistryObject<EntityType<LightningBoltEntity>> LIGHTNING_BOLT =
            ENTITY_TYPES.register("lightning_bolt", () -> (EntityType<LightningBoltEntity>)
                    (EntityType<?>) EntityType.Builder.<LightningBolt>of(LightningBoltEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .clientTrackingRange(16)
                            .updateInterval(Integer.MAX_VALUE)
                            .noSave()
                            .noSummon()
                            .fireImmune()
                            .build("lightning_bolt"));
}

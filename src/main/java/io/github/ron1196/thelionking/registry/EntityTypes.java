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
import io.github.ron1196.thelionking.entity.projectile.ThrownTermiteEntity;
import io.github.ron1196.thelionking.entity.projectile.ZazuEggEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TheLionKingMod.MOD_ID);

    // ========== Passive Entities ==========
    public static final RegistryObject<EntityType<LionEntity>> LION =
            ENTITY_TYPES.register("lion", () -> EntityType.Builder.of(LionEntity::new, MobCategory.CREATURE)
                    .sized(1.3F, 1.6F)
                    .clientTrackingRange(10)
                    .build("lion"));

    public static final RegistryObject<EntityType<LionessEntity>> LIONESS =
            ENTITY_TYPES.register("lioness", () -> EntityType.Builder.of(LionessEntity::new, MobCategory.CREATURE)
                    .sized(1.2F, 1.3F)
                    .clientTrackingRange(10)
                    .build("lioness"));

    public static final RegistryObject<EntityType<ZebraEntity>> ZEBRA =
            ENTITY_TYPES.register("zebra", () -> EntityType.Builder.of(ZebraEntity::new, MobCategory.CREATURE)
                    .sized(1.1F, 1.4F)
                    .clientTrackingRange(10)
                    .build("zebra"));

    public static final RegistryObject<EntityType<GiraffeEntity>> GIRAFFE =
            ENTITY_TYPES.register("giraffe", () -> EntityType.Builder.of(GiraffeEntity::new, MobCategory.CREATURE)
                    .sized(1.7F, 2.8F)
                    .clientTrackingRange(10)
                    .build("giraffe"));

    public static final RegistryObject<EntityType<RhinoEntity>> RHINO =
            ENTITY_TYPES.register("rhino", () -> EntityType.Builder.of(RhinoEntity::new, MobCategory.CREATURE)
                    .sized(1.3F, 1.2F)
                    .clientTrackingRange(10)
                    .build("rhino"));

    public static final RegistryObject<EntityType<GemsbokEntity>> GEMSBOK =
            ENTITY_TYPES.register("gemsbok", () -> EntityType.Builder.of(GemsbokEntity::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.4F)
                    .clientTrackingRange(10)
                    .build("gemsbok"));

    public static final RegistryObject<EntityType<DikDikEntity>> DIKDIK =
            ENTITY_TYPES.register("dikdik", () -> EntityType.Builder.of(DikDikEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.0F)
                    .clientTrackingRange(10)
                    .build("dikdik"));

    public static final RegistryObject<EntityType<FlamingoEntity>> FLAMINGO =
            ENTITY_TYPES.register("flamingo", () -> EntityType.Builder.of(FlamingoEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .build("flamingo"));

    public static final RegistryObject<EntityType<ZazuEntity>> ZAZU =
            ENTITY_TYPES.register("zazu", () -> EntityType.Builder.of(ZazuEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.7F)
                    .clientTrackingRange(10)
                    .build("zazu"));

    public static final RegistryObject<EntityType<BugEntity>> BUG =
            ENTITY_TYPES.register("bug", () -> EntityType.Builder.of(BugEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.4F)
                    .clientTrackingRange(8)
                    .build("bug"));

    // ========== Projectile Entities ==========
    public static final RegistryObject<EntityType<DartEntity>> DART =
            ENTITY_TYPES.register("dart", () -> EntityType.Builder.<DartEntity>of(DartEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("dart"));

    public static final RegistryObject<EntityType<SpearEntity>> SPEAR =
            ENTITY_TYPES.register("spear", () -> EntityType.Builder.<SpearEntity>of(SpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("spear"));

    public static final RegistryObject<EntityType<PumbaaBombEntity>> PUMBAA_BOMB = ENTITY_TYPES.register(
            "pumbaa_bomb", () -> EntityType.Builder.<PumbaaBombEntity>of(PumbaaBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("pumbaa_bomb"));

    public static final RegistryObject<EntityType<ThrownTermiteEntity>> THROWN_TERMITE =
            ENTITY_TYPES.register("thrown_termite", () -> EntityType.Builder.<ThrownTermiteEntity>of(
                            ThrownTermiteEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("thrown_termite"));

    public static final RegistryObject<EntityType<CoinEntity>> COIN =
            ENTITY_TYPES.register("coin", () -> EntityType.Builder.<CoinEntity>of(CoinEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("coin"));

    public static final RegistryObject<EntityType<ZazuEggEntity>> ZAZU_EGG = ENTITY_TYPES.register(
            "zazu_egg", () -> EntityType.Builder.<ZazuEggEntity>of(ZazuEggEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("zazu_egg"));

    // ========== Hostile Entities ==========
    public static final RegistryObject<EntityType<HyenaEntity>> HYENA =
            ENTITY_TYPES.register("hyena", () -> EntityType.Builder.of(HyenaEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.8F)
                    .clientTrackingRange(10)
                    .build("hyena"));

    public static final RegistryObject<EntityType<SkeletalHyenaEntity>> SKELETAL_HYENA = ENTITY_TYPES.register(
            "skeletal_hyena", () -> EntityType.Builder.of(SkeletalHyenaEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.8F)
                    .clientTrackingRange(10)
                    .build("skeletal_hyena"));

    public static final RegistryObject<EntityType<OutlanderEntity>> OUTLANDER =
            ENTITY_TYPES.register("outlander", () -> EntityType.Builder.of(OutlanderEntity::new, MobCategory.MONSTER)
                    .sized(1.3F, 1.6F)
                    .clientTrackingRange(10)
                    .build("outlander"));

    public static final RegistryObject<EntityType<VultureEntity>> VULTURE =
            ENTITY_TYPES.register("vulture", () -> EntityType.Builder.of(VultureEntity::new, MobCategory.MONSTER)
                    .sized(0.8F, 1.5F)
                    .clientTrackingRange(10)
                    .build("vulture"));

    public static final RegistryObject<EntityType<CrocodileEntity>> CROCODILE =
            ENTITY_TYPES.register("crocodile", () -> EntityType.Builder.of(CrocodileEntity::new, MobCategory.MONSTER)
                    .sized(3.0F, 0.7F)
                    .clientTrackingRange(10)
                    .build("crocodile"));

    public static final RegistryObject<EntityType<TermiteEntity>> TERMITE =
            ENTITY_TYPES.register("termite", () -> EntityType.Builder.of(TermiteEntity::new, MobCategory.MONSTER)
                    .sized(0.5F, 0.4F)
                    .clientTrackingRange(8)
                    .build("termite"));

    public static final RegistryObject<EntityType<TermiteQueenEntity>> TERMITE_QUEEN = ENTITY_TYPES.register(
            "termite_queen", () -> EntityType.Builder.of(TermiteQueenEntity::new, MobCategory.MONSTER)
                    .sized(2.5F, 2.0F)
                    .clientTrackingRange(10)
                    .build("termite_queen"));

    // ========== Ticket Lion ==========
    public static final RegistryObject<EntityType<io.github.ron1196.thelionking.entity.npc.TicketLionEntity>>
            TICKET_LION = ENTITY_TYPES.register("ticket_lion", () -> EntityType.Builder.of(
                    io.github.ron1196.thelionking.entity.npc.TicketLionEntity::new, MobCategory.CREATURE)
            .sized(1.3F, 1.6F)
            .clientTrackingRange(10)
            .build("ticket_lion"));

    // ========== NPC Entities ==========
    public static final RegistryObject<EntityType<RafikiEntity>> RAFIKI =
            ENTITY_TYPES.register("rafiki", () -> EntityType.Builder.of(RafikiEntity::new, MobCategory.CREATURE)
                    .sized(0.7F, 1.6F)
                    .clientTrackingRange(10)
                    .build("rafiki"));

    public static final RegistryObject<EntityType<SimbaEntity>> SIMBA =
            ENTITY_TYPES.register("simba", () -> EntityType.Builder.of(SimbaEntity::new, MobCategory.CREATURE)
                    .sized(1.3F, 1.6F)
                    .clientTrackingRange(10)
                    .build("simba"));

    public static final RegistryObject<EntityType<TimonEntity>> TIMON =
            ENTITY_TYPES.register("timon", () -> EntityType.Builder.of(TimonEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.2F)
                    .clientTrackingRange(10)
                    .build("timon"));

    public static final RegistryObject<EntityType<PumbaaEntity>> PUMBAA =
            ENTITY_TYPES.register("pumbaa", () -> EntityType.Builder.of(PumbaaEntity::new, MobCategory.CREATURE)
                    .sized(1.2F, 1.3F)
                    .clientTrackingRange(10)
                    .build("pumbaa"));

    public static final RegistryObject<EntityType<ScarEntity>> SCAR =
            ENTITY_TYPES.register("scar", () -> EntityType.Builder.of(ScarEntity::new, MobCategory.MONSTER)
                    .sized(1.3F, 1.6F)
                    .clientTrackingRange(10)
                    .build("scar"));

    public static final RegistryObject<EntityType<ZiraEntity>> ZIRA =
            ENTITY_TYPES.register("zira", () -> EntityType.Builder.of(ZiraEntity::new, MobCategory.MONSTER)
                    .sized(1.2F, 1.3F)
                    .clientTrackingRange(10)
                    .build("zira"));

    // ========== Interactive Entities ==========
    public static final RegistryObject<EntityType<RugEntity>> RUG =
            ENTITY_TYPES.register("rug", () -> EntityType.Builder.<RugEntity>of(RugEntity::new, MobCategory.MISC)
                    .sized(1.2F, 0.2F)
                    .clientTrackingRange(10)
                    .build("rug"));

    // ========== Weather Effects ==========
    @SuppressWarnings("unchecked")
    public static final RegistryObject<EntityType<LightningBoltEntity>> LK_LIGHTNING_BOLT =
            ENTITY_TYPES.register("lk_lightning_bolt", () -> (EntityType<LightningBoltEntity>)
                    (EntityType<?>) EntityType.Builder.<LightningBolt>of(LightningBoltEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .clientTrackingRange(16)
                            .updateInterval(Integer.MAX_VALUE)
                            .noSave()
                            .noSummon()
                            .fireImmune()
                            .build("lk_lightning_bolt"));

    // ========== Skeletal Hyena Head ==========
    public static final RegistryObject<EntityType<SkeletalHyenaHeadEntity>> SKELETAL_HYENA_HEAD = ENTITY_TYPES.register(
            "skeletal_hyena_head", () -> EntityType.Builder.of(SkeletalHyenaHeadEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.6F)
                    .clientTrackingRange(10)
                    .build("skeletal_hyena_head"));
}

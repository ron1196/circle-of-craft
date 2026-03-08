package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LKEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TheLionKingMod.MOD_ID);

    public static final RegistryObject<EntityType<LionEntity>> LION = ENTITY_TYPES.register("lion",
            () -> EntityType.Builder.of(LionEntity::new, MobCategory.CREATURE)
                    .sized(1.4F, 1.4F).clientTrackingRange(10).build("lion"));

    public static final RegistryObject<EntityType<LionessEntity>> LIONESS = ENTITY_TYPES.register("lioness",
            () -> EntityType.Builder.of(LionessEntity::new, MobCategory.CREATURE)
                    .sized(1.2F, 1.2F).clientTrackingRange(10).build("lioness"));

    public static final RegistryObject<EntityType<ZebraEntity>> ZEBRA = ENTITY_TYPES.register("zebra",
            () -> EntityType.Builder.of(ZebraEntity::new, MobCategory.CREATURE)
                    .sized(1.4F, 1.6F).clientTrackingRange(10).build("zebra"));

    public static final RegistryObject<EntityType<GiraffeEntity>> GIRAFFE = ENTITY_TYPES.register("giraffe",
            () -> EntityType.Builder.of(GiraffeEntity::new, MobCategory.CREATURE)
                    .sized(1.6F, 3.5F).clientTrackingRange(10).build("giraffe"));

    public static final RegistryObject<EntityType<RhinoEntity>> RHINO = ENTITY_TYPES.register("rhino",
            () -> EntityType.Builder.of(RhinoEntity::new, MobCategory.CREATURE)
                    .sized(1.8F, 1.6F).clientTrackingRange(10).build("rhino"));

    public static final RegistryObject<EntityType<GemsbokEntity>> GEMSBOK = ENTITY_TYPES.register("gemsbok",
            () -> EntityType.Builder.of(GemsbokEntity::new, MobCategory.CREATURE)
                    .sized(1.2F, 1.4F).clientTrackingRange(10).build("gemsbok"));

    public static final RegistryObject<EntityType<DikDikEntity>> DIKDIK = ENTITY_TYPES.register("dikdik",
            () -> EntityType.Builder.of(DikDikEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.8F).clientTrackingRange(10).build("dikdik"));

    public static final RegistryObject<EntityType<FlamingoEntity>> FLAMINGO = ENTITY_TYPES.register("flamingo",
            () -> EntityType.Builder.of(FlamingoEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.4F).clientTrackingRange(10).build("flamingo"));

    public static final RegistryObject<EntityType<ZazuEntity>> ZAZU = ENTITY_TYPES.register("zazu",
            () -> EntityType.Builder.of(ZazuEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F).clientTrackingRange(10).build("zazu"));

    public static final RegistryObject<EntityType<BugEntity>> BUG = ENTITY_TYPES.register("bug_entity",
            () -> EntityType.Builder.of(BugEntity::new, MobCategory.CREATURE)
                    .sized(0.3F, 0.3F).clientTrackingRange(8).build("bug_entity"));
}

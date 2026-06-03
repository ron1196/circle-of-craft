package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.animal.Gender;
import io.github.ron1196.circleofcraft.entity.animal.LionEntity;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class LionGameTests {

    private static final String EMPTY = "empty";
    private static final BlockPos SPAWN_A = new BlockPos(2, 2, 2);
    private static final BlockPos SPAWN_B = new BlockPos(3, 2, 2);
    private static final BlockPos ARENA_MIN = new BlockPos(0, 0, 0);
    private static final BlockPos ARENA_MAX = new BlockPos(4, 4, 4);

    private static long countCubsInArena(GameTestHelper helper) {
        BlockPos min = helper.absolutePos(ARENA_MIN);
        BlockPos max = helper.absolutePos(ARENA_MAX);
        AABB bounds = new AABB(min, max);
        return helper.getLevel()
                .getEntitiesOfClass(LionEntity.class, bounds, LionEntity::isBaby)
                .size();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void babyMaleIsManeless(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        lion.setBaby(true);
        lion.setGender(Gender.MALE);
        if (lion.shouldShowMane()) {
            helper.fail("baby male should be maneless", lion);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void babyFemaleIsManeless(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        lion.setBaby(true);
        lion.setGender(Gender.FEMALE);
        if (lion.shouldShowMane()) {
            helper.fail("baby female should be maneless", lion);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void adultFemaleIsManeless(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        lion.setBaby(false);
        lion.setGender(Gender.FEMALE);
        if (lion.shouldShowMane()) {
            helper.fail("adult female should be maneless", lion);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void adultMaleIsManed(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        lion.setBaby(false);
        lion.setGender(Gender.MALE);
        if (!lion.shouldShowMane()) {
            helper.fail("adult male should be maned", lion);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 80)
    public void maleCubGainsManeOnAgeUp(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        lion.setBaby(true);
        lion.setGender(Gender.MALE);
        if (lion.shouldShowMane()) {
            helper.fail("male cub should start maneless", lion);
        }
        lion.setAge(0);
        if (!lion.shouldShowMane()) {
            helper.fail("male should be maned after ageing up", lion);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 200)
    public void sameGenderDoesNotBreed(GameTestHelper helper) {
        LionEntity a = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        a.setBaby(false);
        a.setGender(Gender.MALE);
        a.setInLoveTime(600);

        LionEntity b = helper.spawn(EntityTypes.LION.get(), SPAWN_B);
        b.setBaby(false);
        b.setGender(Gender.MALE);
        b.setInLoveTime(600);

        helper.runAfterDelay(180, () -> {
            long babies = countCubsInArena(helper);
            if (babies > 0) {
                helper.fail("two males should not produce a cub");
            }
            helper.succeed();
        });
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void oppositeGenderBreeds(GameTestHelper helper) {
        LionEntity male = helper.spawn(EntityTypes.LION.get(), SPAWN_A);
        male.setBaby(false);
        male.setGender(Gender.MALE);
        male.setInLoveTime(600);

        LionEntity female = helper.spawn(EntityTypes.LION.get(), SPAWN_B);
        female.setBaby(false);
        female.setGender(Gender.FEMALE);
        female.setInLoveTime(600);

        if (!male.canMate(female)) {
            helper.fail("opposite-gender lions should be able to mate", male);
        }
        AgeableMob cub = male.getBreedOffspring(helper.getLevel(), female);
        if (!(cub instanceof LionEntity)) {
            helper.fail("breeding opposite-gender lions should produce a lion cub", male);
        }
        helper.succeed();
    }
}

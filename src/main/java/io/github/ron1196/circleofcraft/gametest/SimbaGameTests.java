package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class SimbaGameTests {

    private static final String EMPTY = "empty";
    private static final BlockPos SPAWN = new BlockPos(2, 2, 2);

    private static SimbaEntity reload(GameTestHelper helper, CompoundTag saved) {
        SimbaEntity loaded = EntityTypes.SIMBA.get().create(helper.getLevel());
        loaded.load(saved);
        return loaded;
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void newSimbaIsCub(GameTestHelper helper) {
        SimbaEntity simba = helper.spawn(EntityTypes.SIMBA.get(), SPAWN);
        if (!simba.isBaby()) {
            helper.fail("a freshly spawned Simba should be a cub", simba);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void savedAdultStaysAdult(GameTestHelper helper) {
        SimbaEntity simba = helper.spawn(EntityTypes.SIMBA.get(), SPAWN);
        simba.setBaby(false);
        if (reload(helper, simba.saveWithoutId(new CompoundTag())).isBaby()) {
            helper.fail("a saved adult Simba must load as an adult", simba);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void savedCubStaysCub(GameTestHelper helper) {
        SimbaEntity simba = helper.spawn(EntityTypes.SIMBA.get(), SPAWN);
        simba.setBaby(true);
        if (!reload(helper, simba.saveWithoutId(new CompoundTag())).isBaby()) {
            helper.fail("a saved cub Simba must load as a cub", simba);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void legacyBabyFlagCubLoadsAsGrowingCub(GameTestHelper helper) {
        CompoundTag saved = new CompoundTag();
        saved.putBoolean("Baby", true);
        saved.putInt("Age", 0);
        SimbaEntity simba = reload(helper, saved);
        if (!simba.isBaby() || simba.getAge() != -SimbaEntity.GROW_UP_TICKS) {
            helper.fail("a cub saved with the old Baby flag must load as a cub with a full growth timer");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void legacyBabyFlagAdultLoadsAsAdult(GameTestHelper helper) {
        CompoundTag saved = new CompoundTag();
        saved.putBoolean("Baby", false);
        saved.putInt("Age", 0);
        if (reload(helper, saved).isBaby()) {
            helper.fail("an adult saved with the old Baby flag must load as an adult");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 60)
    public void cubGrowsUpWhenTimerRunsOut(GameTestHelper helper) {
        SimbaEntity simba = helper.spawn(EntityTypes.SIMBA.get(), SPAWN);
        simba.setAge(-10);
        helper.succeedWhen(() -> {
            if (simba.isBaby()) {
                helper.fail("Simba should have grown up", simba);
            }
        });
    }

    @GameTest(template = EMPTY, timeoutTicks = 60)
    public void cubHitboxGrowsWithAge(GameTestHelper helper) {
        SimbaEntity newborn = helper.spawn(EntityTypes.SIMBA.get(), SPAWN);
        SimbaEntity older = helper.spawn(EntityTypes.SIMBA.get(), SPAWN.east(2));
        older.setAge(-SimbaEntity.GROW_UP_TICKS / 4);
        helper.succeedWhen(() -> {
            if (!(older.isBaby() && older.getBbHeight() > newborn.getBbHeight() + 0.1F)) {
                helper.fail("a cub closer to adulthood should be taller than a newborn", older);
            }
        });
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void summonWithoutBabyTagIsCub(GameTestHelper helper) {
        if (!reload(helper, new CompoundTag()).isBaby()) {
            helper.fail("/summon loads an empty tag, and the summoned Simba should be a cub");
        }
        helper.succeed();
    }
}

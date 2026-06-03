package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.registry.Features;
import java.util.Optional;
import net.minecraft.world.level.block.grower.TreeGrower;

public class TreeGrowers {

    public static final TreeGrower ACACIA = new TreeGrower(
            "circleofcraft:acacia", Optional.empty(), Optional.of(Features.PRIDE_ACACIA_TREE_KEY), Optional.empty());

    public static final TreeGrower RAINFOREST = new TreeGrower(
            "circleofcraft:rainforest",
            0.1F,
            Optional.of(Features.MEGA_RAINFOREST_TREE_KEY),
            Optional.empty(),
            Optional.of(Features.RAINFOREST_TREE_KEY),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    public static final TreeGrower MANGO = new TreeGrower(
            "circleofcraft:mango", Optional.empty(), Optional.of(Features.MANGO_TREE_KEY), Optional.empty());

    public static final TreeGrower PASSION = new TreeGrower(
            "circleofcraft:passion", Optional.empty(), Optional.of(Features.PASSION_TREE_KEY), Optional.empty());

    public static final TreeGrower BANANA = new TreeGrower(
            "circleofcraft:banana", Optional.empty(), Optional.of(Features.BANANA_TREE_KEY), Optional.empty());
}

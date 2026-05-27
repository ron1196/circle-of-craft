package io.github.ron1196.circleofcraft.quest.stage;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;

public record ClaimableReward(Supplier<Item> item, int count) {}

package io.github.ron1196.thelionking.quest;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public record ClaimableReward(Supplier<Item> item, int count, String rewardKey) {
}

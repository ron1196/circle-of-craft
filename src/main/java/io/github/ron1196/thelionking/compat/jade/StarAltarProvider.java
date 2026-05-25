package io.github.ron1196.thelionking.compat.jade;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.item.SimbaCharmItem;
import io.github.ron1196.thelionking.network.ClientWorldState;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum StarAltarProvider implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation UID = new ResourceLocation(TheLionKingMod.MOD_ID, "star_altar");

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip, @NotNull BlockAccessor accessor, @NotNull IPluginConfig config) {
        Level level = accessor.getLevel();
        BlockPos pos = accessor.getPosition();

        if (level.dimension() != Dimensions.PRIDE_LANDS_LEVEL) {
            addRed(tooltip, "jade.thelionking.star_altar.wrong_dimension");
            return;
        }
        if (!level.canSeeSky(pos.above())) {
            addRed(tooltip, "jade.thelionking.star_altar.sky_obstructed");
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack relevant = findRelevantHeldItem(player.getMainHandItem(), player.getOffhandItem());
        boolean hasSimba = ClientWorldState.hasSimba;
        String rafikiStage = ClientWorldState.getQuestStageId(RafikiQuestline.QUEST_ID);

        if (relevant != null && relevant.is(LionKingItems.RAFIKI_DUST.get())) {
            if (hasSimba) {
                addGray(tooltip, "jade.thelionking.star_altar.already_have_simba");
            } else {
                addGreen(tooltip, "jade.thelionking.star_altar.use_dust_ready");
            }
            return;
        }
        if (relevant != null && relevant.is(LionKingItems.SIMBA_CHARM.get()) && !SimbaCharmItem.isActive(relevant)) {
            addGreen(tooltip, "jade.thelionking.star_altar.charm_ready");
            return;
        }

        if (RafikiQuestline.Stage.USE_STAR_ALTAR.name().equals(rafikiStage) && !hasSimba) {
            addGray(tooltip, "jade.thelionking.star_altar.hold_dust");
        }
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }

    private static @Nullable ItemStack findRelevantHeldItem(ItemStack main, ItemStack off) {
        if (isRelevant(main.getItem())) {
            return main;
        }
        if (isRelevant(off.getItem())) {
            return off;
        }
        return null;
    }

    private static boolean isRelevant(Item item) {
        return item == LionKingItems.RAFIKI_DUST.get() || item == LionKingItems.SIMBA_CHARM.get();
    }

    private static void addGreen(ITooltip tooltip, String key) {
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GREEN));
    }

    private static void addGray(ITooltip tooltip, String key) {
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }

    private static void addRed(ITooltip tooltip, String key) {
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.RED));
    }
}

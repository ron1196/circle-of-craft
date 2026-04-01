package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

public class WaywardFeatherItem extends Item {

    private static final java.util.Set<OutlandsQuestline.Stage> MARCH_STAGES =
            java.util.EnumSet.of(OutlandsQuestline.Stage.FOLLOW_OUTLANDERS, OutlandsQuestline.Stage.ZIRA_OCCUPIES_TREE);

    public WaywardFeatherItem(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) return InteractionResultHolder.success(stack);
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResultHolder.pass(stack);

        // Only works in Pride Lands or Outlands
        ResourceKey<Level> currentDim = level.dimension();
        if (currentDim != Dimensions.PRIDE_LANDS_LEVEL && currentDim != Dimensions.OUTLANDS_LEVEL) {
            return InteractionResultHolder.pass(stack);
        }

        // Disabled during the march sequence
        if (isDuringMarch(serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }

        // Toggle dimension
        ResourceKey<Level> destDim =
                (currentDim == Dimensions.OUTLANDS_LEVEL) ? Dimensions.PRIDE_LANDS_LEVEL : Dimensions.OUTLANDS_LEVEL;

        ServerLevel destLevel = serverPlayer.server.getLevel(destDim);
        if (destLevel == null) return InteractionResultHolder.fail(stack);

        // Teleport to same X/Z, surface Y
        int x = (int) player.getX();
        int z = (int) player.getZ();
        int y = destLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.PORTAL_TRIGGER,
                SoundSource.PLAYERS,
                1.0F,
                level.random.nextFloat() * 0.4F + 0.8F);

        serverPlayer.teleportTo(destLevel, x + 0.5, y, z + 0.5, player.getYRot(), player.getXRot());

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        return InteractionResultHolder.consume(stack);
    }

    private static boolean isDuringMarch(ServerPlayer player) {
        OutlandsQuestline.Stage stage = WorldData.get(player.serverLevel())
                .getQuestManager()
                .getStage("outlands", OutlandsQuestline.Stage.class);
        return MARCH_STAGES.contains(stage);
    }
}

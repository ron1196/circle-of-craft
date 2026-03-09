package io.github.ron1196.thelionking.world.feature;

import com.mojang.serialization.Codec;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Ticket Booth — small structure with Ticket Lion NPC.
 */
public class TicketBoothFeature extends Feature<NoneFeatureConfiguration> {

    public TicketBoothFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();

        BlockState pridestone = LKBlocks.PRIDESTONE.get().defaultBlockState();
        BlockState bricks = LKBlocks.PRIDE_BRICK.get().defaultBlockState();
        BlockState roof = LKBlocks.RAFIKI_WOOD.get().defaultBlockState();

        // No strict ground check — structure system handles terrain placement

        // Floor 3x3
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                level.setBlock(pos.offset(x, 0, z), pridestone, 3);
            }
        }

        // Walls — 3 high, open front (z=0 side)
        for (int y = 1; y <= 3; y++) {
            for (int x = 0; x < 3; x++) {
                // Back wall
                level.setBlock(pos.offset(x, y, 2), bricks, 3);
            }
            // Side walls
            level.setBlock(pos.offset(0, y, 0), bricks, 3);
            level.setBlock(pos.offset(0, y, 1), bricks, 3);
            level.setBlock(pos.offset(2, y, 0), bricks, 3);
            level.setBlock(pos.offset(2, y, 1), bricks, 3);
        }

        // Roof
        for (int x = -1; x <= 3; x++) {
            for (int z = -1; z <= 3; z++) {
                BlockPos roofPos = pos.offset(x, 4, z);
                if (level.getBlockState(roofPos).isAir()) {
                    level.setBlock(roofPos, roof, 3);
                }
            }
        }

        // Counter (half-slab height using a block)
        level.setBlock(pos.offset(1, 1, 0), pridestone, 3);

        // Spawn Ticket Lion inside
        if (!level.isClientSide()) {
            var ticketLion = LKEntityTypes.TICKET_LION.get().create(level.getLevel());
            if (ticketLion != null) {
                ticketLion.moveTo(pos.getX() + 1.5, pos.getY() + 1, pos.getZ() + 1.5, 180, 0);
                ticketLion.setPersistenceRequired();
                level.addFreshEntityWithPassengers(ticketLion);
            }
        }

        return true;
    }
}

package io.github.ron1196.thelionking.world.structure;

import io.github.ron1196.thelionking.registry.LKFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Optional;

/**
 * A structure piece that delegates building to an existing Feature class.
 */
public class LKStructurePiece extends StructurePiece {

    private final ResourceLocation featureId;

    public LKStructurePiece(BlockPos pos, ResourceLocation featureId) {
        super(LKStructureTypes.LK_PIECE_TYPE.get(), 0, new BoundingBox(
                pos.getX() - 16, pos.getY() - 4, pos.getZ() - 16,
                pos.getX() + 16, pos.getY() + 32, pos.getZ() + 16));
        this.featureId = featureId;
    }

    public LKStructurePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(LKStructureTypes.LK_PIECE_TYPE.get(), tag);
        this.featureId = new ResourceLocation(tag.getString("FeatureId"));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putString("FeatureId", featureId.toString());
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager,
                            ChunkGenerator generator, RandomSource random,
                            BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        Feature<NoneFeatureConfiguration> feature = resolveFeature();
        if (feature == null) return;

        FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(
                Optional.empty(), level, generator, random, pos, NoneFeatureConfiguration.INSTANCE);
        feature.place(context);
    }

    @SuppressWarnings("unchecked")
    private Feature<NoneFeatureConfiguration> resolveFeature() {
        String path = featureId.getPath();
        return switch (path) {
            case "rafiki_tree" -> LKFeatures.RAFIKI_TREE.get();
            case "zira_mound" -> LKFeatures.ZIRA_MOUND.get();
            case "ticket_booth" -> LKFeatures.TICKET_BOOTH.get();
            case "timon_pumbaa_lodge" -> LKFeatures.TIMON_PUMBAA_LODGE.get();
            case "treasure_mound" -> LKFeatures.TREASURE_MOUND.get();
            default -> null;
        };
    }
}

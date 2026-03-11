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

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A structure piece that delegates building to an existing Feature class.
 */
public class LKStructurePiece extends StructurePiece {

    private static final Logger LOGGER = LoggerFactory.getLogger(LKStructurePiece.class);

    private final ResourceLocation featureId;

    public LKStructurePiece(BlockPos pos, ResourceLocation featureId) {
        super(LKStructureTypes.LK_PIECE_TYPE.get(), 0, computeBoundingBox(pos, featureId));
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

    /**
     * Current chunk's bounding box — used by features to clip block placement per-chunk.
     * postProcess is called once per overlapping chunk; features must filter their setBlock
     * calls to only place blocks within this box.
     */
    public static final ThreadLocal<BoundingBox> CURRENT_BOX = new ThreadLocal<>();

    @Override
    public void postProcess(
            @NotNull WorldGenLevel level,
            @NotNull StructureManager structureManager,
            @NotNull ChunkGenerator generator,
            @NotNull RandomSource random,
            @NotNull BoundingBox box,
            ChunkPos chunkPos,
            @NotNull BlockPos pos
    ) {
        Feature<NoneFeatureConfiguration> feature = resolveFeature();
        if (feature == null) return;

        int originX = (this.boundingBox.minX() + this.boundingBox.maxX()) / 2;
        int originZ = (this.boundingBox.minZ() + this.boundingBox.maxZ()) / 2;
        // pos.getY() = boundingBox.minY() from placeInChunk; recover actual surface Y
        int belowY = getBelowY(featureId.getPath());
        BlockPos origin = new BlockPos(originX, pos.getY() + belowY, originZ);

        // Set the chunk box so features can clip their block placements
        CURRENT_BOX.set(box);
        try {
            FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(
                    Optional.empty(),
                    level,
                    generator,
                    random,
                    origin,
                    NoneFeatureConfiguration.INSTANCE
            );
            feature.place(context);
        } finally {
            CURRENT_BOX.remove();
        }
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

    private static int getBelowY(String path) {
        return switch (path) {
            case "zira_mound", "rafiki_tree" -> 4;
            default -> 4;
        };
    }

    private static BoundingBox computeBoundingBox(BlockPos pos, ResourceLocation featureId) {
        int halfXZ;
        int belowY;
        int aboveY;
        if ("zira_mound".equals(featureId.getPath())) {
            halfXZ = 40;
            belowY = 4;
            aboveY = 90;
        } else if ("rafiki_tree".equals(featureId.getPath())) {
            halfXZ = 40;
            belowY = 4;
            aboveY = 95;
        } else {
            halfXZ = 16;
            belowY = 4;
            aboveY = 32;
        }
        return new BoundingBox(
                pos.getX() - halfXZ, pos.getY() - belowY, pos.getZ() - halfXZ,
                pos.getX() + halfXZ, pos.getY() + aboveY, pos.getZ() + halfXZ);
    }
}

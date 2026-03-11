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

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A structure piece that delegates building to an existing Feature class.
 */
public class LKStructurePiece extends StructurePiece {

    private static final Logger LOGGER = LoggerFactory.getLogger(LKStructurePiece.class);

    /**
     * Per-structure configuration: bounding box dimensions and feature supplier.
     * belowY=0 so the bounding box bottom is at the floor level — beard_thin then
     * fills the gap between actual terrain and the floor (the vanilla approach).
     *
     * @param halfXZ  horizontal radius of the bounding box
     * @param aboveY  how far above the placement Y the bounding box extends
     * @param feature supplier for the Feature instance
     */
    private record StructureConfig(
            int halfXZ, int aboveY,
            Supplier<Feature<NoneFeatureConfiguration>> feature
    ) {
    }

    private static final StructureConfig DEFAULT_CONFIG = new StructureConfig(16, 32, () -> null);

    private static final Map<String, StructureConfig> CONFIGS = Map.of(
            "rafiki_tree", new StructureConfig(40, 95, LKFeatures.RAFIKI_TREE),
            "zira_mound", new StructureConfig(40, 90, LKFeatures.ZIRA_MOUND),
            "ticket_booth", new StructureConfig(16, 32, LKFeatures.TICKET_BOOTH),
            "timon_pumbaa_lodge", new StructureConfig(16, 32, LKFeatures.TIMON_PUMBAA_LODGE),
            "treasure_mound", new StructureConfig(16, 32, LKFeatures.TREASURE_MOUND)
    );

    private static StructureConfig configFor(String path) {
        return CONFIGS.getOrDefault(path, DEFAULT_CONFIG);
    }

    /**
     * Current chunk's bounding box — used by features to clip block placement per-chunk.
     */
    public static final ThreadLocal<BoundingBox> CURRENT_BOX = new ThreadLocal<>();

    /**
     * Returns true if the given position is within the current chunk's bounding box.
     * Use this to guard one-time actions (entity spawning, saved data writes) so they
     * only execute once across all chunk passes.
     */
    public static boolean isInCurrentChunk(BlockPos pos) {
        BoundingBox box = CURRENT_BOX.get();
        return box == null || box.isInside(pos);
    }

    /**
     * Returns true if the given position is within the current chunk's bounding box.
     * Convenience overload for raw coordinates.
     */
    public static boolean isInCurrentChunk(int x, int y, int z) {
        BoundingBox box = CURRENT_BOX.get();
        return box == null || box.isInside(x, y, z);
    }

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
    protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putString("FeatureId", featureId.toString());
    }

    @Override
    public void postProcess(
            @NotNull WorldGenLevel level,
            @NotNull StructureManager structureManager,
            @NotNull ChunkGenerator generator,
            @NotNull RandomSource random,
            @NotNull BoundingBox box,
            @NotNull ChunkPos chunkPos,
            @NotNull BlockPos pos
    ) {
        StructureConfig config = configFor(featureId.getPath());
        Feature<NoneFeatureConfiguration> feature = config.feature().get();
        if (feature == null) return;

        int originX = (this.boundingBox.minX() + this.boundingBox.maxX()) / 2;
        int originZ = (this.boundingBox.minZ() + this.boundingBox.maxZ()) / 2;
        // Bounding box bottom = floor level (belowY=0). beard_thin fills the gap
        // between actual terrain and this Y — the standard vanilla approach.
        int originY = this.boundingBox.minY();
        BlockPos origin = new BlockPos(originX, originY, originZ);

        LOGGER.info("[LKPiece] {} — originY={}, chunkBox={}", featureId, originY, box);

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

    private static BoundingBox computeBoundingBox(BlockPos pos, ResourceLocation featureId) {
        StructureConfig config = configFor(featureId.getPath());
        return new BoundingBox(
                pos.getX() - config.halfXZ(), pos.getY(), pos.getZ() - config.halfXZ(),
                pos.getX() + config.halfXZ(), pos.getY() + config.aboveY(), pos.getZ() + config.halfXZ()
        );
    }
}

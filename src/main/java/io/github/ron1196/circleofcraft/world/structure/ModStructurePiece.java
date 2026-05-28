package io.github.ron1196.circleofcraft.world.structure;

import io.github.ron1196.circleofcraft.registry.Features;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
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

/**
 * A structure piece that delegates building to an existing Feature class.
 */
public class ModStructurePiece extends StructurePiece {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModStructurePiece.class);

    /**
     * Per-structure configuration: bounding box dimensions and feature supplier.
     *
     * @param halfXZ  horizontal radius of the bounding box
     * @param yOffset offset from surface Y to the feature origin (e.g. -50 for underground mound)
     * @param aboveY  how far above the origin the bounding box extends
     * @param feature supplier for the Feature instance
     */
    private record StructureConfig(
            int halfXZ, int yOffset, int aboveY, Supplier<Feature<NoneFeatureConfiguration>> feature) {}

    public static final String RAFIKI_TREE_ID = "rafiki_tree";
    public static final String ZIRA_MOUND_ID = "zira_mound";
    public static final String TICKET_BOOTH_ID = "ticket_booth";
    public static final String TIMON_PUMBAA_LODGE_ID = "timon_pumbaa_lodge";
    public static final String TREASURE_MOUND_ID = "treasure_mound";

    private static final StructureConfig DEFAULT_CONFIG = new StructureConfig(16, 0, 32, () -> null);

    private static final Map<String, StructureConfig> CONFIGS = Map.of(
            RAFIKI_TREE_ID, new StructureConfig(40, 0, 95, Features.RAFIKI_TREE),
            ZIRA_MOUND_ID, new StructureConfig(40, -50, 55, Features.ZIRA_MOUND),
            TICKET_BOOTH_ID, new StructureConfig(16, 0, 32, Features.TICKET_BOOTH),
            TIMON_PUMBAA_LODGE_ID, new StructureConfig(16, 0, 32, Features.TIMON_PUMBAA_LODGE),
            TREASURE_MOUND_ID, new StructureConfig(16, 0, 32, Features.TREASURE_MOUND));

    private static StructureConfig configFor(String path) {
        return CONFIGS.getOrDefault(path, DEFAULT_CONFIG);
    }

    /**
     * Current chunk's bounding box — used by features to clip block placement per-chunk.
     */
    public static final ThreadLocal<BoundingBox> CURRENT_BOX = new ThreadLocal<>();

    /**
     * Returns true if the given position is within the current chunk's bounding box. Use this to
     * guard one-time actions (entity spawning, saved data writes) so they only execute once across
     * all chunk passes.
     */
    public static boolean isInCurrentChunk(BlockPos pos) {
        BoundingBox box = CURRENT_BOX.get();
        return box == null || box.isInside(pos);
    }

    /**
     * Returns true if the given position is within the current chunk's bounding box. Convenience
     * overload for raw coordinates.
     */
    public static boolean isInCurrentChunk(int x, int y, int z) {
        BoundingBox box = CURRENT_BOX.get();
        return box == null || box.isInside(x, y, z);
    }

    private final ResourceLocation featureId;

    public ModStructurePiece(BlockPos pos, ResourceLocation featureId) {
        super(StructureTypes.LK_PIECE_TYPE.get(), 0, computeBoundingBox(pos, featureId));
        this.featureId = featureId;
    }

    public ModStructurePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(StructureTypes.LK_PIECE_TYPE.get(), tag);
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
            @NotNull BlockPos pos) {
        StructureConfig config = configFor(featureId.getPath());
        Feature<NoneFeatureConfiguration> feature = config.feature().get();
        if (feature == null) return;

        int originX = (this.boundingBox.minX() + this.boundingBox.maxX()) / 2;
        int originZ = (this.boundingBox.minZ() + this.boundingBox.maxZ()) / 2;
        // Origin = surface Y + yOffset. For most structures yOffset=0 (surface level),
        // for zira_mound yOffset=-50 (mostly underground, matching the original mod).
        // beard_thin fills the gap between actual terrain and the bounding box bottom.
        int originY = this.boundingBox.minY();
        BlockPos origin = new BlockPos(originX, originY, originZ);

        CURRENT_BOX.set(box);
        try {
            FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(
                    Optional.empty(), level, generator, random, origin, NoneFeatureConfiguration.INSTANCE);
            feature.place(context);
        } finally {
            CURRENT_BOX.remove();
        }
    }

    private static BoundingBox computeBoundingBox(BlockPos pos, ResourceLocation featureId) {
        StructureConfig config = configFor(featureId.getPath());
        int originY = pos.getY() + config.yOffset();
        return new BoundingBox(
                pos.getX() - config.halfXZ(),
                originY,
                pos.getZ() - config.halfXZ(),
                pos.getX() + config.halfXZ(),
                originY + config.aboveY(),
                pos.getZ() + config.halfXZ());
    }
}

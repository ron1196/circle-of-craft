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

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A structure piece that delegates building to an existing Feature class.
 */
public class LKStructurePiece extends StructurePiece {

    /**
     * Per-structure configuration: bounding box dimensions, Y offset, and feature supplier.
     *
     * @param halfXZ  horizontal radius of the bounding box
     * @param belowY  how far below the placement Y the bounding box extends
     * @param aboveY  how far above the placement Y the bounding box extends
     * @param yOffset offset applied to recover the correct origin Y in postProcess
     *                (-1 for new features, belowY-1 for old-mod 1:1 ports)
     * @param feature supplier for the Feature instance
     */
    private record StructureConfig(int halfXZ, int belowY, int aboveY, int yOffset,
                                   Supplier<Feature<NoneFeatureConfiguration>> feature) {
    }

    private static final StructureConfig DEFAULT_CONFIG =
            new StructureConfig(16, 4, 32, -1, () -> null);

    private static final Map<String, StructureConfig> CONFIGS = Map.of(
            "rafiki_tree", new StructureConfig(40, 4, 95, 2, () -> LKFeatures.RAFIKI_TREE.get()),
            "zira_mound", new StructureConfig(40, 4, 90, 4, () -> LKFeatures.ZIRA_MOUND.get()),
            "ticket_booth", new StructureConfig(16, 4, 32, -1, () -> LKFeatures.TICKET_BOOTH.get()),
            "timon_pumbaa_lodge", new StructureConfig(16, 4, 32, -1, () -> LKFeatures.TIMON_PUMBAA_LODGE.get()),
            "treasure_mound", new StructureConfig(16, 4, 32, -1, () -> LKFeatures.TREASURE_MOUND.get())
    );

    private static StructureConfig configFor(String path) {
        return CONFIGS.getOrDefault(path, DEFAULT_CONFIG);
    }

    /**
     * Current chunk's bounding box — used by features to clip block placement per-chunk.
     * postProcess is called once per overlapping chunk; features must filter their setBlock
     * calls to only place blocks within this box.
     */
    public static final ThreadLocal<BoundingBox> CURRENT_BOX = new ThreadLocal<>();

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
        BlockPos origin = new BlockPos(originX, pos.getY() + config.yOffset(), originZ);

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
                pos.getX() - config.halfXZ(), pos.getY() - config.belowY(), pos.getZ() - config.halfXZ(),
                pos.getX() + config.halfXZ(), pos.getY() + config.aboveY(), pos.getZ() + config.halfXZ());
    }
}

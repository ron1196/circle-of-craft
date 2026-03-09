package io.github.ron1196.thelionking.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

/**
 * A structure type that places code-built landmarks (Rafiki Tree, Zira Mound, etc.)
 * The actual building is done by LKStructurePiece which delegates to existing Feature classes.
 */
public class LKCodeStructure extends Structure {

    public static final Codec<LKCodeStructure> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    settingsCodec(instance),
                    ResourceLocation.CODEC.fieldOf("feature_id").forGetter(s -> s.featureId)
            ).apply(instance, LKCodeStructure::new)
    );

    private final ResourceLocation featureId;

    public LKCodeStructure(StructureSettings settings, ResourceLocation featureId) {
        super(settings);
        this.featureId = featureId;
    }

    public ResourceLocation getFeatureId() {
        return featureId;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(
                x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()
        );

        String path = featureId.getPath();

        // Per-structure terrain validation
        if (!isValidPlacement(context, path, x, y, z)) {
            return Optional.empty();
        }

        BlockPos pos = new BlockPos(x, y, z);

        return Optional.of(new GenerationStub(pos, builder ->
                builder.addPiece(new LKStructurePiece(pos, featureId))));
    }

    private boolean isValidPlacement(GenerationContext context, String path, int x, int y, int z) {
        return switch (path) {
            // Rafiki Tree: large footprint — check all 4 corners are above sea level
            case "rafiki_tree" -> {
                int seaLevel = context.chunkGenerator().getSeaLevel();
                int radius = 6; // canopy radius
                int h1 = getHeight(context, x - radius, z - radius);
                int h2 = getHeight(context, x + radius, z - radius);
                int h3 = getHeight(context, x - radius, z + radius);
                int h4 = getHeight(context, x + radius, z + radius);
                yield Math.min(Math.min(h1, h2), Math.min(h3, h4)) > seaLevel;
            }
            // Outlands structures: Y minimum check — avoid lava lakes
            case "zira_mound", "treasure_mound" ->
                    y > context.chunkGenerator().getSeaLevel();
            // Small Pride Lands structures: heightmap-only (WORLD_SURFACE_WG already excludes liquids)
            // Just verify not at or below sea level as a safety net
            case "ticket_booth", "timon_pumbaa_lodge" ->
                    y > context.chunkGenerator().getSeaLevel();
            // Unknown structures: basic fluid check
            default -> {
                NoiseColumn column = context.chunkGenerator().getBaseColumn(
                        x, z, context.heightAccessor(), context.randomState());
                yield column.getBlock(y - 1).getFluidState().isEmpty();
            }
        };
    }

    private int getHeight(GenerationContext context, int x, int z) {
        return context.chunkGenerator().getFirstOccupiedHeight(
                x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
    }

    @Override
    public StructureType<?> type() {
        return LKStructureTypes.LK_CODE_STRUCTURE.get();
    }
}

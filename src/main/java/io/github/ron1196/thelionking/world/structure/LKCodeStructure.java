package io.github.ron1196.thelionking.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
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
                x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

        // Reject if surface is water/ocean
        NoiseColumn column = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());
        BlockState surfaceState = column.getBlock(y - 1);
        if (!surfaceState.getFluidState().isEmpty()) {
            return Optional.empty();
        }

        BlockPos pos = new BlockPos(x, y, z);

        return Optional.of(new GenerationStub(pos, builder -> {
            builder.addPiece(new LKStructurePiece(pos, featureId));
        }));
    }

    @Override
    public StructureType<?> type() {
        return LKStructureTypes.LK_CODE_STRUCTURE.get();
    }
}

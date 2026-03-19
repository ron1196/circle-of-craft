package io.github.ron1196.thelionking.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A structure type that places code-built landmarks (Rafiki Tree, Zira Mound, etc.) The actual
 * building is done by LKStructurePiece which delegates to existing Feature classes.
 */
public class LionKingStructure extends Structure {

  private static final Logger LOGGER = LoggerFactory.getLogger(LionKingStructure.class);

  public static final Codec<LionKingStructure> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      settingsCodec(instance),
                      ResourceLocation.CODEC.fieldOf("feature_id").forGetter(s -> s.featureId))
                  .apply(instance, LionKingStructure::new));

  private final ResourceLocation featureId;

  public LionKingStructure(StructureSettings settings, ResourceLocation featureId) {
    super(settings);
    this.featureId = featureId;
  }

  public ResourceLocation getFeatureId() {
    return featureId;
  }

  @Override
  protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    ChunkPos chunkPos = context.chunkPos();
    int x = chunkPos.getMiddleBlockX();
    int z = chunkPos.getMiddleBlockZ();
    int y =
        context
            .chunkGenerator()
            .getFirstOccupiedHeight(
                x,
                z,
                Heightmap.Types.OCEAN_FLOOR_WG,
                context.heightAccessor(),
                context.randomState());

    String path = featureId.getPath();

    boolean valid = isValidPlacement(context, path, x, y, z);
    if (!valid) {
      return Optional.empty();
    }

    BlockPos pos = new BlockPos(x, y, z);

    return Optional.of(
        new GenerationStub(
            pos, builder -> builder.addPiece(new LionKingStructurePiece(pos, featureId))));
  }

  private boolean isValidPlacement(GenerationContext context, String path, int x, int y, int z) {
    return LionKingPlacementStrategy.forStructure(path).isValid(context, x, y, z);
  }

  @Override
  public StructureType<?> type() {
    return StructureTypes.LK_CODE_STRUCTURE.get();
  }
}

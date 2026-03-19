package io.github.ron1196.thelionking.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.PrideBedBlockEntity;
import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PrideBedRenderer implements BlockEntityRenderer<PrideBedBlockEntity> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/pride_bed.png");

  private final ModelPart headRoot;
  private final ModelPart footRoot;

  public PrideBedRenderer(BlockEntityRendererProvider.Context context) {
    this.headRoot = context.bakeLayer(ModelLayers.BED_HEAD);
    this.footRoot = context.bakeLayer(ModelLayers.BED_FOOT);
  }

  @Override
  public void render(
      PrideBedBlockEntity blockEntity,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    Level level = blockEntity.getLevel();
    if (level != null) {
      BlockState blockstate = blockEntity.getBlockState();
      DoubleBlockCombiner.NeighborCombineResult<? extends PrideBedBlockEntity> combineResult =
          DoubleBlockCombiner.combineWithNeigbour(
              BlockEntityTypes.PRIDE_BED.get(),
              BedBlock::getBlockType,
              BedBlock::getConnectedDirection,
              ChestBlock.FACING,
              blockstate,
              level,
              blockEntity.getBlockPos(),
              (a, b) -> false);
      int brightness = combineResult.apply(new BrightnessCombiner<>()).get(packedLight);
      ModelPart part =
          blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot;
      renderPiece(
          poseStack,
          buffer,
          part,
          blockstate.getValue(BedBlock.FACING),
          brightness,
          packedOverlay,
          false);
    } else {
      renderPiece(
          poseStack, buffer, this.headRoot, Direction.SOUTH, packedLight, packedOverlay, false);
      renderPiece(
          poseStack, buffer, this.footRoot, Direction.SOUTH, packedLight, packedOverlay, true);
    }
  }

  private void renderPiece(
      PoseStack poseStack,
      MultiBufferSource buffer,
      ModelPart modelPart,
      Direction direction,
      int packedLight,
      int packedOverlay,
      boolean isFoot) {
    poseStack.pushPose();
    poseStack.translate(0.0F, 0.5625F, isFoot ? -1.0F : 0.0F);
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.translate(0.5F, 0.5F, 0.5F);
    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
    poseStack.translate(-0.5F, -0.5F, -0.5F);
    VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(TEXTURE));
    modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    poseStack.popPose();
  }
}

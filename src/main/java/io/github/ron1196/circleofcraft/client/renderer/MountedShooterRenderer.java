package io.github.ron1196.circleofcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.MountedShooterBlock;
import io.github.ron1196.circleofcraft.block.entity.MountedShooterBlockEntity;
import io.github.ron1196.circleofcraft.client.model.MountedShooterModel;
import io.github.ron1196.circleofcraft.event.ClientEvents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MountedShooterRenderer implements BlockEntityRenderer<MountedShooterBlockEntity> {

    private static final ResourceLocation TEXTURE = CircleOfCraftMod.id("textures/entity/mounted_shooter.png");

    private final MountedShooterModel model;

    public MountedShooterRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new MountedShooterModel(context.bakeLayer(ClientEvents.MOUNTED_SHOOTER_LAYER));
    }

    @Override
    public void render(
            @NotNull MountedShooterBlockEntity be,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {

        Direction facing = be.getBlockState().getValue(MountedShooterBlock.FACING);
        float yRot = getYRotation(facing);

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.scale(-1F, -1F, 1F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        var buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, be.getFireCounter());

        poseStack.popPose();
    }

    private static float getYRotation(Direction facing) {
        return switch (facing) {
            case NORTH -> 0F;
            case SOUTH -> 180F;
            case WEST -> 90F;
            case EAST -> -90F;
            default -> 0F;
        };
    }
}

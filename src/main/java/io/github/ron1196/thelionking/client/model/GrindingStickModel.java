package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

/**
 * Grinding stick model — a simple tilted stick that rotates inside the grinding bowl.
 * Ported from old mod's LKModelGrindingStick.
 */
public class GrindingStickModel extends Model {

    private final ModelPart stick;

    public GrindingStickModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.stick = root.getChild("stick");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild(
                "stick",
                CubeListBuilder.create().addBox(-0.5F, -11F, -0.5F, 1, 12, 1),
                PartPose.offsetAndRotation(0F, 18F, 0F, (float) Math.toRadians(-35), 0F, 0F));
        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack poseStack,
            @NotNull VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        stick.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

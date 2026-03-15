package io.github.ron1196.thelionking.client.model;

import io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaHeadEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class SkeletalHyenaHeadModel extends HierarchicalModel<SkeletalHyenaHeadEntity> {

    private final ModelPart root;

    public SkeletalHyenaHeadModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6)
                        .texOffs(0, 15).addBox(-3.0F, -8.0F, 0.0F, 1, 2, 2)
                        .texOffs(6, 15).addBox(2.0F, -8.0F, 0.0F, 1, 2, 2),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(@NotNull SkeletalHyenaHeadEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Static head - no animation needed
    }
}

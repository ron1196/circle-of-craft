package io.github.ron1196.thelionking.client.model;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.LKAnimal;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

/**
 * Generic quadruped model used as a placeholder for all LK animals.
 * Individual models will be refined in later phases.
 */
public class LKAnimalModel<T extends LKAnimal> extends QuadrupedModel<T> {

    public LKAnimalModel(ModelPart root) {
        super(root, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public static LayerDefinition createBodyLayer(float bodyWidth, float bodyHeight, float legLength) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 24.0F - bodyHeight - legLength - 3.0F, -bodyWidth / 2));

        // Body
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(-bodyWidth / 2, -bodyHeight / 2, -bodyWidth / 2,
                                bodyWidth, bodyHeight, bodyWidth),
                PartPose.offset(0.0F, 24.0F - legLength - bodyHeight / 2, 0.0F));

        // Legs
        float legY = 24.0F - legLength;
        root.addOrReplaceChild("right_hind_leg",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, legLength, 2.0F),
                PartPose.offset(-2.0F, legY, bodyWidth / 2 - 1.0F));
        root.addOrReplaceChild("left_hind_leg",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, legLength, 2.0F),
                PartPose.offset(2.0F, legY, bodyWidth / 2 - 1.0F));
        root.addOrReplaceChild("right_front_leg",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, legLength, 2.0F),
                PartPose.offset(-2.0F, legY, -bodyWidth / 2 + 1.0F));
        root.addOrReplaceChild("left_front_leg",
                CubeListBuilder.create().texOffs(0, 28)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, legLength, 2.0F),
                PartPose.offset(2.0F, legY, -bodyWidth / 2 + 1.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    // Layer locations for each entity
    public static final ModelLayerLocation LION_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "lion"), "main");
    public static final ModelLayerLocation LIONESS_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "lioness"), "main");
    public static final ModelLayerLocation ZEBRA_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "zebra"), "main");
    public static final ModelLayerLocation GIRAFFE_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "giraffe"), "main");
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "rhino"), "main");
    public static final ModelLayerLocation GEMSBOK_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "gemsbok"), "main");
    public static final ModelLayerLocation DIKDIK_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "dikdik"), "main");
    public static final ModelLayerLocation FLAMINGO_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "flamingo"), "main");
    public static final ModelLayerLocation ZAZU_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "zazu"), "main");
    public static final ModelLayerLocation BUG_LAYER = new ModelLayerLocation(
            new ResourceLocation(TheLionKingMod.MOD_ID, "bug_entity"), "main");
}

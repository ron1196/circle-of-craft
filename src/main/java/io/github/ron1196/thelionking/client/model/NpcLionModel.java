package io.github.ron1196.thelionking.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

/**
 * Lion model for NPC entities (Scar, Zira, Ticket Lion). Typed to Mob instead of LionKingAnimal
 * because NPCs extend PathfinderMob, not LionKingAnimal.
 */
public class NpcLionModel extends EntityModel<Mob> {

    private final ModelPart root;
    private final ModelPart head;

    public NpcLionModel(ModelPart root) {
        this.root = root;
        // Try to find common parts for animation
        this.head = root.hasChild("head") ? root.getChild("head") : null;
    }

    @Override
    public void setupAnim(
            @NotNull Mob entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // Animate head if present
        if (head != null) {
            head.xRot = headPitch * ((float) Math.PI / 180F);
            head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        }

        // Animate legs if present
        animateLeg("leg1", root, limbSwing, limbSwingAmount, false);
        animateLeg("leg2", root, limbSwing, limbSwingAmount, true);
        animateLeg("leg3", root, limbSwing, limbSwingAmount, true);
        animateLeg("leg4", root, limbSwing, limbSwingAmount, false);

        // Also animate headwear/mane to match head
        if (head != null) {
            copyRotation("headwear", root, head);
            copyRotation("mane", root, head);
        }

        // Set body rotation if it exists
        if (root.hasChild("body")) {
            root.getChild("body").xRot = ((float) Math.PI / 2F);
        }
    }

    private void animateLeg(String name, ModelPart root, float limbSwing, float limbSwingAmount, boolean inverted) {
        if (root.hasChild(name)) {
            float phase = inverted ? (float) Math.PI : 0;
            root.getChild(name).xRot = Mth.cos(limbSwing * 0.6662F + phase) * 1.4F * limbSwingAmount;
        }
    }

    private void copyRotation(String name, ModelPart root, ModelPart source) {
        if (root.hasChild(name)) {
            ModelPart part = root.getChild(name);
            part.xRot = source.xRot;
            part.yRot = source.yRot;
        }
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
        root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

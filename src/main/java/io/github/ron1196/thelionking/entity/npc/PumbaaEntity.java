package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.entity.ai.PumbaaFollowTimonGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PumbaaEntity extends PathfinderMob {

    private int talkCooldown = 0;

    public PumbaaEntity(EntityType<? extends PumbaaEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PumbaaFollowTimonGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false; // Invulnerable NPC
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;

        // Random fart particles
        if (level().isClientSide() && random.nextInt(1200) == 0) {
            for (int i = 0; i < 5; i++) {
                level().addParticle(ParticleTypes.SMOKE,
                        getX() - 0.5 + random.nextFloat(),
                        getY() + 0.5 + random.nextFloat(),
                        getZ() - 0.5 + random.nextFloat(),
                        0, 0.05, 0);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        talkCooldown = 140;

        String[] speeches = {
            "They call me... Mr. Pig!",
            "Hakuna Matata! What a wonderful phrase!",
            "Are you talking to me?",
            "Timon! There's a human looking at me!",
            "I'm a sensitive soul, though I seem thick-skinned.",
            "When I was a young warthog...",
            "It's our problem-free philosophy!"
        };
        player.sendSystemMessage(Component.literal(
                "\u00a7e<Pumbaa> \u00a7f" + speeches[random.nextInt(speeches.length)]));
        return InteractionResult.SUCCESS;
    }
}

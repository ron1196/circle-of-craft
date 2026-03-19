package io.github.ron1196.thelionking.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A portal particle with configurable color, mimicking vanilla's PortalParticle behavior. The
 * particle starts at a given position, drifts toward its origin point, and fades over its lifetime.
 */
public class ColoredPortalParticle extends TextureSheetParticle {

    private static final float BRIGHTNESS_VARIATION = 0.6F;
    private static final float BRIGHTNESS_BASE = 0.4F;
    private static final float INITIAL_SIZE = 0.01F;
    private static final float SIZE_GROWTH = 0.02F;
    private static final float MAX_SIZE = 0.08F;
    private static final int BASE_LIFETIME = 8;
    private static final int RANDOM_LIFETIME = 24;
    private static final double FACE_SPREAD = 1.0;
    private static final double EDGE_OFFSET = 0.4;
    private static final double DRIFT_SPEED = 0.04;

    private final double originX;
    private final double originY;
    private final double originZ;

    private ColoredPortalParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            float red,
            float green,
            float blue) {
        super(level, x, y, z);
        this.originX = x;
        this.originY = y;
        this.originZ = z;

        // Offset spawn position outward from the portal in all directions
        int sign = this.random.nextInt(2) * 2 - 1;
        this.x += EDGE_OFFSET * sign + this.random.nextDouble() * FACE_SPREAD * sign;
        this.y += (this.random.nextDouble() - 0.5) * FACE_SPREAD * sign;
        this.z += EDGE_OFFSET * sign + this.random.nextDouble() * FACE_SPREAD * sign;

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.quadSize = INITIAL_SIZE * (this.random.nextFloat() * 0.2F + 0.5F);
        this.lifetime = (int) (Math.random() * RANDOM_LIFETIME) + BASE_LIFETIME;

        float brightness = this.random.nextFloat() * BRIGHTNESS_VARIATION + BRIGHTNESS_BASE;
        this.rCol = red * brightness;
        this.gCol = green * brightness;
        this.bCol = blue * brightness;
    }

    @Override
    public float getQuadSize(float partialTick) {
        float progress = (this.age + partialTick) / (float) this.lifetime;
        progress = 1.0F - progress;
        progress *= progress;
        progress = 1.0F - progress;
        return this.quadSize * progress;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // Drift toward origin at a constant speed
        double dx = this.originX - this.x;
        double dy = this.originY - this.y;
        double dz = this.originZ - this.z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist > DRIFT_SPEED) {
            double scale = DRIFT_SPEED / dist;
            this.x += dx * scale;
            this.y += dy * scale;
            this.z += dz * scale;
        }

        this.quadSize = Math.min(this.quadSize + SIZE_GROWTH, MAX_SIZE);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        int original = super.getLightColor(partialTick);
        int skyLight = original >> 16 & 0xFF;
        return 0xF0 | (skyLight << 16);
    }

    /**
     * Factory that produces portal particles with a fixed color. One instance is created per
     * particle type registration (Pride Lands gold, Outlands red, etc.).
     */
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final float red;
        private final float green;
        private final float blue;

        public Provider(SpriteSet sprites, float red, float green, float blue) {
            this.sprites = sprites;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @Override
        public @Nullable Particle createParticle(
                @NotNull SimpleParticleType type,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double vx,
                double vy,
                double vz) {
            var particle = new ColoredPortalParticle(level, x, y, z, red, green, blue);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}

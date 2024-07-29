package mod.ginleaf.placer.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class PlacerVentParticle extends AscendingParticle {
    protected PlacerVentParticle(ClientWorld world, double x, double y, double z, double dx, double dy, double dz, float scale, SpriteProvider sprite) {
        super(world,x,y,z,0.1f,0.1f,0.1f,dx,dy,dz,scale,sprite, 1.0f, 8, -0.035f, true);
        this.velocityMultiplier = 0.95f;
        float f;
        this.red = f = 0.6f + world.random.nextFloat() / 3.0f;
        this.blue = f;
        this.green = f;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType>
    {
        private final SpriteProvider provider;

        public Factory(SpriteProvider provider)
        {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double deltaX, double deltaY, double deltaZ)
        {
            return new PlacerVentParticle(world, x, y, z, deltaX, deltaY, deltaZ, 5f, this.provider);
        }
    }
}

package net.ginleaf.testmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.ginleaf.testmod.particle.PlacerVentParticle;
import net.ginleaf.testmod.screen.PlacerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class TestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(TestMod.PLACER_SCREEN_HANDLER, PlacerScreen::new);
        ParticleFactoryRegistry.getInstance().register(TestMod.VENT_SMOKE, PlacerVentParticle.Factory::new);
    }
}

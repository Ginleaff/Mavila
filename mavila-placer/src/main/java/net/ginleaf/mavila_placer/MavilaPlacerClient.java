package net.ginleaf.mavila_placer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.ginleaf.mavila_placer.particle.PlacerVentParticle;
import net.ginleaf.mavila_placer.screen.PlacerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class MavilaPlacerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(MavilaPlacer.PLACER_SCREEN_HANDLER, PlacerScreen::new);
        ParticleFactoryRegistry.getInstance().register(MavilaPlacer.VENT_SMOKE, PlacerVentParticle.Factory::new);
    }
}

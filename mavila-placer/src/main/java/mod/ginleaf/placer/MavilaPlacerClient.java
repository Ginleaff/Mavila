package mod.ginleaf.placer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import mod.ginleaf.placer.particle.PlacerVentParticle;
import mod.ginleaf.placer.screen.PlacerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class MavilaPlacerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(MavilaPlacer.PLACER_BLOCK_SCREEN_HANDLER, PlacerScreen::new);
        ParticleFactoryRegistry.getInstance().register(MavilaPlacer.VENT_SMOKE, PlacerVentParticle.Factory::new);
    }
}

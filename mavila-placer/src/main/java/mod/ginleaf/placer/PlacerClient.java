package mod.ginleaf.placer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import mod.ginleaf.placer.particle.PlacerVentParticle;
import mod.ginleaf.placer.screen.PlacerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class PlacerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(Placer.PLACER_SCREEN_HANDLER, PlacerScreen::new);
        ParticleFactoryRegistry.getInstance().register(Placer.VENT_SMOKE, PlacerVentParticle.Factory::new);
    }
}

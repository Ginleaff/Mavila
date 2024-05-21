package net.ginleaf.testmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ginleaf.testmod.screen.PlacerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class TestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(TestMod.PLACER_SCREEN_HANDLER, PlacerScreen::new);
    }
}

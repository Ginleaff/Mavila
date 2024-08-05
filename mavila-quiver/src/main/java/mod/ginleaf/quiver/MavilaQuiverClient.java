package mod.ginleaf.quiver;

import mod.ginleaf.quiver.item.QuiverItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MavilaQuiverClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(MavilaQuiver.QUIVER, Identifier.ofVanilla("filled"), (itemStack, clientWorld, livingEntity, seed) -> (float) QuiverItem.getQuiverCapacity(itemStack));
    }
}

package mod.ginleaf.quiver;

import mod.ginleaf.quiver.item.QuiverItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quiver implements ModInitializer {

	public static final String MOD_ID = "mavila_quiver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Identifier QUIVER_ID = Identifier.of(MOD_ID, "quiver");
	public static final QuiverItem QUIVER = new QuiverItem(new Item.Settings()
			.maxCount(1)
			.component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT));

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, QUIVER_ID, QUIVER);
		LOGGER.info("Sub-Mavila Mod: "+ MOD_ID + " | Success");
	}
}
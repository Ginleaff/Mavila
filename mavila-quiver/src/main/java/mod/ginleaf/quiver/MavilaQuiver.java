package mod.ginleaf.quiver;

import mod.ginleaf.quiver.component.AdjustableBundleComponent;
import mod.ginleaf.quiver.item.QuiverItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MavilaQuiver implements ModInitializer {

	public static final String MOD_ID = "mavila_quiver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier QUIVER_ITEM_ID = Identifier.of(MOD_ID, "quiver");
	public static final Identifier INSPECT_QUIVER_ITEM = Identifier.of(MOD_ID, "inspect_quiver");
	public static final ComponentType<AdjustableBundleComponent> ADJUSTABLE_BUNDLE_CONTENTS = ComponentType.<AdjustableBundleComponent>builder()
			.codec(AdjustableBundleComponent.CODEC).packetCodec(AdjustableBundleComponent.PACKET_CODEC).build();
	public static final QuiverItem QUIVER = new QuiverItem(new Item.Settings()
			.maxCount(1)
			.component(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, new AdjustableBundleComponent(List.of(), 256)));
	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, QUIVER_ITEM_ID, QUIVER);
		Registry.register(Registries.DATA_COMPONENT_TYPE, QUIVER_ITEM_ID, ADJUSTABLE_BUNDLE_CONTENTS);
		Registry.register(Registries.CUSTOM_STAT, INSPECT_QUIVER_ITEM.getPath(), INSPECT_QUIVER_ITEM);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.addAfter(Items.LEAD, QUIVER));
		LOGGER.info(MOD_ID + " | Loaded Successfully");
	}
}
package mod.ginleaf.placer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import mod.ginleaf.placer.block.PlacerBlock;
import mod.ginleaf.placer.block.entity.PlacerBlockEntity;
import mod.ginleaf.placer.screen.PlacerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.*;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavilaPlacer implements ModInitializer {

	public static final String MOD_ID = "mavila_placer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier PLACER_ID = Identifier.of(MOD_ID, "placer");
	public static final Identifier INSPECT_PLACER_BLOCK = Identifier.of(MOD_ID, "inspect_placer");
	public static final PlacerBlock PLACER_BLOCK = new PlacerBlock(Block.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool().strength(3.5f)
			.pistonBehavior(PistonBehavior.BLOCK));
	public static final BlockItem PLACER_ITEM = new BlockItem(PLACER_BLOCK, new Item.Settings());
	public static final BlockEntityType<PlacerBlockEntity> PLACER_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(PlacerBlockEntity::new, PLACER_BLOCK).build();
	public static final ScreenHandlerType<PlacerScreenHandler> PLACER_BLOCK_SCREEN_HANDLER = new ScreenHandlerType<>(PlacerScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static final SimpleParticleType VENT_SMOKE = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, PLACER_ID, PLACER_BLOCK);
		Registry.register(Registries.ITEM, PLACER_ID, PLACER_ITEM);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, PLACER_ID, PLACER_BLOCK_ENTITY_TYPE);
		Registry.register(Registries.SCREEN_HANDLER, PLACER_ID, PLACER_BLOCK_SCREEN_HANDLER);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID,"vent_smoke"), VENT_SMOKE);
		Registry.register(Registries.CUSTOM_STAT, INSPECT_PLACER_BLOCK.getPath(), INSPECT_PLACER_BLOCK);
		Stats.CUSTOM.getOrCreateStat(INSPECT_PLACER_BLOCK, StatFormatter.DEFAULT);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((itemGroup) -> itemGroup.addAfter(Items.DROPPER, PLACER_ITEM));
		LOGGER.info(MOD_ID + " | Loaded Successfully");
	}
}
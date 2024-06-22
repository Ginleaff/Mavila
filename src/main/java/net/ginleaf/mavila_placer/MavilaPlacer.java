package net.ginleaf.mavila_placer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.ginleaf.mavila_placer.block.PlacerBlock;
import net.ginleaf.mavila_placer.block.entity.PlacerBlockEntity;
import net.ginleaf.mavila_placer.screen.PlacerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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
	public static final Identifier INSPECT_PLACER = Identifier.of(MOD_ID, "inspect_placer");
	public static final PlacerBlock PLACER = new PlacerBlock(Block.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool().strength(3.5f)
			.pistonBehavior(PistonBehavior.BLOCK));
	public static final BlockEntityType<PlacerBlockEntity> PLACER_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(PlacerBlockEntity::new, PLACER).build();
	public static final ScreenHandlerType<PlacerScreenHandler> PLACER_SCREEN_HANDLER = new ScreenHandlerType<>(PlacerScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static final SimpleParticleType VENT_SMOKE = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, PLACER_ID, PLACER);
		Registry.register(Registries.ITEM, PLACER_ID, new BlockItem(PLACER, new Item.Settings()));
		Registry.register(Registries.BLOCK_ENTITY_TYPE, PLACER_ID, PLACER_BLOCK_ENTITY_TYPE);
		Registry.register(Registries.SCREEN_HANDLER, PLACER_ID, PLACER_SCREEN_HANDLER);
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID,"vent_smoke"), VENT_SMOKE);
		Registry.register(Registries.CUSTOM_STAT, INSPECT_PLACER.getPath(), INSPECT_PLACER);
		Stats.CUSTOM.getOrCreateStat(INSPECT_PLACER, StatFormatter.DEFAULT);
		LOGGER.info("Sub-Mavilla Mod: "+ MOD_ID + " | Success");
	}
}
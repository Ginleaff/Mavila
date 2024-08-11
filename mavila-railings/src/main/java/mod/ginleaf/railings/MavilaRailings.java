package mod.ginleaf.railings;

import mod.ginleaf.railings.block.RailingBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavilaRailings implements ModInitializer {

	public static final String MOD_ID = "mavila_railings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Identifier WHITE_RAILING_ID = Identifier.of(MOD_ID, "white_railing");
	private static final Identifier ORANGE_RAILING_ID = Identifier.of(MOD_ID, "orange_railing");
	private static final Identifier MAGENTA_RAILING_ID = Identifier.of(MOD_ID, "magenta_railing");
	private static final Identifier LIGHT_BLUE_RAILING_ID = Identifier.of(MOD_ID, "light_blue_railing");
	private static final Identifier YELLOW_RAILING_ID = Identifier.of(MOD_ID, "yellow_railing");
	private static final Identifier LIME_RAILING_ID = Identifier.of(MOD_ID, "lime_railing");
	private static final Identifier PINK_RAILING_ID = Identifier.of(MOD_ID, "pink_railing");
	private static final Identifier GRAY_RAILING_ID = Identifier.of(MOD_ID, "gray_railing");
	private static final Identifier LIGHT_GRAY_RAILING_ID = Identifier.of(MOD_ID, "light_gray_railing");
	private static final Identifier CYAN_RAILING_ID = Identifier.of(MOD_ID, "cyan_railing");
	private static final Identifier PURPLE_RAILING_ID = Identifier.of(MOD_ID, "purple_railing");
	private static final Identifier BLUE_RAILING_ID = Identifier.of(MOD_ID, "blue_railing");
	private static final Identifier BROWN_RAILING_ID = Identifier.of(MOD_ID, "brown_railing");
	private static final Identifier GREEN_RAILING_ID = Identifier.of(MOD_ID, "green_railing");
	private static final Identifier RED_RAILING_ID = Identifier.of(MOD_ID, "red_railing");
	private static final Identifier BLACK_RAILING_ID = Identifier.of(MOD_ID, "black_railing");

	public static final RailingBlock WHITE_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.WHITE).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock ORANGE_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.ORANGE).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock MAGENTA_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.MAGENTA).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock LIGHT_BLUE_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.ORANGE).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock YELLOW_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.YELLOW).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock LIME_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.LIME).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock PINK_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.PINK).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock GRAY_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.GRAY).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock LIGHT_GRAY_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.LIGHT_GRAY).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock CYAN_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.CYAN).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock PURPLE_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.PURPLE).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock BLUE_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.BROWN).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock BROWN_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.ORANGE).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock GREEN_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.GREEN).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock RED_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.RED).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));
	public static final RailingBlock BLACK_RAILING_BLOCK = new RailingBlock(Block.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.COPPER).nonOpaque().solidBlock(Blocks::never).suffocates(Blocks::never).pistonBehavior(PistonBehavior.DESTROY).blockVision(Blocks::never));

	public static final BlockItem WHITE_RAILING_ITEM = new BlockItem(WHITE_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem ORANGE_RAILING_ITEM = new BlockItem(ORANGE_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem MAGENTA_RAILING_ITEM = new BlockItem(MAGENTA_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem LIGHT_BLUE_RAILING_ITEM = new BlockItem(LIGHT_BLUE_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem YELLOW_RAILING_ITEM = new BlockItem(YELLOW_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem LIME_RAILING_ITEM = new BlockItem(LIME_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem PINK_RAILING_ITEM = new BlockItem(PINK_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem GRAY_RAILING_ITEM = new BlockItem(GRAY_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem LIGHT_GRAY_RAILING_ITEM = new BlockItem(LIGHT_GRAY_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem CYAN_RAILING_ITEM = new BlockItem(CYAN_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem PURPLE_RAILING_ITEM = new BlockItem(PURPLE_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem BLUE_RAILING_ITEM = new BlockItem(BLUE_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem BROWN_RAILING_ITEM = new BlockItem(BROWN_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem GREEN_RAILING_ITEM = new BlockItem(GREEN_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem RED_RAILING_ITEM = new BlockItem(RED_RAILING_BLOCK, new Item.Settings());
	public static final BlockItem BLACK_RAILING_ITEM = new BlockItem(BLACK_RAILING_BLOCK, new Item.Settings());

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, WHITE_RAILING_ID, WHITE_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, ORANGE_RAILING_ID, ORANGE_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, MAGENTA_RAILING_ID, MAGENTA_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, LIGHT_BLUE_RAILING_ID, LIGHT_BLUE_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, YELLOW_RAILING_ID, YELLOW_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, LIME_RAILING_ID, LIME_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, PINK_RAILING_ID, PINK_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, GRAY_RAILING_ID, GRAY_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, LIGHT_GRAY_RAILING_ID, LIGHT_GRAY_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, CYAN_RAILING_ID, CYAN_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, PURPLE_RAILING_ID, PURPLE_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, BLUE_RAILING_ID, BLUE_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, BROWN_RAILING_ID, BROWN_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, GREEN_RAILING_ID, GREEN_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, RED_RAILING_ID, RED_RAILING_BLOCK);
		Registry.register(Registries.BLOCK, BLACK_RAILING_ID, BLACK_RAILING_BLOCK);

		Registry.register(Registries.ITEM, WHITE_RAILING_ID, WHITE_RAILING_ITEM);
		Registry.register(Registries.ITEM, ORANGE_RAILING_ID, ORANGE_RAILING_ITEM);
		Registry.register(Registries.ITEM, MAGENTA_RAILING_ID, MAGENTA_RAILING_ITEM);
		Registry.register(Registries.ITEM, LIGHT_BLUE_RAILING_ID, LIGHT_BLUE_RAILING_ITEM);
		Registry.register(Registries.ITEM, YELLOW_RAILING_ID, YELLOW_RAILING_ITEM);
		Registry.register(Registries.ITEM, LIME_RAILING_ID, LIME_RAILING_ITEM);
		Registry.register(Registries.ITEM, PINK_RAILING_ID, PINK_RAILING_ITEM);
		Registry.register(Registries.ITEM, GRAY_RAILING_ID, GRAY_RAILING_ITEM);
		Registry.register(Registries.ITEM, LIGHT_GRAY_RAILING_ID, LIGHT_GRAY_RAILING_ITEM);
		Registry.register(Registries.ITEM, CYAN_RAILING_ID, CYAN_RAILING_ITEM);
		Registry.register(Registries.ITEM, PURPLE_RAILING_ID, PURPLE_RAILING_ITEM);
		Registry.register(Registries.ITEM, BLUE_RAILING_ID, BLUE_RAILING_ITEM);
		Registry.register(Registries.ITEM, BROWN_RAILING_ID, BROWN_RAILING_ITEM);
		Registry.register(Registries.ITEM, GREEN_RAILING_ID, GREEN_RAILING_ITEM);
		Registry.register(Registries.ITEM, RED_RAILING_ID, RED_RAILING_ITEM);
		Registry.register(Registries.ITEM, BLACK_RAILING_ID, BLACK_RAILING_ITEM);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(WHITE_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(LIGHT_GRAY_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(GRAY_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(BLACK_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(BROWN_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(RED_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(ORANGE_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(YELLOW_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(LIME_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(GREEN_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(CYAN_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(LIGHT_BLUE_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(BLUE_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(PURPLE_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(MAGENTA_RAILING_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register((itemGroup) -> itemGroup.add(PINK_RAILING_ITEM));

		LOGGER.info(MOD_ID + " | Loaded Successfully");
	}
}
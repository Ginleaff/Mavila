package mod.ginleaf.railings;

import mod.ginleaf.railings.block.HorizontalBorderBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavilaRailings implements ModInitializer {

	public static final String MOD_ID = "mavila_quiver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier RAILINGS_ID = Identifier.of(MOD_ID, "railings");
	public static final HorizontalBorderBlock TEST_BLOCK = new HorizontalBorderBlock(Block.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool().strength(3.5f));
	public static final BlockItem TEST_ITEM = new BlockItem(TEST_BLOCK, new Item.Settings());

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, RAILINGS_ID, TEST_BLOCK);
		Registry.register(Registries.ITEM, RAILINGS_ID, TEST_ITEM);
		LOGGER.info(MOD_ID + " | Loaded Successfully");
	}
}
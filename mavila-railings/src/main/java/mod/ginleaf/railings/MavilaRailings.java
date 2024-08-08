package mod.ginleaf.railings;

import mod.ginleaf.railings.block.HorizontalBorderBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavilaRailings implements ModInitializer {

	public static final String MOD_ID = "mavila_railings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier TEST_RAILING_ID = Identifier.of(MOD_ID, "test_railing");
	public static final HorizontalBorderBlock TEST_BLOCK = new HorizontalBorderBlock(AbstractBlock.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.sounds(BlockSoundGroup.METAL)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool().strength(3.5f));
	public static final BlockItem TEST_ITEM = new BlockItem(TEST_BLOCK, new Item.Settings());

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, TEST_RAILING_ID, TEST_BLOCK);
		Registry.register(Registries.ITEM, TEST_RAILING_ID, TEST_ITEM);
		LOGGER.info(MOD_ID + " | Loaded Successfully");
	}
}
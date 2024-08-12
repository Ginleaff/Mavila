package mod.ginleaf.railings.datagen;

import mod.ginleaf.railings.MavilaRailings;
import mod.ginleaf.railings.block.RailingBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class RailingsLootTableProvider extends FabricBlockLootTableProvider {
    public RailingsLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(MavilaRailings.WHITE_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.WHITE_RAILING_BLOCK, MavilaRailings.WHITE_RAILING_ITEM));
        addDrop(MavilaRailings.ORANGE_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.ORANGE_RAILING_BLOCK, MavilaRailings.ORANGE_RAILING_ITEM));
        addDrop(MavilaRailings.MAGENTA_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.MAGENTA_RAILING_BLOCK, MavilaRailings.MAGENTA_RAILING_ITEM));
        addDrop(MavilaRailings.LIGHT_BLUE_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.LIGHT_BLUE_RAILING_BLOCK, MavilaRailings.LIGHT_BLUE_RAILING_ITEM));
        addDrop(MavilaRailings.YELLOW_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.YELLOW_RAILING_BLOCK, MavilaRailings.YELLOW_RAILING_ITEM));
        addDrop(MavilaRailings.LIME_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.LIME_RAILING_BLOCK, MavilaRailings.LIME_RAILING_ITEM));
        addDrop(MavilaRailings.PINK_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.PINK_RAILING_BLOCK, MavilaRailings.PINK_RAILING_ITEM));
        addDrop(MavilaRailings.GRAY_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.GRAY_RAILING_BLOCK, MavilaRailings.GRAY_RAILING_ITEM));
        addDrop(MavilaRailings.LIGHT_GRAY_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.LIGHT_GRAY_RAILING_BLOCK, MavilaRailings.LIGHT_GRAY_RAILING_ITEM));
        addDrop(MavilaRailings.CYAN_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.CYAN_RAILING_BLOCK, MavilaRailings.CYAN_RAILING_ITEM));
        addDrop(MavilaRailings.PURPLE_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.PURPLE_RAILING_BLOCK, MavilaRailings.PURPLE_RAILING_ITEM));
        addDrop(MavilaRailings.BLUE_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.BLUE_RAILING_BLOCK, MavilaRailings.BLUE_RAILING_ITEM));
        addDrop(MavilaRailings.BROWN_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.BROWN_RAILING_BLOCK, MavilaRailings.BROWN_RAILING_ITEM));
        addDrop(MavilaRailings.GREEN_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.GREEN_RAILING_BLOCK, MavilaRailings.GREEN_RAILING_ITEM));
        addDrop(MavilaRailings.RED_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.RED_RAILING_BLOCK, MavilaRailings.RED_RAILING_ITEM));
        addDrop(MavilaRailings.BLACK_RAILING_BLOCK, flowerbedToRailingsDrops(MavilaRailings.BLACK_RAILING_BLOCK, MavilaRailings.BLACK_RAILING_ITEM));
    }

    public LootTable.Builder flowerbedToRailingsDrops(Block drop, Item item) {
        return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                .with(this.applyExplosionDecay(drop, ItemEntry.builder(item)
                        .apply(IntStream.rangeClosed(1, 4).boxed().toList(), (amount) ->
                                SetCountLootFunction.builder(ConstantLootNumberProvider.create((float) amount))
                                        .conditionally(BlockStatePropertyLootCondition.builder(drop)
                                                .properties(StatePredicate.Builder.create()
                                                        .exactMatch(RailingBlock.RAILING_AMOUNT, amount)))))));
    }
}

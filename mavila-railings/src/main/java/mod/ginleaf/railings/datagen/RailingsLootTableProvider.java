package mod.ginleaf.railings.datagen;

import mod.ginleaf.railings.MavilaRailings;
import mod.ginleaf.railings.block.HorizontalBorderBlock;
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
        addDrop(MavilaRailings.TEST_BLOCK, flowerbedToRailingsDrops(MavilaRailings.TEST_BLOCK, MavilaRailings.TEST_ITEM));
    }

    public LootTable.Builder flowerbedToRailingsDrops(Block drop, Item item) {
        return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                .with(this.applyExplosionDecay(drop, ItemEntry.builder(item)
                        .apply(IntStream.rangeClosed(1, 4).boxed().toList(), (amount) ->
                                SetCountLootFunction.builder(ConstantLootNumberProvider.create((float) amount))
                                        .conditionally(BlockStatePropertyLootCondition.builder(drop)
                                                .properties(StatePredicate.Builder.create()
                                                        .exactMatch(HorizontalBorderBlock.RAILING_AMOUNT, amount)))))));
    }
}

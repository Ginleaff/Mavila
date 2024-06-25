package net.ginleaf.mavila_placer.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.ginleaf.mavila_placer.MavilaPlacer;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class PlacerRecipeProvider extends FabricRecipeProvider {
    public PlacerRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, MavilaPlacer.PLACER)
                .input('#', Items.COBBLESTONE)
                .input('X', Items.PISTON)
                .input('R', Items.REDSTONE)
                .pattern("###")
                .pattern("#X#")
                .pattern("#R#")
                .criterion(FabricRecipeProvider.hasItem(Items.COBBLESTONE), FabricRecipeProvider.conditionsFromItem(Items.COBBLESTONE))
                .criterion(FabricRecipeProvider.hasItem(Items.PISTON), FabricRecipeProvider.conditionsFromItem(Items.PISTON))
                .criterion(FabricRecipeProvider.hasItem(Items.COBBLESTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter);


    }
}

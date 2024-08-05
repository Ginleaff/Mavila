package mod.ginleaf.quiver.datagen;

import mod.ginleaf.quiver.MavilaQuiver;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class QuiverItemRecipeProvider extends FabricRecipeProvider {
    public QuiverItemRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, MavilaQuiver.QUIVER)
                .input('R', Items.RABBIT_HIDE)
                .input('F', Items.FEATHER)
                .input('I', Items.IRON_INGOT)
                .input('S', Items.STRING)
                .pattern("SRF")
                .pattern("RFR")
                .pattern("IR ")
                .criterion(FabricRecipeProvider.hasItem(Items.RABBIT_HIDE), FabricRecipeProvider.conditionsFromItem(Items.RABBIT_HIDE))
                .criterion(FabricRecipeProvider.hasItem(Items.FEATHER), FabricRecipeProvider.conditionsFromItem(Items.FEATHER))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.STRING), FabricRecipeProvider.conditionsFromItem(Items.STRING))
                .offerTo(exporter);


    }
}

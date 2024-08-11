package mod.ginleaf.railings.datagen;

import mod.ginleaf.railings.MavilaRailings;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class RailingsRecipeProvider extends FabricRecipeProvider {
    public RailingsRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final int OUT = 16;

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.WHITE_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.WHITE_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.WHITE_DYE), FabricRecipeProvider.conditionsFromItem(Items.WHITE_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.ORANGE_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.ORANGE_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.ORANGE_DYE), FabricRecipeProvider.conditionsFromItem(Items.ORANGE_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.MAGENTA_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.MAGENTA_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.MAGENTA_DYE), FabricRecipeProvider.conditionsFromItem(Items.MAGENTA_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.LIGHT_BLUE_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.LIGHT_BLUE_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.LIGHT_BLUE_DYE), FabricRecipeProvider.conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.YELLOW_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.YELLOW_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.YELLOW_DYE), FabricRecipeProvider.conditionsFromItem(Items.YELLOW_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.LIME_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.LIME_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.LIME_DYE), FabricRecipeProvider.conditionsFromItem(Items.LIME_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.PINK_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.PINK_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.PINK_DYE), FabricRecipeProvider.conditionsFromItem(Items.PINK_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.GRAY_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.GRAY_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.GRAY_DYE), FabricRecipeProvider.conditionsFromItem(Items.GRAY_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.LIGHT_GRAY_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.LIGHT_GRAY_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.LIGHT_GRAY_DYE), FabricRecipeProvider.conditionsFromItem(Items.LIGHT_GRAY_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.CYAN_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.CYAN_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.CYAN_DYE), FabricRecipeProvider.conditionsFromItem(Items.CYAN_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.PURPLE_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.PURPLE_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.PURPLE_DYE), FabricRecipeProvider.conditionsFromItem(Items.PURPLE_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.BLUE_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.BLUE_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.BLUE_DYE), FabricRecipeProvider.conditionsFromItem(Items.BLUE_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.BROWN_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.BROWN_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.BROWN_DYE), FabricRecipeProvider.conditionsFromItem(Items.BROWN_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.GREEN_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.GREEN_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.GREEN_DYE), FabricRecipeProvider.conditionsFromItem(Items.GREEN_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.RED_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.RED_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.RED_DYE), FabricRecipeProvider.conditionsFromItem(Items.RED_DYE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MavilaRailings.BLACK_RAILING_BLOCK, OUT)
                .input('#', Items.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .input('D', Items.BLACK_DYE)
                .pattern("IDI")
                .pattern("###")
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_BARS), FabricRecipeProvider.conditionsFromItem(Items.IRON_BARS))
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.BLACK_DYE), FabricRecipeProvider.conditionsFromItem(Items.BLACK_DYE))
                .offerTo(exporter);
    }
}

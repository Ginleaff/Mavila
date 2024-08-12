package mod.ginleaf.railings.datagen;

import mod.ginleaf.railings.MavilaRailings;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class RailingsBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public RailingsBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
                MavilaRailings.WHITE_RAILING_BLOCK,
                MavilaRailings.ORANGE_RAILING_BLOCK,
                MavilaRailings.MAGENTA_RAILING_BLOCK,
                MavilaRailings.LIGHT_BLUE_RAILING_BLOCK,
                MavilaRailings.YELLOW_RAILING_BLOCK,
                MavilaRailings.LIME_RAILING_BLOCK,
                MavilaRailings.PINK_RAILING_BLOCK,
                MavilaRailings.GRAY_RAILING_BLOCK,
                MavilaRailings.LIGHT_GRAY_RAILING_BLOCK,
                MavilaRailings.CYAN_RAILING_BLOCK,
                MavilaRailings.PURPLE_RAILING_BLOCK,
                MavilaRailings.BLUE_RAILING_BLOCK,
                MavilaRailings.BROWN_RAILING_BLOCK,
                MavilaRailings.GREEN_RAILING_BLOCK,
                MavilaRailings.RED_RAILING_BLOCK,
                MavilaRailings.BLACK_RAILING_BLOCK
        );
    }
}

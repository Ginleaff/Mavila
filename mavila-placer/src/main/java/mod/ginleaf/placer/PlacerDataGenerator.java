package mod.ginleaf.placer;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import mod.ginleaf.placer.datagen.PlacerBlockTagProvider;
import mod.ginleaf.placer.datagen.PlacerLootTableProvider;
import mod.ginleaf.placer.datagen.PlacerRecipeProvider;

public class PlacerDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack placerPack = fabricDataGenerator.createPack();
		placerPack.addProvider(PlacerLootTableProvider::new);
		placerPack.addProvider(PlacerBlockTagProvider::new);
		placerPack.addProvider(PlacerRecipeProvider::new);
	}
}

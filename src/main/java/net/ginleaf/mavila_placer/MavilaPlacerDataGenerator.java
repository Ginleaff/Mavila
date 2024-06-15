package net.ginleaf.mavila_placer;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.ginleaf.mavila_placer.datagen.PlacerBlockTagProvider;
import net.ginleaf.mavila_placer.datagen.PlacerLootTableProvider;
import net.ginleaf.mavila_placer.datagen.PlacerRecipeProvider;

public class MavilaPlacerDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack placerPack = fabricDataGenerator.createPack();
		placerPack.addProvider(PlacerLootTableProvider::new);
		placerPack.addProvider(PlacerBlockTagProvider::new);
		placerPack.addProvider(PlacerRecipeProvider::new);
	}
}

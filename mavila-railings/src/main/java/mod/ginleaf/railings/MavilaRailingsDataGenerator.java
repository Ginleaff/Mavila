package mod.ginleaf.railings;

import mod.ginleaf.railings.datagen.RailingsBlockTagProvider;
import mod.ginleaf.railings.datagen.RailingsLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MavilaRailingsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack railingsPack = fabricDataGenerator.createPack();
		railingsPack.addProvider(RailingsLootTableProvider::new);
		railingsPack.addProvider(RailingsBlockTagProvider::new);
	}
}

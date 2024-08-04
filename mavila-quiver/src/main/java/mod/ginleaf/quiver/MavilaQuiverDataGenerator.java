package mod.ginleaf.quiver;

import mod.ginleaf.quiver.datagen.QuiverItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MavilaQuiverDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack quiverPack = fabricDataGenerator.createPack();
		quiverPack.addProvider(QuiverItemTagProvider::new);

	}
}

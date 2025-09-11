package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = Rsc.ID, bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGen {
	private static GatherDataEvent event;
	private static DataGenerator generator;
	@SubscribeEvent
	public static void gather_data(GatherDataEvent event) {
		ModDataGen.event = event;
		generator = event.getGenerator();
		var output = generator.getPackOutput();
		var existing_file_helper = event.getExistingFileHelper();
		var provider = event.getLookupProvider();
		server(new ModItemTagProvider(output, provider,
			server(new ModBlockTagProvider(output, provider, existing_file_helper)).contentsGetter(),
			existing_file_helper));
		server(new ModGeneralDataProvider(output, provider));
		server(new ModRecipeProvider(output));
		server(new ModLootTableProvider(output));
		client(new ModItemModelProvider(output, existing_file_helper));
		client(new ModBlockStateProvider(output, existing_file_helper));
		client(new ModLanguageProvider(output, "en_us"));
	}
	private static <T extends DataProvider> T server(T provider) {
		generator.addProvider(event.includeServer(), provider);
		return provider;
	}
	@SuppressWarnings("UnusedReturnValue")
	private static <T extends DataProvider> T client(T provider) {
		generator.addProvider(event.includeClient(), provider);
		return provider;
	}
}

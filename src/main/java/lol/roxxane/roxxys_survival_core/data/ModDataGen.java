package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGen {
	private static GatherDataEvent event;
	private static DataGenerator generator;
	@SubscribeEvent
	public static void gather_data(GatherDataEvent event) {
		ModDataGen.event = event;
		generator = event.getGenerator();
		var output = generator.getPackOutput();
		var existingFileHelper = event.getExistingFileHelper();
		var provider = event.getLookupProvider();
		server(new ModItemTagProvider(output, provider,
			server(new ModBlockTagProvider(output, provider, existingFileHelper)).contentsGetter(),
			existingFileHelper));
		server(new ModGeneralDataProvider(output, provider));
		server(new ModRecipeProvider(output));
		server(new ModLootTableProvider(output));
		server(new ModMobEffectTagsProvider(output, provider, existingFileHelper));
		server(new ModEntityTypeTagsProvider(output, provider, existingFileHelper));
		client(new ModBlockStateProvider(output, existingFileHelper));
		client(new ModItemModelProvider(output, existingFileHelper));
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

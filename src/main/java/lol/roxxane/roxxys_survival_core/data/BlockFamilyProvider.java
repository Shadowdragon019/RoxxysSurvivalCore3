package lol.roxxane.roxxys_survival_core.data;

import com.google.gson.JsonArray;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public class BlockFamilyProvider implements DataProvider {
	private final PathProvider pathProvider;
	public BlockFamilyProvider(PackOutput output) {
		pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, getName());
	}
	public CompletableFuture<?> run(CachedOutput output) {
		var futures = new ArrayList<CompletableFuture<?>>();
		var data = new HashMap<String, List<Item>>();
		data.put("test", List.of(Items.DIRT, Items.DIRT_PATH));
		data.forEach((id, items) -> {
			var json = new JsonArray();
			items.forEach(item -> json.add(requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString()));
			futures.add(DataProvider.saveStable(output, json, pathProvider.json(Id.mod(id))));
		});
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}
	public String getName() {
		return "block_family";
	}
}

package lol.roxxane.roxxys_survival_core.data;

import com.google.gson.JsonElement;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import snownee.kiwi.customization.block.family.BlockFamily;
import snownee.kiwi.customization.block.family.BlockFamily.SwitchAttrs;
import snownee.kiwi.util.KHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlockFamilyManager extends SimpleJsonResourceReloadListener {
	public static final List<KHolder<BlockFamily>> FAMILIES = new ArrayList<>();
	public BlockFamilyManager() {
		super(Rsc.PRETTY_GSON, "block_families");
	}
	protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
		FAMILIES.clear();
		jsons.forEach((recipeId, json) -> {
			var items = json.getAsJsonArray().asList().stream().map(element -> ResourceKey.create(Registries.ITEM, Id.of(element.getAsString()))).toList();
			var blockFamily = new BlockFamily(false, List.of(), items, List.of(), false, Optional.empty(), 0, SwitchAttrs.create(true, true, false));
			FAMILIES.add(new KHolder<>(recipeId, blockFamily));
		});
	}
}

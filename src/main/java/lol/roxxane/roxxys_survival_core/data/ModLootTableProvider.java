package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends LootTableProvider {
	public ModLootTableProvider(PackOutput output) {
		super(output, Set.of(), List.of(
			new SubProviderEntry(MoBlockLootSubProvider::new, LootContextParamSets.BLOCK)
		));
	}
	@SuppressWarnings("unused")
	public static class MoBlockLootSubProvider extends BlockLootSubProvider {
		public MoBlockLootSubProvider() {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		}
		protected void generate() {
			dropOther(ModBlocks.FLINT.get(), ModItems.FLINT.get());
		}
		public Iterable<Block> getKnownBlocks() {
			return ModBlocks.REGISTRY.getEntries()
				.stream()
				.flatMap(RegistryObject::stream)
				::iterator;
		}
	}
}
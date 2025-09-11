package lol.roxxane.roxxys_survival_core.items;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Rsc.ID);
	public static final RegistryObject<BlockItem> FLINT =
		item("flint", p -> new BlockItem(ModBlocks.FLINT.get(), p));
	private static <T extends Item> RegistryObject<T> item(String path, Function<Properties, T> function) {
		return REGISTRY.register(path, () -> function.apply(new Properties()));
	}
	private static RegistryObject<Item> item(String path) {
		return REGISTRY.register(path, () -> new Item(new Properties()));
	}
	public static void register() {}
}

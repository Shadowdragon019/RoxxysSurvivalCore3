package lol.roxxane.roxxys_survival_core.blocks;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Rsc.ID);
	public static final RegistryObject<FlintBlock> FLINT =
		block("flint", p -> new FlintBlock(p.instabreak().mapColor(MapColor.STONE)));
	private static <T extends Block> RegistryObject<T> block(String path, Function<Properties, T> function) {
		return REGISTRY.register(path, () -> function.apply(Properties.of()));
	}
	private static RegistryObject<Block> block(String path) {
		return REGISTRY.register(path, () -> new Block(Properties.of()));
	}
	public static void register() {}
}

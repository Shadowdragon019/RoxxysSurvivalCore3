package lol.roxxane.roxxys_survival_core.tags;

import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
	public static TagKey<Block> mc(String path) {
		return BlockTags.create(Id.mc(path));
	}
	public static TagKey<Block> forge(String path) {
		return BlockTags.create(Id.forge(path));
	}
	public static TagKey<Block> mod(String path) {
		return BlockTags.create(Id.mod(path));
	}
}

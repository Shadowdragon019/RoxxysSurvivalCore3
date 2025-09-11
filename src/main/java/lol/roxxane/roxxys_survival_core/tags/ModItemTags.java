package lol.roxxane.roxxys_survival_core.tags;

import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
	public static final TagKey<Item> UNBREAKABLE = mod("unbreakable");
	public static TagKey<Item> mc(String path) {
		return ItemTags.create(Id.mc(path));
	}
	public static TagKey<Item> forge(String path) {
		return ItemTags.create(Id.forge(path));
	}
	public static TagKey<Item> mod(String path) {
		return ItemTags.create(Id.mod(path));
	}
}

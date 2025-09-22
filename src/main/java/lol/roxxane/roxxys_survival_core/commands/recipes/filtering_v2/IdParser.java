package lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public enum IdParser {
	NAMESPACE {
		public List<String> parse(ResourceLocation id) {
			return List.of(id.getNamespace());
		}
	},
	PATH {
		public List<String> parse(ResourceLocation id) {
			return List.of(id.getPath());
		}
	},
	ID {
		public List<String> parse(ResourceLocation id) {
			return List.of(id.toString());
		}
	},
	EITHER {
		public List<String> parse(ResourceLocation id) {
			return List.of(id.getNamespace(), id.getPath());
		}
	};
	public abstract List<String> parse(ResourceLocation id);
}
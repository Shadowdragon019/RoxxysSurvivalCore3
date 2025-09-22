package lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2;

import java.util.List;

public enum IdValidator {
	CONTAINS {
		public boolean testIndividual(String parsedRecipeId, String against) {
			return parsedRecipeId.contains(against);
		}
	},
	EQUALS {
		public boolean testIndividual(String parsedRecipeId, String against) {
			return parsedRecipeId.equals(against);
		}
	},
	ANY {
		public boolean testIndividual(String parsedRecipeId, String against) {
			return true;
		}
	};
	public boolean test(List<String> parsedRecipeIds, String against) {
		return parsedRecipeIds.stream().anyMatch(parsedRecipeId -> testIndividual(parsedRecipeId, against));
	}
	public abstract boolean testIndividual(String parsedRecipeId, String against);
}
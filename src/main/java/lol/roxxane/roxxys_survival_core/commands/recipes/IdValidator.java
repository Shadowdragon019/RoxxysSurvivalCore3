package lol.roxxane.roxxys_survival_core.commands.recipes;

@SuppressWarnings("unused")
public enum IdValidator {
	CONTAINS {
		public boolean test(String id, String locating_id) {
			return id.contains(locating_id);
		}
	}, EQUALS {
		public boolean test(String id, String locating_id) {
			return id.equals(locating_id);
		}
	};
	public abstract boolean test(String id, String locating_id);
}

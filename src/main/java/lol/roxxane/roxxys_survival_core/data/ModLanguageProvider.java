package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Map;

import static java.util.Map.entry;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(PackOutput output, String locale) {
		super(output, Rsc.ID, locale);
	}
	protected void addTranslations() {
		add(ModItems.FLINT.get(), "Flint");
		add(ModItems.OAK_RESPAWN_TOTEM.get(), "Oak Respawn Totem");
		add("tooltip.roxxys_survival_core.item_tags_header", "§7Item Tags:");
		add("tooltip.roxxys_survival_core.block_tags_header", "§7Block Tags:");
		add("tooltip.roxxys_survival_core.tag_header", "§7Tag:");
		add("gui.roxxys_survival_core.name_tag", "Name Tag");
		add("gui.roxxys_survival_core.name_tag.edit_box", "Edit box");
		add("gui.roxxys_survival_core.name_tag.name_tag", "Name tag");
		add("tooltip.roxxys_survival_core.flint", "§7Knap knap!");
		add(ModMobEffects.WITCH_SLOWNESS.get(), "Witch Slowness");
		add(ModMobEffects.WITCH_WEAKNESS.get(), "Witch Weakness");
		add(ModMobEffects.WITCH_MINING_FATIGUE.get(), "Witch Mining Fatigue");
		add(ModMobEffects.WITCH_SPEED.get(), "Witch Speed");
		add(ModMobEffects.WITCH_STRENGTH.get(), "Witch Strength");
		add(ModMobEffects.WITCH_REGENERATION.get(), "Witch Regeneration");
		add(ModMobEffects.WITCH_JUMP_BOOST.get(), "Witch Jump Boost");
		add(ModMobEffects.WITCH_RESISTANCE.get(), "Witch Resistance");
		add(ModMobEffects.WITCH_INVISIBILITY.get(), "Witch Invisibility");
		add(ModMobEffects.WITCH_FIRE_RESISTANCE.get(), "Witch Fire Resistance");
		add(ModMobEffects.WITCH_WATER_BREATHING.get(), "Witch Water Breathing");
		add(ModMobEffects.WITCH_INSTANT_DAMAGE.get(), "Witch Instant Damage");
		add(ModMobEffects.WITCH_INSTANT_HEALTH.get(), "Witch Instant Health");
		for (var entry : Map.ofEntries(
			entry("witch_slowness", "Witch Slowness"),
			entry("witch_weakness", "Witch Weakness"),
			entry("witch_mining_fatigue", "Witch Mining Fatigue"),
			entry("witch_blindness", "Witch Blindness"),
			entry("witch_speed", "Witch Speed"),
			entry("witch_strength", "Witch Strength"),
			entry("witch_regeneration", "Witch Regeneration"),
			entry("witch_jump_boost", "Witch Jump Boost"),
			entry("witch_resistance", "Witch Resistance"),
			entry("witch_invisibility", "Witch Invisibility"),
			entry("witch_fire_resistance", "Witch Fire Resistance"),
			entry("witch_water_breathing", "Witch Water Breathing"),
			entry("witch_instant_damage", "Witch Instant Damage"),
			entry("witch_instant_health", "Witch Instant Health")
		).entrySet()) {
			var potionPath = entry.getKey();
			var potionName = entry.getValue();
			add("item.minecraft.tipped_arrow.effect.%s".formatted(potionPath), "Arrow of %s ".formatted(potionName));
			add("item.minecraft.potion.effect.%s".formatted(potionPath), "Potion of %s ".formatted(potionName));
			add("item.minecraft.splash_potion.effect.%s".formatted(potionPath), "Splash Potion of %s ".formatted(potionName));
			add("item.minecraft.lingering_potion.effect.%s".formatted(potionPath), "Lingering Potion of %s ".formatted(potionName));
		}
	}
}

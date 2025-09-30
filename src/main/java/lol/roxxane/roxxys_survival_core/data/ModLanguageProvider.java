package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
	public ModLanguageProvider(PackOutput output, String locale) {
		super(output, Rsc.ID, locale);
	}
	protected void addTranslations() {
		add(ModItems.OAK_RESPAWN_TOTEM.get(), "Oak Respawn Totem");
		add("tooltip.roxxys_survival_core.item_tags_header", "§7Item Tags:");
		add("tooltip.roxxys_survival_core.block_tags_header", "§7Block Tags:");
		add("tooltip.roxxys_survival_core.tag_header", "§7Tag:");
		add("gui.roxxys_survival_core.name_tag", "Name Tag");
		add("gui.roxxys_survival_core.name_tag.edit_box", "Edit box");
		add("gui.roxxys_survival_core.name_tag.name_tag", "Name tag");
		add(ModMobEffects.BLEEDING.get(), "Bleeding");
	}
}

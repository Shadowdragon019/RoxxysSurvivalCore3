package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(PackOutput output, ExistingFileHelper existing_file_helper) {
		super(output, Rsc.ID, existing_file_helper);
	}
	protected void registerModels() {
		withExistingParent("item/flint", Id.mc("generated"))
			.texture("layer0", Id.mc("item/flint"));
	}
}

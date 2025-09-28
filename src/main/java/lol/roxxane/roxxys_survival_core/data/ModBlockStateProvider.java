package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
	public ModBlockStateProvider(PackOutput output, ExistingFileHelper existing_file_helper) {
		super(output, Rsc.ID, existing_file_helper);
	}
	protected void registerStatesAndModels() {
		models().cubeColumn("block/oak_respawn_totem", Id.mod("block/respawn_totem/oak_side"), Id.mod("block/respawn_totem/oak_end"));
		simpleBlock(ModBlocks.OAK_RESPAWN_TOTEM.get(), models().getExistingFile(modLoc("block/oak_respawn_totem")));
	}
}

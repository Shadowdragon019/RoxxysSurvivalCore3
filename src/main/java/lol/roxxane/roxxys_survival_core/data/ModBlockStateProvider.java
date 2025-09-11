package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
	public ModBlockStateProvider(PackOutput output, ExistingFileHelper existing_file_helper) {
		super(output, Rsc.ID, existing_file_helper);
	}
	protected void registerStatesAndModels() {
		getVariantBuilder(ModBlocks.FLINT.get()).forAllStatesExcept(
			state -> ConfiguredModel.builder()
				.modelFile(models().getExistingFile(modLoc("block/flint")))
				.rotationY(((int) state.getValue(
					BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360
				)
				.build(),
			BlockStateProperties.WATERLOGGED
		);
	}
}

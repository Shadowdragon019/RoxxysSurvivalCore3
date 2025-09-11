package lol.roxxane.roxxys_survival_core.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@SuppressWarnings("deprecation")
public class FlintBlock extends Block implements SimpleWaterloggedBlock {
	public static final VoxelShape EAST_WEST = Block.box(2, 0, 4, 14, 2, 12);
	public static final VoxelShape NORTH_SOUTH = Block.box(4, 0, 2, 12, 2, 14);
	public FlintBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState()
			.setValue(WATERLOGGED, false)
			.setValue(HORIZONTAL_FACING, Direction.EAST)
		);
	}
	public FluidState getFluidState(BlockState state) {
		if (state.getValue(WATERLOGGED))
			return Fluids.WATER.getSource(false);
		else return Fluids.EMPTY.defaultFluidState();
	}
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var level = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var state = defaultBlockState();
		var direction = ctx.getClickedFace();
		var fluidState = level.getFluidState(pos);
		if (!ctx.replacingClickedOnBlock() && direction.getAxis().isHorizontal())
			state = state.setValue(HORIZONTAL_FACING, direction);
		else
			state = state.setValue(HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
		state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		return state;
	}
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, HORIZONTAL_FACING);
	}
	public boolean canSurvive(BlockState $, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.offset(0, -1, 0))
			.isFaceSturdy(level, pos, Direction.UP);
	}
	public VoxelShape getShape(BlockState state, BlockGetter $, BlockPos $1, CollisionContext $2) {
		return switch (state.getValue(HORIZONTAL_FACING)) {
			case EAST, WEST -> EAST_WEST;
			case SOUTH, NORTH -> NORTH_SOUTH;
			default -> throw new IllegalStateException("Not a possible direction!");
		};
	}
}

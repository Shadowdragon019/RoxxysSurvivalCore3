package lol.roxxane.roxxys_survival_core.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import static lol.roxxane.roxxys_survival_core.blocks.ModStateProperties.RESPAWN_TOTEM_PART;
import static lol.roxxane.roxxys_survival_core.blocks.state_property_enums.RespawnTotemPart.*;

@SuppressWarnings("deprecation")
public class RespawnTotemBlock extends Block {
	public RespawnTotemBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState()
			.setValue(RESPAWN_TOTEM_PART, BOTTOM));
	}
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.setRespawnPosition(level.dimension(), pos, 0, false, true);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.CONSUME;
	}
	/**
	 * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific direction passed in.
	 */
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		var part = state.getValue(RESPAWN_TOTEM_PART);
		var aboveIsTotem = level.getBlockState(pos.above()).is(this);
		var belowIsTotem = level.getBlockState(pos.below()).is(this);
		if (part == TOP && belowIsTotem)
			return state;
		if (part == MIDDLE && belowIsTotem && aboveIsTotem)
			return state;
		if (part == BOTTOM && aboveIsTotem)
			return state;
		return Blocks.AIR.defaultBlockState();
	}
	/// Called by BlockItem after this block has been placed.
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		var middlePos = pos.above();
		var topPos = middlePos.above();
		level.setBlockAndUpdate(middlePos, defaultBlockState().setValue(RESPAWN_TOTEM_PART, MIDDLE));
		level.setBlockAndUpdate(topPos, defaultBlockState().setValue(RESPAWN_TOTEM_PART, TOP));
	}
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		var middleCanBePlaced = level.getBlockState(pos.above()).canBeReplaced();
		var topCanBePlaced = level.getBlockState(pos.above(2)).canBeReplaced();
		var fitsInWorld = pos.getY() < level.getMaxBuildHeight() - 2;
		return middleCanBePlaced && topCanBePlaced && fitsInWorld;
	}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(RESPAWN_TOTEM_PART);
	}
}
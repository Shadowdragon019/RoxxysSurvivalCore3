package lol.roxxane.roxxys_survival_core.mixins.name_tag_can_rename_items;

import lol.roxxane.roxxys_survival_core.RscClient;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

// I know I can just use events, but it isn't working & I don't feel like finding out why
@SuppressWarnings("unused")
@Mixin(NameTagItem.class)
abstract class NameTagItemMixin extends Item {
	public NameTagItemMixin(Properties $) { super($); }
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		if (level.isClientSide) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> RscClient::load_tag_screen);
			return InteractionResultHolder.success(player.getItemInHand(hand));
		} else
			return InteractionResultHolder.consume(player.getItemInHand(hand));
	}
}
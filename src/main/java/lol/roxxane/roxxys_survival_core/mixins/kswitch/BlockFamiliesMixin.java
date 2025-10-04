package lol.roxxane.roxxys_survival_core.mixins.kswitch;

import lol.roxxane.roxxys_survival_core.data.BlockFamilyManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.customization.block.family.BlockFamilies;
import snownee.kiwi.customization.block.family.BlockFamily;
import snownee.kiwi.customization.block.family.BlockFamilyInferrer;
import snownee.kiwi.util.KHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Mixin(value = BlockFamilies.class, remap = false)
abstract class BlockFamiliesMixin {
	@Redirect(method = "reloadResources",
		at = @At(value = "INVOKE",
			target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
	private static Set<Entry<ResourceLocation, BlockFamily>> blockYamlBlockFamilies(Map<ResourceLocation, BlockFamily> instance) {
		return new HashSet<>();
	}
	@Redirect(method = "reloadTags",
		at = @At(value = "INVOKE",
			target = "Lsnownee/kiwi/customization/block/family/BlockFamilyInferrer;generate()Ljava/util/Collection;"))
	private static Collection<KHolder<BlockFamily>> replaceAutomaticBlockFamiliesWithCustomBlockFamilies(BlockFamilyInferrer instance) {
		return BlockFamilyManager.FAMILIES;
	}
	@Inject(method = "getConvertRatio", cancellable = true, at = @At("HEAD"))
	private static void consistentConvertRatio(Item item, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(1f);
	}
}

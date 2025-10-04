package lol.roxxane.roxxys_survival_core.mixins.kswitch;

import org.spongepowered.asm.mixin.Mixin;
import snownee.kiwi.customization.block.family.BlockFamilies;

@Mixin(value = BlockFamilies.class, remap = false)
abstract class BlockYamlBlockFamilies {
	/*@Redirect(method = "reloadResources",
		at = @At(value = "INVOKE",
			target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
	private static Set<Map.Entry<ResourceLocation, BlockFamily>> block(Map<ResourceLocation, BlockFamily> instance) {
		return new HashSet<>();
	}*/
}

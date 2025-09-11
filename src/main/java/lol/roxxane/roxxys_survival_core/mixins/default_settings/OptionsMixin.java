package lol.roxxane.roxxys_survival_core.mixins.default_settings;

import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Options.class)
abstract class OptionsMixin {
	// Cus I think it's c o o l
	// Also it just looks better imo
	@ModifyConstant(method = "<init>", constant = @Constant(doubleValue = 0.5), slice = @Slice(
		from = @At(value = "FIELD",
			target = "Lnet/minecraft/client/Options;damageTiltStrength:Lnet/minecraft/client/OptionInstance;"),
		to = @At(value = "FIELD",
			target = "Lnet/minecraft/client/Options;gamma:Lnet/minecraft/client/OptionInstance;")
	))
	private double default_brightness(double constant) {
		return 0;
	}
	// Entities can attack so fast it results in absurd damage tilt so it's getting toned down a LOT.
	// Try surrounding yourself with 0 iframes slimes & make yourself immortal. Time to puke!
	// All hail accessibility settings!
	@ModifyConstant(method = "<init>", constant = @Constant(doubleValue = 1), slice = @Slice(
		from = @At(value = "FIELD",
			target = "Lnet/minecraft/client/Options;glintStrength:Lnet/minecraft/client/OptionInstance;"),
		to = @At(value = "FIELD",
			target = "Lnet/minecraft/client/Options;damageTiltStrength:Lnet/minecraft/client/OptionInstance;")
	))
	private double default_damage_tilt(double constant) {
		return 0.25;
	}
}

package lol.roxxane.roxxys_survival_core.mixins.witch_changes;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.entities.goals.WitchTargetFoeGoal;
import lol.roxxane.roxxys_survival_core.entities.goals.WitchTargetFriendGoal;
import lol.roxxane.roxxys_survival_core.tags.ModEntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.potions.ModPotions.*;

@Mixin(Witch.class)
abstract class WitchMixin extends Raider implements RangedAttackMob {
	protected WitchMixin(EntityType<? extends Raider> $, Level $1) { super($, $1); }
	@Unique
	private static final List<Supplier<Potion>> rsc$POTIONS_FOR_FOES =
		List.of(WITCH_BLINDNESS, WITCH_SLOWNESS, WITCH_WEAKNESS, WITCH_MINING_FATIGUE);
	@Unique
	private static final List<Supplier<Potion>> rsc$POTIONS_FOR_FRIENDS =
		List.of(WITCH_SPEED, WITCH_STRENGTH, WITCH_REGENERATION, WITCH_JUMP_BOOST, WITCH_RESISTANCE);
	@Unique
	private WitchTargetFriendGoal rsc$targetFriendGoal;
	@Unique
	private WitchTargetFoeGoal rsc$targetFoeGoal;
	@Inject(method = "performRangedAttack", cancellable = true,
		at = @At(value = "NEW",
			target = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/projectile/ThrownPotion;"))
	private void changePotion(LivingEntity target, float distanceFactor, CallbackInfo ci, @Local LocalRef<Potion> potion) {
		potion.set(Potions.EMPTY);
		if (rsc$entityInTag(target, ModEntityTypeTags.WITCH_FOES))
			potion.set(rsc$choosePotion(rsc$POTIONS_FOR_FOES, target));
		else if (rsc$entityInTag(target, ModEntityTypeTags.WITCH_FRIENDS)) {
			var potions = new ArrayList<>(rsc$POTIONS_FOR_FRIENDS);
			if (target.isOnFire())
				potions.add(WITCH_FIRE_RESISTANCE);
			if (target.isUnderWater() && !target.canDrownInFluidType(ForgeMod.WATER_TYPE.get()))
				potions.add(WITCH_WATER_BREATHING);
			potion.set(rsc$choosePotion(potions, target));
		}
		if (potion == Potions.EMPTY) {
			Rsc.warn("Witch could not find potion to throw!!");
			ci.cancel();
		}
	}
	@Unique
	private Potion rsc$choosePotion(List<Supplier<Potion>> potions, LivingEntity target) {
		var potionsTargetDoesNotHave = potions.stream()
			.map(Supplier::get)
			.filter(harmfulPotion -> {
				var effectInstance = harmfulPotion.getEffects().get(0);
				var effect = effectInstance.getEffect();
				if (target.hasEffect(effect))
					if (effectInstance.getAmplifier() > requireNonNull(target.getEffect(effect)).getAmplifier())
						return true;
				return !target.hasEffect(effect);
			}).toList();
		if (!potionsTargetDoesNotHave.isEmpty())
			return potionsTargetDoesNotHave.get(random.nextInt(potionsTargetDoesNotHave.size()));
		return rsc$getPotionWithLowestDuration(potions, target);
	}
	@Unique
	private Potion rsc$getPotionWithLowestDuration(List<Supplier<Potion>> potions, LivingEntity target) {
		return potions.stream()
			.map(Supplier::get)
			.min((potionA, potionB) -> {
				var effectA = requireNonNull(target.getEffect(potionA.getEffects().get(0).getEffect()));
				var effectB = requireNonNull(target.getEffect(potionB.getEffects().get(0).getEffect()));
				return Integer.compare(effectA.getDuration(), effectB.getDuration());
			}).orElseThrow();
	}
	@Unique
	private static boolean rsc$entityInTag(Entity entity, TagKey<EntityType<?>> tagKey) {
		return requireNonNull(ForgeRegistries.ENTITY_TYPES.tags()).getTag(tagKey).contains(entity.getType());
	}
	@ModifyArg(method = "registerGoals",
		index = 1,
		slice = @Slice(from = @At(value = "FIELD",
			target = "Lnet/minecraft/world/entity/monster/Witch;targetSelector:Lnet/minecraft/world/entity/ai/goal/GoalSelector;")),
		at = @At(value = "INVOKE", ordinal = 1,
			target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"))
	private Goal targetFriendGoal(Goal $) {
		return rsc$targetFriendGoal = new WitchTargetFriendGoal(this);
	}
	@ModifyArg(method = "registerGoals",
		index = 1,
		slice = @Slice(from = @At(value = "FIELD",
			target = "Lnet/minecraft/world/entity/monster/Witch;targetSelector:Lnet/minecraft/world/entity/ai/goal/GoalSelector;")),
		at = @At(value = "INVOKE", ordinal = 2,
			target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"))
	private Goal targetFoeGoal(Goal $) {
		return rsc$targetFoeGoal = new WitchTargetFoeGoal(this);
	}
	@Redirect(method = "aiStep",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/goal/target/NearestHealableRaiderTargetGoal;decrementCooldown()V"))
	private void decrementCooldown(NearestHealableRaiderTargetGoal<?> instance) {
		rsc$targetFriendGoal.decrementCooldown();
		rsc$targetFoeGoal.setCanAttack(rsc$targetFriendGoal.getCooldown() <= 0);
	}
	@Redirect(method = "aiStep",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/target/NearestAttackableWitchTargetGoal;setCanAttack(Z)V"))
	private void setCanAttack(NearestAttackableWitchTargetGoal<?> instance, boolean $) {}
}
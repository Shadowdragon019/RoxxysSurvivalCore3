package lol.roxxane.roxxys_survival_core.mixins.sheep_naturally_drop_wool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Sheep.class)
abstract class SheepMixin extends Animal {
	protected SheepMixin(EntityType<? extends Animal> $, Level $1) { super($, $1); }
	@SuppressWarnings("WrongEntityDataParameterClass")
	@Unique private static final EntityDataAccessor<Integer> WOOL_DROP_COOLDOWN_ID =
		SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.INT);
	@Shadow @Final private static Map<DyeColor, ItemLike> ITEM_BY_DYE;
	@Shadow public abstract DyeColor getColor();
	@Shadow public abstract boolean readyForShearing();
	@Shadow public abstract void setSheared(boolean pSheared);
	@Inject(method = "aiStep", at = @At("HEAD"))
	private void ai_step_inject(CallbackInfo ci) {
		if (isAlive() && readyForShearing() && rsc$lower_and_get_wool_drop_cooldown() < 1) {
			var stack = new ItemStack(ITEM_BY_DYE.get(getColor()));
			var item_entity = spawnAtLocation(stack, 1);
			if (item_entity != null)
				item_entity.setDeltaMovement(item_entity.getDeltaMovement().add(
					(random.nextFloat() - random.nextFloat()) * 0.1F,
					random.nextFloat() * 0.05F,
					(random.nextFloat() - random.nextFloat()) * 0.1F));
			rsc$set_wool_drop_cooldown(24000);
			setSheared(true);
		}
	}
	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void define_additional_synced_data(CallbackInfo ci) {
		entityData.define(WOOL_DROP_COOLDOWN_ID, 24000);
	}
	@Unique
	public int rsc$get_wool_drop_cooldown() {
		return entityData.get(WOOL_DROP_COOLDOWN_ID);
	}
	@Unique
	public void rsc$set_wool_drop_cooldown(int value) {
		entityData.set(WOOL_DROP_COOLDOWN_ID, value);
	}
	@Unique public int rsc$lower_and_get_wool_drop_cooldown() {
		var cooldown = rsc$get_wool_drop_cooldown() - 1;
		rsc$set_wool_drop_cooldown(cooldown);
		return cooldown;
	}
	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void add_data(CompoundTag compound, CallbackInfo ci) {
		compound.putInt("rsc:wool_drop_cooldown", rsc$get_wool_drop_cooldown());
	}
	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void read_data(CompoundTag compound, CallbackInfo ci) {
		rsc$set_wool_drop_cooldown(compound.getInt("rsc:wool_drop_cooldown"));
	}
}

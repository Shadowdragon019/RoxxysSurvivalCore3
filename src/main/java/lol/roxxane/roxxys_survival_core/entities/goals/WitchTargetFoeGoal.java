package lol.roxxane.roxxys_survival_core.entities.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.tags.ModEntityTypeTags.WITCH_FOES;
import static net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES;

public class WitchTargetFoeGoal extends NearestAttackableTargetGoal<LivingEntity> {
	private boolean canAttack = true;
	public WitchTargetFoeGoal(Mob mob) {
		super(mob, LivingEntity.class, 5, true, false, target -> {
			if (target == null) return false;
			return requireNonNull(ENTITY_TYPES.tags()).getTag(WITCH_FOES).contains(target.getType());
		});
	}
	public void setCanAttack(boolean value) {
		canAttack = value;
	}
	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		return canAttack && super.canUse();
	}
}

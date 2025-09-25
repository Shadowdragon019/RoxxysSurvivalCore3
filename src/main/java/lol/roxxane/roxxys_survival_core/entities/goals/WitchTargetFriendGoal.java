package lol.roxxane.roxxys_survival_core.entities.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.tags.ModEntityTypeTags.WITCH_FRIENDS;
import static net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES;

public class WitchTargetFriendGoal extends NearestAttackableTargetGoal<LivingEntity> {
	private static final int DEFAULT_COOLDOWN = 200;
	private int cooldown = 0;
	public WitchTargetFriendGoal(Mob mob) {
		super(mob, LivingEntity.class, 10, true, false, target -> {
			if (target == null) return false;
			return requireNonNull(ENTITY_TYPES.tags()).getTag(WITCH_FRIENDS).contains(target.getType());
		});
	}
	public int getCooldown() {
		return cooldown;
	}
	public void decrementCooldown() {
		--cooldown;
	}
	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		if (cooldown <= 0) {
			findTarget();
			return target != null;
		}
		return false;
	}
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		cooldown = reducedTickDelay(200);
		super.start();
	}
}

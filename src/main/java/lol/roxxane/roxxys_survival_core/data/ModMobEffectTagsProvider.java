package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.*;
import static lol.roxxane.roxxys_survival_core.tags.ModMobEffectTags.AFFECTS_WITCH_FOES;
import static lol.roxxane.roxxys_survival_core.tags.ModMobEffectTags.AFFECTS_WITCH_FRIENDS;

public class ModMobEffectTagsProvider extends TagsProvider<MobEffect> {
	public ModMobEffectTagsProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, Registries.MOB_EFFECT, provider, Rsc.ID, existingFileHelper);
	}
	protected void addTags(Provider provider) {
		tags(AFFECTS_WITCH_FOES, WITCH_SLOWNESS, WITCH_WEAKNESS, WITCH_MINING_FATIGUE);
		tags(AFFECTS_WITCH_FRIENDS, WITCH_SPEED, WITCH_STRENGTH, WITCH_REGENERATION, WITCH_JUMP_BOOST, WITCH_RESISTANCE, WITCH_INVISIBILITY, WITCH_FIRE_RESISTANCE, WITCH_WATER_BREATHING);
	}
	@SafeVarargs protected final TagAppender<MobEffect> tags(TagKey<MobEffect> tagKey, Supplier<MobEffect>... effects) {
		var tag = tag(tagKey);
		for (var effect : effects)
			tag.add(ForgeRegistries.MOB_EFFECTS.getResourceKey(effect.get()).orElseThrow());
		return tag;
	}
}

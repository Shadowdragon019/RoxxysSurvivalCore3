package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static lol.roxxane.roxxys_survival_core.tags.ModEntityTypeTags.WITCH_FOES;
import static lol.roxxane.roxxys_survival_core.tags.ModEntityTypeTags.WITCH_FRIENDS;
import static net.minecraft.world.entity.EntityType.*;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
	public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, Rsc.ID, existingFileHelper);
	}
	protected void addTags(Provider provider) {
		tag(WITCH_FOES).add(PLAYER);
		tag(WITCH_FRIENDS).add(CAVE_SPIDER, ELDER_GUARDIAN, WITCH, ZOMBIE, ZOMBIE_VILLAGER);
	}
}

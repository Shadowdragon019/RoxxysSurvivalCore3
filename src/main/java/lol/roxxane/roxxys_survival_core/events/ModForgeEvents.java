package lol.roxxane.roxxys_survival_core.events;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.commands.RscCommand;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.*;

@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.FORGE)
public class ModForgeEvents {
	@SubscribeEvent
	public static void living_hurt(LivingDamageEvent event) {
		var entity = event.getEntity();
		@SuppressWarnings("resource") var level = entity.level();
		if (override_iframe_functionality) {
			var type = event.getSource().type();
			var registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
			entity.invulnerableTime = DAMAGE_TYPE_IFRAMES.getOrDefault(registry.getKey(type), default_iframes);
		}
	}
	@SubscribeEvent
	public static void register_commands(RegisterCommandsEvent event) {
		var dispatcher = event.getDispatcher();
		//RscReloadCommand.register(dispatcher);
		RscCommand.register(dispatcher);
	}
}

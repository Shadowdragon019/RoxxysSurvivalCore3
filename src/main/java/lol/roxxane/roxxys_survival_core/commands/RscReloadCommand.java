package lol.roxxane.roxxys_survival_core.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RscReloadCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		var command = Commands.literal("rsc_force_load_all_client_config")
			.requires(source -> source.hasPermission(2))
			.executes(context -> {
				try {
					// This loads EVERYTHING which me no like
					//ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.CLIENT, FMLPaths.CONFIGDIR.get());
					// Also this crashes
					/*ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.SERVER,
						getServerConfigPath(requireNonNull(requireNonNull(Minecraft.getInstance().level).getServer())));*/
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			});
		dispatcher.register(command);
	}
	private static final LevelResource SERVERCONFIG = new LevelResource("serverconfig");
	private static Path getServerConfigPath(MinecraftServer server) {
		final Path server_config = server.getWorldPath(SERVERCONFIG);
		if (!Files.isDirectory(server_config))
			try {
				Files.createDirectories(server_config);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		return server_config;
	}
}

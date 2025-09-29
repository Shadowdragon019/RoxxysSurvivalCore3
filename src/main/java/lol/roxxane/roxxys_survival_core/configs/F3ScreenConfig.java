package lol.roxxane.roxxys_survival_core.configs;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lol.roxxane.roxxys_survival_core.util.New;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static lol.roxxane.roxxys_survival_core.Rsc.PRETTY_GSON;
import static lol.roxxane.roxxys_survival_core.util.Parsing.if_has_bool;
import static lol.roxxane.roxxys_survival_core.util.Parsing.json_to_normal;
import static net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR;

public class F3ScreenConfig {
	public static boolean survivalMode = true;
	private static final Path PATH = Path.of(CONFIGDIR.get() + "/roxxys_survival_core/client/f3_screen_modification.json");
	public static void load() {
		if (!Files.exists(PATH))
			try {
				make_default_file();
			} catch (Exception e) {
				throw new RuntimeException("Failed to create default file for %s".formatted(PATH), e);
			}
		try {
			read_file();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to read file for %s".formatted(PATH), e);
		}
	}
	private static void make_default_file() throws IOException {
		PATH.getParent().toFile().mkdirs();
		var data = PRETTY_GSON.toJsonTree(New.map(
			"survival_mode", true
		));
		var writer = new FileWriter(PATH.toString());
		writer.write(PRETTY_GSON.toJson(data));
		writer.close();
	}
	private static void read_file() throws FileNotFoundException {
		var reader = new JsonReader(new FileReader(PATH.toString()));
		@SuppressWarnings("unchecked") var data =
			(Map<String, Object>) json_to_normal(PRETTY_GSON.fromJson(reader, JsonObject.class));
		if_has_bool(data, "survival_mode", bool -> survivalMode = bool);
	}
}

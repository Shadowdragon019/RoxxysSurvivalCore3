package lol.roxxane.roxxys_survival_core;

import lol.roxxane.roxxys_survival_core.screens.NameTagScreen;
import net.minecraft.client.Minecraft;

public class RscClient {
	public static void load_tag_screen() {
		Minecraft.getInstance().setScreen(new NameTagScreen(NameTagScreen.TITLE));
	}
}

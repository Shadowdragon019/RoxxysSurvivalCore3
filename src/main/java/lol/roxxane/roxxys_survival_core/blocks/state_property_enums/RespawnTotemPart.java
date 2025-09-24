package lol.roxxane.roxxys_survival_core.blocks.state_property_enums;

import net.minecraft.util.StringRepresentable;

public enum RespawnTotemPart implements StringRepresentable {
	TOP,
	MIDDLE,
	BOTTOM;
	public String getSerializedName() {
		return name().toLowerCase();
	}
}

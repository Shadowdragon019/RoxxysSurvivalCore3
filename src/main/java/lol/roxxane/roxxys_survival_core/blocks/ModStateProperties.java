package lol.roxxane.roxxys_survival_core.blocks;

import lol.roxxane.roxxys_survival_core.blocks.state_property_enums.RespawnTotemPart;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModStateProperties {
	public static final EnumProperty<RespawnTotemPart> RESPAWN_TOTEM_PART = EnumProperty.create("part", RespawnTotemPart.class);
}

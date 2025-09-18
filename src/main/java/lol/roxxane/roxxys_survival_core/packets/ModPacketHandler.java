package lol.roxxane.roxxys_survival_core.packets;

import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		Id.mod("main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);
	public static void register() {
		var i = -1;
		INSTANCE.registerMessage(i++, ServerboundNameTagRenameItemPacket.class,
			(packet, buffer) -> buffer.writeUtf(packet.name),
			buffer -> new ServerboundNameTagRenameItemPacket(buffer.readUtf()),
			(packet, context) -> {
				context.get().enqueueWork(() -> {
					if (Util.isBlank(packet.name))
						return;
					var player = context.get().getSender();
					if (player == null) return;
					var mainHandStack = player.getMainHandItem();
					var offHandStack = player.getOffhandItem();
					if (mainHandStack.is(Items.NAME_TAG))
						mainHandStack.setHoverName(Component.literal(packet.name));
					else if (offHandStack.is(Items.NAME_TAG))
						offHandStack.setHoverName(Component.literal(packet.name));
				});
				context.get().setPacketHandled(true);
			});
	}
}

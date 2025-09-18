package lol.roxxane.roxxys_survival_core.screens;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.packets.ModPacketHandler;
import lol.roxxane.roxxys_survival_core.packets.ServerboundNameTagRenameItemPacket;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import oshi.util.Util;

import static net.minecraft.network.chat.Component.translatable;

public class NameTagScreen extends Screen {
	public static final Component TITLE = translatable("gui." + Rsc.ID + ".name_tag");
	public static final ResourceLocation BACKGROUND_TEXTURE = Id.mod("textures/gui/name_tag.png");
	public final int imageWidth = 122;
	public final int imageHeight = 60;
	public int topPos = 0;
	public int leftPos = 0;
	public EditBox editBox;
	public Button button;
	public NameTagScreen(Component title) {
		super(title);
	}
	public void tick() {
		editBox.tick();
		super.tick();
	}
	protected void init() {
		super.init();
		leftPos = (width - imageWidth) / 2;
		topPos = (height - imageHeight) / 2;
		editBox = new EditBox(font, leftPos + 9, topPos + 20, 101, 12, translatable("gui." + Rsc.ID + ".name_tag.edit_box"));
		editBox.setCanLoseFocus(false);
		editBox.setTextColor(-1);
		editBox.setTextColorUneditable(-1);
		editBox.setBordered(false);
		editBox.setMaxLength(100);
		button = Button.builder(translatable("gui." + Rsc.ID + ".name_tag.name_tag"), pressedButton -> {
			var name = editBox.getValue();
			if (Util.isBlank(name)) return;
			name = SharedConstants.filterText(name);
			if (name.length() > 100) return;
			ModPacketHandler.INSTANCE.sendToServer(new ServerboundNameTagRenameItemPacket(name));
		}).width(110).pos(leftPos + 6, topPos + 34).build();
		addWidget(editBox);
		addWidget(button);
		setInitialFocus(editBox);
	}
	public void resize(Minecraft minecraft, int width, int height) {
		var s = editBox.getValue();
		init(minecraft, width, height);
		editBox.setValue(s);
	}
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		assert minecraft != null;
		assert minecraft.player != null;
		if (keyCode == 256)
			minecraft.player.closeContainer();
		return editBox.keyPressed(keyCode, scanCode, modifiers) || editBox.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
	}
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		graphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		super.render(graphics, mouseX, mouseY, partialTick);
		graphics.drawString(font, title, leftPos + 6, topPos + 6, 0x404040, false);
		editBox.render(graphics, mouseX, mouseY, partialTick);
		button.render(graphics, mouseX, mouseY, partialTick);
	}
	public boolean isPauseScreen() {
		return false;
	}
}
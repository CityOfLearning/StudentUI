package com.dyn.student.proxy;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.server.utils.PlayerLevel;
import com.dyn.student.StudentUI;
import com.dyn.student.gui.Home;
import com.rabbit.gui.GuiFoundation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Client implements Proxy {

	private KeyBinding studentKey;

	@Override
	public void init() {
		if ((DYNServerMod.status == PlayerLevel.STUDENT)) {
			MinecraftForge.EVENT_BUS.register(this);
			studentKey = new KeyBinding("key.toggle.studentui", Keyboard.KEY_M, "key.categories.toggle");
			ClientRegistry.registerKeyBinding(studentKey);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Minecraft.getMinecraft().gameSettings.keyBindScreenshot.isPressed()) {
			// not sure if we can cancel keyevents but whatever this will save
			// an image to the specified directory
			event.setCanceled(true);
			ScreenShotHelper.saveScreenshot(DYNServerMod.screenshotPath, Minecraft.getMinecraft().displayWidth,
					Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
		}

		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
		if (studentKey.isPressed()) {
			GuiFoundation.proxy.display(new Home());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			if (event.entity == Minecraft.getMinecraft().thePlayer) {
				if (StudentUI.frozen) {
					event.setCanceled(true);
				}
			}
		}
	}

	/**
	 * @see forge.reference.proxy.Proxy#renderGUI()
	 */
	@Override
	public void renderGUI() {
		// Render GUI when on call from client
	}
}
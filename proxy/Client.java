package com.dyn.student.proxy;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.student.StudentUI;
import com.dyn.student.gui.Requests;
import com.dyn.utils.PlayerLevel;
import com.rabbit.gui.RabbitGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
		if ((DYNServerMod.status == PlayerLevel.STUDENT) && studentKey.isPressed()) {
			RabbitGui.proxy.display(new Requests());
		}
	}

	@SubscribeEvent
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
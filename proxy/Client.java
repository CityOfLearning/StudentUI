package com.dyn.student.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.packets.server.ServerCommandMessage;
import com.dyn.student.StudentUI;
import com.dyn.student.gui.Home;
import com.dyn.utils.BooleanChangeListener;
import com.dyn.utils.PlayerAccessLevel;
import com.rabbit.gui.RabbitGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Client implements Proxy {

	private KeyBinding homeKey;

	@Override
	public Map<String, ?> getKeyBindings() {
		Map<String, KeyBinding> keys = new HashMap();
//		keys.put("student", homeKey);
		return keys;
	}

	@Override
	public void init() {
		if ((DYNServerMod.accessLevel == PlayerAccessLevel.STUDENT)) {
			MinecraftForge.EVENT_BUS.register(this);
//			homeKey = new KeyBinding("key.toggle.studentui", Keyboard.KEY_M, "key.categories.toggle");
//			ClientRegistry.registerKeyBinding(homeKey);

			BooleanChangeListener listener = (event, show) -> {
				if (event.getDispatcher().getFlag()) {
					ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
					Minecraft.getMinecraft().thePlayer.addChatMessage(
							new ChatComponentText("You will thaw in 3 minutes or when your teacher unfreezes you"));
					Runnable task = () -> StudentUI.frozen.setFlag(false);
					executor.schedule(task, 3, TimeUnit.MINUTES);
				} else {
					Minecraft.getMinecraft().thePlayer
							.addChatMessage(new ChatComponentText("You are now free to move"));
					NetworkManager.sendToServer(new ServerCommandMessage(
							"/p user " + Minecraft.getMinecraft().thePlayer.getName() + " group remove _FROZEN_"));
				}
			};

			StudentUI.frozen.setFlag(false);
			StudentUI.frozen.addBooleanChangeListener(listener);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {

		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
//		if ((DYNServerMod.accessLevel == PlayerAccessLevel.STUDENT) && homeKey.isPressed()) {
//			RabbitGui.proxy.display(new Home());
//		}
	}

	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			if (event.entity == Minecraft.getMinecraft().thePlayer) {
				if (StudentUI.frozen.getFlag()) {
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
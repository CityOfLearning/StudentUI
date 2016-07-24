package com.dyn.student.proxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.packets.server.ServerCommandMessage;
import com.dyn.student.StudentUI;
import com.dyn.student.gui.Requests;
import com.dyn.utils.BooleanChangeListener;
import com.dyn.utils.PlayerLevel;
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

	private KeyBinding studentKey;

	@Override
	public void init() {
		if ((DYNServerMod.status == PlayerLevel.STUDENT)) {
			MinecraftForge.EVENT_BUS.register(this);
			studentKey = new KeyBinding("key.toggle.studentui", Keyboard.KEY_M, "key.categories.toggle");
			ClientRegistry.registerKeyBinding(studentKey);

			BooleanChangeListener listener = event -> {
				if (event.getDispatcher().getFlag()) {
					ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
					Minecraft.getMinecraft().thePlayer.addChatMessage(
							new ChatComponentText("You will thaw in 3 minutes or when your teacher unfreezes you"));
					Runnable task = () -> StudentUI.frozen.setFlag(false);
					executor.schedule(task, 3, TimeUnit.MINUTES);
				} else {
					Minecraft.getMinecraft().thePlayer
							.addChatMessage(new ChatComponentText("You are now free to move"));
					PacketDispatcher.sendToServer(new ServerCommandMessage("/p user "
							+ Minecraft.getMinecraft().thePlayer.getDisplayNameString() + " group remove _FROZEN_"));
				}
			};

			StudentUI.frozen.addBooleanChangeListener(listener);
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
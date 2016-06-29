package com.dyn.student.proxy;

import com.dyn.DYNServerMod;
import com.dyn.student.StudentUI;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Server implements Proxy {

	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	/**
	 * @see forge.reference.proxy.Proxy#renderGUI()
	 */
	@Override
	public void renderGUI() {
		// Actions on render GUI for the server (logging)

	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			if (DYNServerMod.frozenPlayers.contains(event.entity.getName())) {
					event.setCanceled(true);
			}
		}
	}

}
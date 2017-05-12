package com.dyn.student.proxy;

import java.util.Collections;
import java.util.Map;

import com.dyn.DYNServerMod;
import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.FEPermissions;
import com.forgeessentials.api.permissions.IPermissionsHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Server implements Proxy {

	public static final String DYN_PERM = FEPermissions.PLAYER + ".dyn";
	public static final String STUDENT_PERM = DYN_PERM + ".student";

	@Override
	public Map<String, ?> getKeyBindings() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);

		IPermissionsHelper perms = APIRegistry.perms;
		perms.getServerZone().getRootZone();

		perms.registerPermissionDescription(STUDENT_PERM, "DYN Student permissions");

		// ways of handling permissions
		// perms.registerPermission(PERM_COLUMN, PermissionLevel.TRUE,
		// "If true, all plots will always extend from bottom to top of the
		// world. Price will only depend on X and Z dimensions.");
		// perms.registerPermissionProperty(PERM_SIZE_MIN, "3", "Minimum size of
		// one plot axis");
		// perms.registerPermissionDescription(PERM_SIZE_MAX, "Maximum size of
		// one plot axis");

	}

	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			if (DYNServerMod.frozenPlayers.contains(event.entity.getName())) {
				event.setCanceled(true);
			}
		}
	}

	/**
	 * @see forge.reference.proxy.Proxy#renderGUI()
	 */
	@Override
	public void renderGUI() {
		// Actions on render GUI for the server (logging)

	}
}
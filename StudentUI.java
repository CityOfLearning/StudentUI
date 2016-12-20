package com.dyn.student;

import java.util.ArrayList;
import java.util.List;

import com.dyn.student.proxy.Proxy;
import com.dyn.student.reference.MetaData;
import com.dyn.student.reference.Reference;
import com.dyn.utils.BooleanListener;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:dyn|server")
public class StudentUI {

	public static List<String> plots = new ArrayList<>();
	public static BooleanListener needsRefresh = new BooleanListener(false);
	public static BooleanListener frozen = new BooleanListener(false);

	@Mod.Instance(Reference.MOD_ID)
	public static StudentUI instance;

	@SidedProxy(modId = Reference.MOD_ID, clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static Proxy proxy;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		proxy.init();
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MetaData.init(event.getModMetadata());
	}
}

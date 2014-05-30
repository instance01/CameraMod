package com.comze_instancelabs.cammod;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "CamMod")
public class CamMod {

	@Instance("CamMod")
	public static CamMod instance;

	@SideOnly(Side.CLIENT)
	public EntityCam camera;
	private boolean hideGUI;
	private float fovSetting;
	private int thirdPersonView;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		EntityRegistry.registerModEntity(EntityCam.class, "CamModCamera", EntityRegistry.findGlobalUniqueEntityId(), this, 256, 1, true);
		FMLCommonHandler.instance().bus().register(new KeyBind());
	}

	public boolean running = false;
	ArrayList<int[]> positions = new ArrayList<int[]>();

	public void addKey(int[] i) {
		positions.add(i);
	}

	@SideOnly(Side.CLIENT)
	public void startCam() {
		if (camera == null) {
			running = true;
			World world = Minecraft.getMinecraft().theWorld;
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			if (positions.size() < 1) {
				// int[] f = {(int) p.posX, (int) p.posY + 1, (int) p.posZ,
				// (int) p.rotationYaw, (int) p.rotationPitch};
				// addKey(f);
				p.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not start, please ensure that you have enough keys added."));
				running = false;
				return;
			}
			camera = new EntityCam(world, positions);

			if (camera.worldObj.spawnEntityInWorld(camera)) {
				Minecraft mc = Minecraft.getMinecraft();
				hideGUI = mc.gameSettings.hideGUI;
				fovSetting = mc.gameSettings.fovSetting;
				thirdPersonView = mc.gameSettings.thirdPersonView;

				mc.gameSettings.hideGUI = true;
				mc.gameSettings.fovSetting *= 1.1F;
				mc.gameSettings.thirdPersonView = thirdPersonView != 0 ? thirdPersonView : 1;
				mc.renderViewEntity = camera;
			} else {
				camera = null;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void stopCam() {
		if (camera != null) {
			running = false;
			Minecraft mc = Minecraft.getMinecraft();
			mc.gameSettings.hideGUI = hideGUI;
			mc.gameSettings.fovSetting = fovSetting;
			mc.gameSettings.thirdPersonView = thirdPersonView;
			mc.renderViewEntity = mc.thePlayer;

			camera.setDead();
			camera = null;
		}
	}

}

package com.comze_instancelabs.cammod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyBind {
	private static final String[] desc = { "Start Cam", "Stop Cam", "Add Key", "Reset Keys" };

	private static final int[] keyValues = { Keyboard.KEY_H, Keyboard.KEY_J, Keyboard.KEY_I, Keyboard.KEY_R };
	private final KeyBinding[] keys;

	public KeyBind() {
		keys = new KeyBinding[desc.length];
		for (int i = 0; i < desc.length; ++i) {
			keys[i] = new KeyBinding(desc[i], keyValues[i], "TestingStuff Controls");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
			if (keys[0].isPressed()) {
				// start the camera
				CamMod.instance.startCam();
			} else if (keys[1].isPressed()) {
				// stop the camera
				CamMod.instance.stopCam();
			} else if (keys[2].isPressed()){
				// add a position
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				p.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Added a key at: " + Integer.toString((int) p.posX) + " " + Integer.toString((int) p.posY) + " " + Integer.toString((int) p.posZ)));
				CamMod.instance.addKey(new int[] { (int) p.posX, (int) p.posY, (int) p.posZ, (int) p.rotationYaw, (int) p.rotationPitch });
			} else if( keys[3].isPressed()){
				// reset the keys
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				if(!CamMod.instance.running){
					p.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "All keys have been reset."));
					CamMod.instance.positions.clear();
				}else{
					p.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not reset keys, because the camera is running right now."));
				}
			}
		}
	}
}
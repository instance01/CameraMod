package com.comze_instancelabs.cammod;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCam extends EntityLivingBase {

	public EntityCam(World world) {
		super(world);
		setSize(0.0F, 0.0F);
		boundingBox.setBounds(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		yOffset = 0.0F;
	}

	ArrayList<int[]> target = new ArrayList<int[]>();
	int current = 0;
	
	double currentx = 0;
	double currenty = 0;
	double currentz = 0;
	double currentyaw = 0;
	double currentpitch = 0;
	
	double cx = 0D;
	double cy = 0D;
	double cz = 0D;
	double cyaw = 0;
	double cpitch = 0;
	
	public EntityCam(World world, ArrayList<int[]> pos) {
		this(world);
		this.target = pos;
		int[] target_ = target.get(0);
		setPositionAndRotation(target_[0], target_[1], target_[2], target_[3], target_[4]);
		setNextPoint();
	}

	public void setNextPoint(){
		current += 1;
		if(current > target.size() - 1){
			CamMod.instance.stopCam();
			return;
		}
		int[] target_ = target.get(current);
		currentx = (int) (target_[0] - posX);
		currenty = (int) (target_[1] - posY);
		currentz = (int) (target_[2] - posZ);
		currentyaw = (int) (target_[3] - rotationYaw);
		currentpitch = (int) (target_[4] - rotationPitch);
		
		while(currentyaw > 360){
			currentyaw -= 360;
		}
		while(currentpitch > 360){
			currentpitch -= 360;
		}
		
		if(Math.signum(currentx) * currentx > Math.signum(currentz) * currentz){
			if(currentx > 0 || currentx < 0){
				cx = Math.signum(currentx) * 0.05;
				cz = Math.signum(currentx) * 0.05 * (currentz / currentx);
				cy = Math.signum(currentx) * 0.05 * (currenty / currentx);
				//System.out.println(currentz + " " + currentx + " " + (currentz / currentx));
			}else{
				cx = Math.signum(currentx) * 0.05;
				cz = 0;
			}
			//System.out.println("A");
			cyaw = (int) (currentyaw / (currentx / cx));
			cpitch = (int) (currentpitch / (currentx / cx));
		}else{
			if(currentz > 0 || currentz < 0){
				cx = Math.signum(currentz) * 0.05 * (currentx / currentz);
				cz = Math.signum(currentz) * 0.05;
				cy = Math.signum(currentz) * 0.05 * (currenty / currentz);
				//System.out.println(currentz + " " + currentx + " " + (currentz / currentx));
			}else{
				cx = 0;
				cz = Math.signum(currentx) * 0.05;
			}
			//System.out.println("B");
			cyaw = currentyaw / (currentz / cz);
			cpitch = currentpitch / (currentz / cz);
		}
		//System.out.println(cx + " " + cy + " " + cz);
		System.out.println(cyaw + " " + cpitch);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (target != null) {
			int[] target_ = target.get(current);
			
			//setPosition(posX + (currentx / 200F), posY + (currenty / 200F), posZ + (currentz / 200F));
			setRotation(rotationYaw + (float)(currentyaw / 200F), rotationPitch + (float)(currentpitch / 200F));

			setPosition(posX + cx, posY + cy, posZ + cz);
			
			
			setRotation(rotationYaw + (float)cyaw, rotationPitch + (float)cpitch);

			//System.out.println(rotationYaw + " " + rotationPitch + " " + currentyaw +  " " + currentpitch);
			if(posX - target_[0] < 2 && posX - target_[0] > -2 && posY - target_[1] < 2 && posY - target_[1] > -2 && posZ - target_[2] < 2 && posZ - target_[2] > -2){
				setNextPoint();
			}
		}

		if (!onGround && motionY < 0.0) {
			motionY = 0.0;
		}

		Chunk chunk = worldObj.getChunkFromBlockCoords((int) posX, (int) posY);
		if (!chunk.isChunkLoaded) {
			worldObj.getChunkProvider().loadChunk(chunk.xPosition, chunk.zPosition);
		}

		if (Minecraft.getMinecraft().thePlayer == null) {
			CamMod.instance.stopCam();
		}
	}

	@Override
	public boolean isPotionActive(Potion potion) {
		return Minecraft.getMinecraft().thePlayer.isPotionActive(potion);
	}

	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public ItemStack getHeldItem() {
		return null;
	}

	@Override
	public ItemStack getEquipmentInSlot(int i) {
		return null;
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return new ItemStack[] { null, null, null, null, null };
	}

	@Override
	public void setCurrentItemOrArmor(int var1, ItemStack var2) {
		// TODO Auto-generated method stub
		
	}
}

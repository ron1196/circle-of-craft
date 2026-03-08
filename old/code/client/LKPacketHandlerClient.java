package lionking.client;

import lionking.common.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.packet.*;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.*;
import net.minecraft.server.*;
import net.minecraft.server.management.*;
import net.minecraft.src.*;
import net.minecraft.stats.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;
import net.minecraft.client.*;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.model.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.*;


import net.minecraft.src.*;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.*;
import net.minecraft.client.Minecraft;
import java.nio.ByteBuffer;

public class LKPacketHandlerClient implements IPacketHandler
{
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		byte[] data = packet.data;
		if (packet.channel.equals("lk.tileEntity"))
		{
			ByteBuffer buffer = ByteBuffer.wrap(data);
			int i = buffer.getInt(0);
			int j = buffer.getInt(4);
			int k = buffer.getInt(8);
			TileEntity tileentity = Minecraft.getMinecraft().theWorld.getBlockTileEntity(i, j, k);
			
			if (tileentity != null && tileentity instanceof LKTileEntityBugTrap)
			{
				for (int l = 0; l < 4; l++)
				{
					int id = buffer.getInt(12 + l * 8);
					int damage = buffer.getInt(16 + l * 8);
					if (Item.itemsList[id] != null)
					{
						((LKTileEntityBugTrap)tileentity).setInventorySlotContents(l, new ItemStack(id, 1, damage));
					}
				}
			}
			
			else if (tileentity != null && tileentity instanceof LKTileEntityHyenaHead)
			{
				((LKTileEntityHyenaHead)tileentity).setHyenaType(data[12]);
				((LKTileEntityHyenaHead)tileentity).setRotation(data[13]);
			}
			
			else if (tileentity != null && tileentity instanceof LKTileEntityMountedShooter)
			{
				if (data[12] == (byte)-1)
				{
					((LKTileEntityMountedShooter)tileentity).fireCounter = 20;
				}
				else
				{
					((LKTileEntityMountedShooter)tileentity).setShooterType(data[12]);
				}
			}
			
			else if (tileentity != null && tileentity instanceof LKTileEntityFurRug)
			{
				((LKTileEntityFurRug)tileentity).direction = data[12];
			}
			
			else if (tileentity != null && tileentity instanceof LKTileEntityMobSpawner)
			{
				double yaw = buffer.getDouble(12);
				double yaw2 = buffer.getDouble(20);
				int mobID = buffer.getInt(28);
				((LKTileEntityMobSpawner)tileentity).yaw = yaw;
				((LKTileEntityMobSpawner)tileentity).yaw2 = yaw2;
				((LKTileEntityMobSpawner)tileentity).setMobID(mobID);
			}
		}
		
		else if (packet.channel.equals("lk.particles"))
		{
			ByteBuffer buffer = ByteBuffer.wrap(data);
			double posX = buffer.getDouble(3);
			double posY = buffer.getDouble(11);
			double posZ = buffer.getDouble(19);
			double velX = buffer.getDouble(27);
			double velY = buffer.getDouble(35);
			double velZ = buffer.getDouble(43);
			World world = Minecraft.getMinecraft().theWorld;
			if (world != null)
			{
				world.spawnEntityInWorld(new LKEntityCustomFX(world, data[0], data[1], data[2] == (byte)1, posX, posY, posZ, velX, velY, velZ));
			}
		}
		
		else if (packet.channel.equals("lk.breakItem"))
		{
			Entity entity = mod_LionKing.proxy.getEntityFromID(ByteBuffer.wrap(data).getInt(0), Minecraft.getMinecraft().theWorld);
			if (entity != null && entity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entity;
				int type = data[5];
				if (type == (byte)0)
				{
					entityplayer.renderBrokenItemStack(new ItemStack(mod_LionKing.zebraBoots));
				}
				else if	(type == (byte)1)
				{
					entityplayer.renderBrokenItemStack(new ItemStack(mod_LionKing.wings));
				}
			}
		}
		
		else if (packet.channel.equals("lk.homePortal"))
		{
			ByteBuffer buffer = ByteBuffer.wrap(data);
			LKLevelData.homePortalX = buffer.getInt(0);
			LKLevelData.homePortalY = buffer.getInt(4);
			LKLevelData.homePortalZ = buffer.getInt(8);
		}
		
		else if (packet.channel.equals("lk.scar"))
		{
			LKLevelData.defeatedScar = data[0];
		}
		
		else if (packet.channel.equals("lk.mound"))
		{
			ByteBuffer buffer = ByteBuffer.wrap(data);
			LKLevelData.moundX = buffer.getInt(0);
			LKLevelData.moundY = buffer.getInt(4);
			LKLevelData.moundZ = buffer.getInt(8);
		}
		
		else if (packet.channel.equals("lk.outlanders"))
		{
			LKLevelData.outlandersHostile = data[0];
		}
		
		else if (packet.channel.equals("lk.zira"))
		{
			LKLevelData.ziraStage = data[0];
		}
		
		else if (packet.channel.equals("lk.pumbaa"))
		{
			LKLevelData.pumbaaStage = data[0];
		}
		
		else if (packet.channel.equals("lotr.hasSimba"))
		{
			ByteBuffer buffer = ByteBuffer.wrap(data);
			LKLevelData.setHasSimba(Minecraft.getMinecraft().thePlayer, data[0] == 1);
		}
		
		else if (packet.channel.equals("lk.questDoStage"))
		{
			LKQuestBase quest = LKQuestBase.allQuests[data[0]];
			quest.stagesCompleted[data[1]] = 1;
		}
		
		else if (packet.channel.equals("lk.questDelay"))
		{
			LKQuestBase quest = LKQuestBase.allQuests[data[0]];
			quest.stagesDelayed = data[1];
		}
		
		else if (packet.channel.equals("lk.questCheck"))
		{
			LKQuestBase quest = LKQuestBase.allQuests[data[0]];
			quest.checked = data[1];
		}
		
		else if (packet.channel.equals("lk.questStage"))
		{
			LKQuestBase quest = LKQuestBase.allQuests[data[0]];
			quest.currentStage = data[1];
		}
		
		else if (packet.channel.equals("lk.flatulence"))
		{
			LKLevelData.flatulenceSoundsRemaining = data[0];
		}
		
		else if (packet.channel.equals("lk.login"))
		{
			ByteBuffer buffer = ByteBuffer.wrap(data);
			
			LKLevelData.homePortalX = buffer.getInt(0);
			LKLevelData.homePortalY = buffer.getInt(4);
			LKLevelData.homePortalZ = buffer.getInt(8);
			LKLevelData.moundX = buffer.getInt(12);
			LKLevelData.moundY = buffer.getInt(16);
			LKLevelData.moundZ = buffer.getInt(20);
			
			LKLevelData.defeatedScar = data[24];
			LKLevelData.ziraStage = data[25];
			LKLevelData.pumbaaStage = data[26];
			LKLevelData.outlandersHostile = data[27];
			LKLevelData.flatulenceSoundsRemaining = data[28];
			
			for (int i = 0; i < 16; i++)
			{
				LKQuestBase quest = LKQuestBase.allQuests[i];
				if (quest == null)
				{
					continue;
				}
				
				quest.stagesDelayed = data[29 + i * 19 + 0];
				quest.currentStage = data[29 + i * 19 + 1];
				quest.stagesDelayed = data[29 + i * 19 + 2];
				
				for (int j = 0; j < 16; j++)
				{
					if (j >= quest.stagesCompleted.length)
					{
						continue;
					}
					quest.stagesCompleted[j] = data[29 + i * 19 + 3 + j];
				}
			}
		}
		
		else if (packet.channel.equals("lk.message"))
		{
			String message = new String(data);
			EntityPlayer entityplayer = Minecraft.getMinecraft().thePlayer;
			if (entityplayer != null)
			{
				entityplayer.addChatMessage(message);
			}
		}
	}
}

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

import cpw.mods.fml.client.FMLClientHandler;
import lionking.client.*;
import lionking.common.*;
import java.io.*;
import java.net.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.*;
import net.minecraftforge.event.world.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.common.network.*;
import java.nio.ByteBuffer;

public class LKTickHandlerClient implements ITickHandler
{
	private GuiScreen lastGuiOpen;
	private boolean checkedUpdate;
	private boolean wingFlight;
	private int wingTicks;
	public static HashMap playersInPortals = new HashMap();
	public static HashMap playersInOutPortals = new HashMap();
	public static LKEntityScar scarBoss;
	public static LKEntityZira ziraBoss;
	
	public void tickStart(EnumSet type, Object... tickData) {}
	
    public void tickEnd(EnumSet type, Object... tickData)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		
		if (type.equals(EnumSet.of(TickType.RENDER)))
		{
			float f = (Float)tickData[0];
			runRenderTick(f, minecraft);
		}
		
		if (type.equals(EnumSet.of(TickType.CLIENT)))
		{
			if (minecraft.currentScreen == null)
			{
				lastGuiOpen = null;
			}
			
			runClientTick(minecraft);
			
			((LKBlockLeaves)mod_LionKing.forestLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
			((LKBlockLeaves)mod_LionKing.mangoLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
			((LKBlockLeaves)mod_LionKing.leaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
			((LKBlockLeaves)mod_LionKing.passionLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
			((LKBlockLeaves)mod_LionKing.bananaLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
			((LKBlockRafikiLeaves)mod_LionKing.rafikiLeaves).setGraphicsLevel(minecraft.gameSettings.fancyGraphics);
			
			if (minecraft.thePlayer != null && minecraft.theWorld != null && !checkedUpdate && mod_LionKing.shouldCheckUpdates)
			{
				try
				{
					URL updateURL = new URL("http://dl.dropbox.com/s/b9odiuz504iy6ol/version.txt");
					HttpURLConnection updateConnection = (HttpURLConnection)updateURL.openConnection();
					BufferedReader updateReader = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
					String updateVersion = updateReader.readLine();
					updateReader.close();
					
					String version = null;
					
					for (ModContainer mod : Loader.instance().getModList())
					{
						if (mod.getMod() == mod_LionKing.instance)
						{
							version = mod.getVersion();
						}
					}
					
					if (version != null && updateVersion != null && !updateVersion.equals(version))
					{
						minecraft.thePlayer.addChatMessage("\u00a7eThe Lion King Mod: \u00a7fUpdate available! (" + updateVersion + ")");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				checkedUpdate = true;
			}
			
			if (minecraft.thePlayer != null && minecraft.theWorld != null)
			{
				EntityPlayer entityplayer = minecraft.thePlayer;
				if ((entityplayer.dimension == 0 || entityplayer.dimension == mod_LionKing.idPrideLands) && playersInPortals.containsKey(entityplayer))
				{
					if (LKIngame.isPlayerInLionPortal(entityplayer, true))
					{
						int i = (Integer)playersInPortals.get(entityplayer);
						i++;
						playersInPortals.put(entityplayer, Integer.valueOf(i));
						if (i >= 100)
						{
							Minecraft.getMinecraft().sndManager.playSoundFX("portal.travel", 1F, minecraft.theWorld.rand.nextFloat() * 0.4F + 0.8F);
							playersInPortals.remove(entityplayer);
						}
					}
					else
					{
						playersInPortals.remove(entityplayer);
					}
				}
				if ((entityplayer.dimension == mod_LionKing.idOutlands || entityplayer.dimension == mod_LionKing.idPrideLands) && playersInOutPortals.containsKey(entityplayer))
				{
					if (LKIngame.isPlayerInLionPortal(entityplayer, false))
					{
						int i = (Integer)playersInOutPortals.get(entityplayer);
						i++;
						playersInOutPortals.put(entityplayer, Integer.valueOf(i));
						if (i >= 100)
						{
							Minecraft.getMinecraft().sndManager.playSoundFX("portal.travel", 1F, minecraft.theWorld.rand.nextFloat() * 0.4F + 0.8F);
							playersInOutPortals.remove(entityplayer);
						}
					}
					else
					{
						playersInOutPortals.remove(entityplayer);
					}
				}
			}
			
			GuiScreen guiscreen = minecraft.currentScreen;
			if (guiscreen != null)
			{
				if (guiscreen instanceof GuiAchievements)
				{
					int page = ((GuiAchievements)guiscreen).currentPage;
					if (AchievementPage.getTitle(page).equals("Lion King"))
					{
						minecraft.thePlayer.openGui(mod_LionKing.instance, LKCommonProxy.GUI_ID_ACHIEVEMENTS, minecraft.theWorld, page, 0, 0);
					}
				}
				
				if (guiscreen instanceof LKGuiAchievements)
				{
					int page = ((LKGuiAchievements)guiscreen).currentPage;
					if (!(AchievementPage.getTitle(page).equals("Lion King")))
					{
						GuiAchievements gui = new GuiAchievements(minecraft.statFileWriter);
						gui.currentPage = page;
						FMLClientHandler.instance().displayGuiScreen(minecraft.thePlayer, gui);
					}
				}
				
				if (guiscreen instanceof GuiMainMenu && !(lastGuiOpen instanceof GuiMainMenu))
				{
					LKLevelData.needsLoad = true;
				}
				
				lastGuiOpen = guiscreen;
			}
        }
	}
	
	private void runRenderTick(float f, Minecraft minecraft)
	{
		if (minecraft.currentScreen == null)
		{
			if (scarBoss != null)
			{
				LKGuiIngame.drawBossHealth(minecraft, scarBoss);
			}
			
			if (ziraBoss != null)
			{
				LKGuiIngame.drawBossHealth(minecraft, ziraBoss);
			}
		}
		
		if (minecraft.thePlayer != null && minecraft.theWorld != null)
		{
			if (playersInPortals.containsKey(minecraft.thePlayer))
			{
				int i = (Integer)playersInPortals.get(minecraft.thePlayer);
				if (i > 0)
				{
					LKGuiIngame.renderPortalOverlay(0.1F + ((float)i / 100F) * 0.6F, minecraft, true);
				}
			}
			
			if (playersInOutPortals.containsKey(minecraft.thePlayer))
			{
				int i = (Integer)playersInOutPortals.get(minecraft.thePlayer);
				if (i > 0)
				{
					LKGuiIngame.renderPortalOverlay(0.1F + ((float)i / 100F) * 0.6F, minecraft, false);
				}
			}
		}
		
		if (LKIngame.flatulenceSoundTick > 0)
		{
			LKGuiIngame.renderFlatulenceOverlay(0.1F + ((float)LKIngame.flatulenceSoundTick / 25.0F) * 0.7F, minecraft);
		}
		
		if (LKIngame.loadRenderers)
		{
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
			LKIngame.loadRenderers = false;
		}
	}
	
	private void runClientTick(Minecraft minecraft)
	{
		EntityPlayerSP entityplayer = minecraft.thePlayer;
		World world = minecraft.theWorld;
		if (entityplayer != null && world != null)
		{
			Random random = world.rand;
			
			if (entityplayer.dimension == mod_LionKing.idPrideLands)
			{
				entityplayer.triggerAchievement(LKAchievementList.enterPrideLands);
			}
			
			ItemStack boots = entityplayer.inventory.armorItemInSlot(0);
			if (boots != null && boots.itemID == mod_LionKing.zebraBoots.itemID)
			{
				damageZebraBoots(entityplayer, boots);
			}
		
			ItemStack body = entityplayer.inventory.armorItemInSlot(2);
			if (body != null && body.itemID == mod_LionKing.wings.itemID)
			{
				applyWingFlight(entityplayer, body);
			}
			else
			{
				wingFlight = false;
			}
			
			ItemStack[] armor = entityplayer.inventory.armorInventory;
			if (armor[3] != null && armor[2] != null && armor[1] != null && armor[0] != null && armor[3].getItem() == mod_LionKing.ticketLionHead && armor[2].getItem() == mod_LionKing.ticketLionSuit && armor[1].getItem() == mod_LionKing.ticketLionLegs && armor[0].getItem() == mod_LionKing.ticketLionFeet)
			{
				entityplayer.triggerAchievement(LKAchievementList.ticketLionSuit);
				if (entityplayer.sprintingTicksLeft > 0 && random.nextInt(3) == 0)
				{
					double d = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;
					world.spawnEntityInWorld(new LKEntityCustomFX(world, 48, 32, false, (entityplayer.posX + (((double)(random.nextFloat() * entityplayer.width * 2.0F)) - (double)entityplayer.width) * 0.75F), entityplayer.posY - 1.0F + (double)(random.nextFloat() * entityplayer.height), (entityplayer.posZ + (((double)(random.nextFloat() * entityplayer.width * 2.0F)) - (double)entityplayer.width) * 0.75F), d, d1, d2));
				}
			}
			
			if (armor[3] != null && armor[3].getItem() == mod_LionKing.outlandsHelm && random.nextInt(3) == 0)
			{
				double d = random.nextGaussian() * 0.01D;
				double d1 = random.nextGaussian() * 0.02D;
				if (d1 < 0.0D)
				{
					d1 *= -1D;
				}
				double d2 = random.nextGaussian() * 0.01D;
				world.spawnParticle("flame", entityplayer.posX + ((((double)(random.nextFloat() * entityplayer.width * 2.0F)) - (double)entityplayer.width) * 0.25F), entityplayer.boundingBox.maxY + 0.3F + (double)(random.nextFloat() * 0.25F), (entityplayer.posZ + (((double)(random.nextFloat() * entityplayer.width * 2.0F)) - (double)entityplayer.width) * 0.25F), d, d1, d2);
			}
		}
	}
	
	private void damageZebraBoots(EntityPlayerSP entityplayer, ItemStack itemstack)
	{
		if (!entityplayer.worldObj.isAnyLiquid(entityplayer.boundingBox) && entityplayer.movementInput.moveForward > 0.0F && !(itemstack.getItemDamage() == itemstack.getMaxDamage()) && !wingFlight)
		{
			entityplayer.moveEntityWithHeading(entityplayer.movementInput.moveForward, 30F);
			sendDamageItemPacket(entityplayer, 0);
		}
	}
	
	private void applyWingFlight(EntityPlayerSP entityplayer, ItemStack itemstack)
	{
		if (entityplayer.onGround)
		{
			wingFlight = false;
		}
		if (entityplayer.posY < 0D)
		{
			wingFlight = false;
			wingTicks = 0;
		}
		if (wingTicks > 0)
		{
			wingTicks--;
			if (entityplayer.movementInput.sneak)
			{
				entityplayer.motionY = -0.22D;
			}
			else
			{
				entityplayer.motionY = ((double)(wingTicks / 12)) * 0.05D;
			}
		}
		if (!(itemstack.getItemDamage() == itemstack.getMaxDamage()))
		{
			if (entityplayer.motionY < 0D && !entityplayer.movementInput.sneak && wingFlight)
			{
				entityplayer.motionY = -0.22D;
			}
			if (entityplayer.movementInput.jump && wingTicks == 0 && entityplayer.posY < 240D && entityplayer.posY > 0D && !entityplayer.movementInput.sneak)
			{
				wingTicks = 50;
				entityplayer.motionY = 1D;
				sendDamageItemPacket(entityplayer, 1);
				wingFlight = false;
			}
		}
		if (itemstack.getItemDamage() == itemstack.getMaxDamage())
		{
			wingTicks = 0;
			wingFlight = true;
		}
	}
	
    public EnumSet ticks()
	{
		return EnumSet.of(TickType.RENDER, TickType.CLIENT);
	}

    public String getLabel()
	{
		return "The Lion King Mod";
	}
	
	private static void sendDamageItemPacket(EntityPlayer entityplayer, int type)
	{
		byte[] data = new byte[6];
		byte[] id = ByteBuffer.allocate(4).putInt(entityplayer.entityId).array();
		for (int i = 0; i < 4; i++)
		{
			data[i] = id[i];
		}
		data[4] = (byte)entityplayer.dimension;
		data[5] = (byte)type;
		Packet250CustomPayload packet = new Packet250CustomPayload("lk.damageItem", data);
		PacketDispatcher.sendPacketToServer(packet);
	}
}
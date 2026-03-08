package lionking.common;
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

import java.util.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.registry.*;

public class LKTickHandlerServer implements ITickHandler
{
	public static HashMap playersInPortals = new HashMap();
	public static HashMap playersInOutPortals = new HashMap();
	
	public void tickStart(EnumSet type, Object... tickData) {}
	
    public void tickEnd(EnumSet type, Object... tickData)
	{
		if (type.equals(EnumSet.of(TickType.WORLD)))
		{
			World world = (World)tickData[0];
			if (world == DimensionManager.getWorld(0))
			{
				if (LKLevelData.needsLoad)
				{
					LKLevelData.load();
					LKLevelData.needsLoad = false;
				}
				
				LKQuestBase.updateAllQuests();
				LKIngame.runMainWorldTick(world);
				
				if (LKLevelData.needsSave)
				{
					LKLevelData.save();
					LKLevelData.needsSave = false;
				}
				
				if (world.getWorldTime() % 600L == 0L)
				{
					LKLevelData.save();
				}
			}
			
			if (world == DimensionManager.getWorld(mod_LionKing.idPrideLands))
			{
				List players = world.playerEntities;
				if (players.size() > 0)
				{
					for (int player = 0; player < players.size(); player++)
					{
						EntityPlayer entityplayer = (EntityPlayer)players.get(player);
						int i = MathHelper.floor_double(entityplayer.posX);
						int j = MathHelper.floor_double(entityplayer.posY);
						int k = MathHelper.floor_double(entityplayer.posZ);
						for (int i1 = -8; i1 < 9; i1++)
						{
							for (int j1 = -8; j1 < 9; j1++)
							{
								for (int k1 = -8; k1 < 9; k1++)
								{
									if (world.getBlockId(i + i1, j + j1, k + k1) == Block.portal.blockID)
									{
										world.setBlockToAir(i + i1, j + j1, k + k1);
									}
								}
							}
						}
					}

					if (((WorldServer)world).allPlayersSleeping)
					{
						boolean allPlayersFullyAsleep = true;
						for (int i = 0; i < world.playerEntities.size(); i++)
						{
							EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(i);
							if (!entityplayer.isPlayerSleeping() || entityplayer.sleepTimer < 98)
							{
								allPlayersFullyAsleep = false;
							}
						}
						
						if (allPlayersFullyAsleep)
						{
							World overWorld = DimensionManager.getWorld(0);
							long time = world.getWorldInfo().getWorldTime() + 24000L;
							overWorld.getWorldInfo().setWorldTime(time - time % 24000L);
							overWorld.getWorldInfo().setRainTime(0);
							overWorld.getWorldInfo().setRaining(false);
							overWorld.getWorldInfo().setThunderTime(0);
							overWorld.getWorldInfo().setThundering(false);
							
							((WorldServer)world).allPlayersSleeping = false;
							Iterator it = world.playerEntities.iterator();

							while (it.hasNext())
							{
								EntityPlayer player = (EntityPlayer)it.next();

								if (player.isPlayerSleeping())
								{
									player.wakeUpPlayer(false, false, true);
								}
							}

							overWorld.provider.resetRainAndThunder();
						}
					}
				}
				
				if (!world.isRemote && LKLevelData.ziraStage == 13 && world.getChunkProvider().chunkExists(0, 0))
				{
					LKLevelData.setZiraStage(14);
					LKEntityZira zira = new LKEntityZira(world);
					zira.setLocationAndAngles(0, 103, 0, 0.0F, 0.0F);
					world.spawnEntityInWorld(zira);
					zira.spawnOutlandersInTree();
				}
			}
			
			if (world == DimensionManager.getWorld(mod_LionKing.idOutlands))
			{
				List players = world.playerEntities;
				if (players.size() > 0)
				{
					for (int player = 0; player < players.size(); player++)
					{
						EntityPlayer entityplayer = (EntityPlayer)players.get(player);
						if (player == 0 && world.rand.nextInt(120) == 0)
						{
							LKIngame.generateOutsand(entityplayer, world, world.rand);
						}
					
						int i = MathHelper.floor_double(entityplayer.posX);
						int j = MathHelper.floor_double(entityplayer.posY);
						int k = MathHelper.floor_double(entityplayer.posZ);
						for (int i1 = -8; i1 < 9; i1++)
						{
							for (int j1 = -8; j1 < 9; j1++)
							{
								for (int k1 = -8; k1 < 9; k1++)
								{
									if (world.getBlockId(i + i1, j + j1, k + k1) == Block.portal.blockID)
									{
										world.setBlockToAir(i + i1, j + j1, k + k1);
									}
								}
							}
						}
					}
				}
				
				LKTileEntityOutlandsPool.updateInventory(world);
				
				if (LKLevelData.ziraStage == 22 && players.size() > 0)
				{
					EntityPlayer entityplayer = (EntityPlayer)players.get(0);
					int i = MathHelper.floor_double(entityplayer.posX);
					int j = MathHelper.floor_double(entityplayer.boundingBox.minY);
					int k = MathHelper.floor_double(entityplayer.posZ);
					
					if (world.canBlockSeeTheSky(i, j, k) && j == world.getHeightValue(i, k))
					{
						int i1 = i - 8 + world.rand.nextInt(17);
						int k1 = k - 8 + world.rand.nextInt(17);
						int j1 = world.getHeightValue(i1, k1);

						LKEntityZira zira = new LKEntityZira(world);
						zira.selfTalkTick = 0;
						zira.setPosition(i1, j1, k1);
						zira.getLookHelper().setLookPosition(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ, 10.0F, (float)zira.getVerticalFaceSpeed());
						world.spawnEntityInWorld(zira);
						world.spawnEntityInWorld(new LKEntityLightning(entityplayer, world, (double)i1, (double)j1, (double)k1, 0));
						
						LKLevelData.setZiraStage(23);
					}
				}
			}
		}
		else if (type.equals(EnumSet.of(TickType.PLAYER)))
		{
			EntityPlayer entityplayer = (EntityPlayer)tickData[0];
			World world = entityplayer.worldObj;
			if (entityplayer != null && world != null)
			{
				if (entityplayer.dimension == mod_LionKing.idOutlands)
				{
					entityplayer.triggerAchievement(LKAchievementList.enterOutlands);
				}
				if (entityplayer.dimension == mod_LionKing.idUpendi)
				{
					entityplayer.triggerAchievement(LKAchievementList.upendi);
				}
				
				ItemStack[] armor = entityplayer.inventory.armorInventory;
				if (armor[2] != null && armor[2].getItem() == mod_LionKing.wings && !entityplayer.onGround)
				{
					entityplayer.triggerAchievement(LKAchievementList.wings);
				}
			
				if ((entityplayer.dimension == 0 || entityplayer.dimension == mod_LionKing.idPrideLands) && playersInPortals.containsKey(entityplayer))
				{
					if (LKIngame.isPlayerInLionPortal(entityplayer, true))
					{
						int i = (Integer)playersInPortals.get(entityplayer);
						i++;
						playersInPortals.put(entityplayer, Integer.valueOf(i));
						if (i >= 100)
						{
							int dimension = 0;
							if (entityplayer.dimension == 0)
							{
								dimension = mod_LionKing.idPrideLands;
							}
							else if (entityplayer.dimension == mod_LionKing.idPrideLands)
							{
								dimension = 0;
							}
							if (entityplayer instanceof EntityPlayerMP && world instanceof WorldServer)
							{
								MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entityplayer, dimension, new LKTeleporter(DimensionManager.getWorld(dimension), true, LKIngame.getSimbas(entityplayer)));
								LKIngame.doAdditionalPortalActions(entityplayer, dimension, true);
							}
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
							int dimension = mod_LionKing.idOutlands;
							if (entityplayer.dimension == mod_LionKing.idOutlands)
							{
								dimension = mod_LionKing.idPrideLands;
							}
							else if (entityplayer.dimension == mod_LionKing.idPrideLands)
							{
								dimension = mod_LionKing.idOutlands;
							}
							if (entityplayer instanceof EntityPlayerMP && world instanceof WorldServer)
							{
								MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entityplayer, dimension, new LKTeleporter(DimensionManager.getWorld(dimension), false, LKIngame.getSimbas(entityplayer)));
								LKIngame.doAdditionalPortalActions(entityplayer, dimension, false);
							}
							playersInOutPortals.remove(entityplayer);
						}
					}
					else
					{
						playersInOutPortals.remove(entityplayer);
					}
				}
			}
		}
	}
	
    public EnumSet ticks()
	{
		return EnumSet.of(TickType.WORLD, TickType.PLAYER);
	}

    public String getLabel()
	{
		return "The Lion King Mod";
	}
}
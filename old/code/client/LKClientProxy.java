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

import net.minecraft.src.*;
import lionking.common.*;
import net.minecraftforge.client.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.client.*;
import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.common.network.*;
import net.minecraft.client.Minecraft;
import java.nio.ByteBuffer;
import cpw.mods.fml.relauncher.Side;

public class LKClientProxy extends LKCommonProxy
{
	public static final ResourceLocation iconsTexture = new ResourceLocation("lionking:gui/icons.png");
	public static final ResourceLocation itemGlintTexture = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	
	private int grindingBowlRenderID;
	private int pillarRenderID;
	private int starAltarRenderID;
	private int vaseRenderID;
	private int bugTrapRenderID;
	private int aridGrassRenderID;
	private int kiwanoBlockRenderID;
	private int kiwanoStemRenderID;
	private int leverRenderID;
	private int lilyRenderID;
	private int rugRenderID;
	
	private LKTickHandlerClient tickHandler = new LKTickHandlerClient();
	private LKPacketHandlerClient clientHandler = new LKPacketHandlerClient();
	
	public void onPreload()
	{
		MinecraftForge.EVENT_BUS.register(new LKSound());
	}

	public void onLoad()
	{
		TickRegistry.registerTickHandler(tickHandler, Side.CLIENT);
		
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.tileEntity", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.particles", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.breakItem", Side.CLIENT);
		
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.homePortal", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.scar", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.mound", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.outlanders", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.zira", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.pumbaa", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.hasSimba", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.flatulence", Side.CLIENT);
		
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.questStage", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.questDelay", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.questCheck", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.questDoStage", Side.CLIENT);
		
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.login", Side.CLIENT);
		NetworkRegistry.instance().registerChannel(clientHandler, "lk.message", Side.CLIENT);
		
		RenderingRegistry.registerEntityRenderingHandler(LKEntityLionBase.class, new LKRenderLion());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityHyena.class, new LKRenderHyena());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityTicketLion.class, new LKRenderTicketLion());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityZazu.class, new LKRenderZazu(new LKModelZazu(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityBlueDart.class, new LKRenderDart("blue"));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityYellowDart.class, new LKRenderDart("yellow"));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityRedDart.class, new LKRenderDart("red"));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityBlackDart.class, new LKRenderDart("black"));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityZebra.class, new LKRenderZebra());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityRafiki.class, new LKRenderRafiki(new LKModelRafiki()));
        RenderingRegistry.registerEntityRenderingHandler(LKEntityThrownTermite.class, new RenderSnowball(mod_LionKing.itemTermite));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityScar.class, new LKRenderScar());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityCoin.class, new LKRenderThrownCoin());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityLightning.class, new LKRenderLightning());
		RenderingRegistry.registerEntityRenderingHandler(LKEntitySimba.class, new LKRenderSimba());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityOutlander.class, new LKRenderOutlander());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityVulture.class, new LKRenderVulture(new LKModelVulture(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityTermite.class, new LKRenderTermite());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityBug.class, new LKRenderBug());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityOutsand.class, new LKRenderOutsand());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityPumbaa.class, new LKRenderPumbaa());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityTimon.class, new LKRenderTimon());
        RenderingRegistry.registerEntityRenderingHandler(LKEntityPumbaaBomb.class, new RenderSnowball(mod_LionKing.pumbaaBomb));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityRhino.class, new LKRenderRhino());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityGemsbok.class, new LKRenderGemsbok());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityGemsbokSpear.class, new LKRenderSpear(false));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityPoisonedSpear.class, new LKRenderSpear(true));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityCrocodile.class, new LKRenderCrocodile());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityZazuEgg.class, new RenderSnowball(mod_LionKing.zazuEgg));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityScarRug.class, new LKRenderScarRug());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityZira.class, new LKRenderZira());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityTermiteQueen.class, new LKRenderTermiteQueen());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityGiraffe.class, new LKRenderGiraffe(new LKModelGiraffe(0F), new LKModelGiraffe(0.5F)));
		RenderingRegistry.registerEntityRenderingHandler(LKEntitySkeletalHyenaHead.class, new LKRenderSkeletalHyenaHead());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityCustomFX.class, new LKRenderCustomFX());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityDikdik.class, new LKRenderDikdik());
		RenderingRegistry.registerEntityRenderingHandler(LKEntityPinkDart.class, new LKRenderDart("pink"));
		RenderingRegistry.registerEntityRenderingHandler(LKEntityFlamingo.class, new LKRenderFlamingo(new LKModelFlamingo()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityGrindingBowl.class, new LKRenderGrindingStick());
		ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityBugTrap.class, new LKTileEntityBugTrapRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityHyenaHead.class, new LKTileEntityHyenaHeadRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityMountedShooter.class, new LKTileEntityMountedShooterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(LKTileEntityMobSpawner.class, new LKTileEntityMobSpawnerRenderer());
		
		grindingBowlRenderID = RenderingRegistry.getNextAvailableRenderId();
		pillarRenderID = RenderingRegistry.getNextAvailableRenderId();
		starAltarRenderID = RenderingRegistry.getNextAvailableRenderId();
		vaseRenderID = RenderingRegistry.getNextAvailableRenderId();
		bugTrapRenderID = RenderingRegistry.getNextAvailableRenderId();
		aridGrassRenderID = RenderingRegistry.getNextAvailableRenderId();
		kiwanoBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
		kiwanoStemRenderID = RenderingRegistry.getNextAvailableRenderId();
		leverRenderID = RenderingRegistry.getNextAvailableRenderId();
		lilyRenderID = RenderingRegistry.getNextAvailableRenderId();
		rugRenderID = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(grindingBowlRenderID, new LKRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(pillarRenderID, new LKRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(starAltarRenderID, new LKRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(vaseRenderID, new LKRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(bugTrapRenderID, new LKRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(aridGrassRenderID, new LKRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(kiwanoBlockRenderID, new LKRenderBlocks(true));
		RenderingRegistry.registerBlockHandler(kiwanoStemRenderID, new LKRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(leverRenderID, new LKRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(lilyRenderID, new LKRenderBlocks(false));
		RenderingRegistry.registerBlockHandler(rugRenderID, new LKRenderBlocks(true));
		
		MinecraftForgeClient.registerItemRenderer(mod_LionKing.tunnahDiggah.itemID, new LKRenderLargeItem());
	}

	public int getGrindingBowlRenderID()
	{
		return grindingBowlRenderID;
	}

	public int getPillarRenderID()
	{
		return pillarRenderID;
	}
	
	public int getStarAltarRenderID()
	{
		return starAltarRenderID;
	}
	
	public int getVaseRenderID()
	{
		return vaseRenderID;
	}
	
	public int getBugTrapRenderID()
	{
		return bugTrapRenderID;
	}
	
	public int getAridGrassRenderID()
	{
		return aridGrassRenderID;
	}
	
	public int getKiwanoBlockRenderID()
	{
		return kiwanoBlockRenderID;
	}
	
	public int getKiwanoStemRenderID()
	{
		return kiwanoStemRenderID;
	}
	
	public int getLeverRenderID()
	{
		return leverRenderID;
	}
	
	public int getLilyRenderID()
	{
		return lilyRenderID;
	}
	
	public int getRugRenderID()
	{
		return rugRenderID;
	}
	
	@Override
	public void setInPrideLandsPortal(EntityPlayer entityplayer)
	{
		if (!LKTickHandlerClient.playersInPortals.containsKey(entityplayer))
		{
			LKTickHandlerClient.playersInPortals.put(entityplayer, Integer.valueOf(0));
		}
		
		if (Minecraft.getMinecraft().isSingleplayer() && !LKTickHandlerServer.playersInPortals.containsKey(entityplayer))
		{
			LKTickHandlerServer.playersInPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	@Override
	public void setInOutlandsPortal(EntityPlayer entityplayer)
	{
		if (!LKTickHandlerClient.playersInOutPortals.containsKey(entityplayer))
		{
			LKTickHandlerClient.playersInOutPortals.put(entityplayer, Integer.valueOf(0));
		}
		
		if (Minecraft.getMinecraft().isSingleplayer() && !LKTickHandlerServer.playersInOutPortals.containsKey(entityplayer))
		{
			LKTickHandlerServer.playersInOutPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	@Override
	public void playPortalFXForUpendi(World world)
	{
		Minecraft.getMinecraft().sndManager.playSoundFX("portal.travel", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
	}
	
	@Override
	public void spawnParticle(String type, double d, double d1, double d2, double d3, double d4, double d5)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.renderViewEntity != null && mc.theWorld != null)
		{
			World world = mc.theWorld;
			int i = mc.gameSettings.particleSetting;
			
            if (i == 1 && world.rand.nextInt(3) == 0)
            {
                i = 2;
            }
			
			if (i > 1)
			{
				return;
			}
			
			if (type.equals("outlandsPortal"))
			{
				mc.effectRenderer.addEffect(new LKEntityPortalFX(world, d, d1, d2, d3, d4, d5, false));
			}
			
			if (type.equals("passion"))
			{
				mc.effectRenderer.addEffect(new LKEntityPassionFX(world, d, d1, d2, d3, d4, d5));
			}
			
			if (type.equals("prideLandsPortal"))
			{
				mc.effectRenderer.addEffect(new LKEntityPortalFX(world, d, d1, d2, d3, d4, d5, true));
			}
		}
	}
	
	@Override
	public EntityPlayer getSPPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}

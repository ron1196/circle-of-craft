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
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;

public class LKWorldProviderOutlands extends WorldProvider 
{
	@Override
    public void registerWorldChunkManager()
    {
        worldChunkMgr = new LKWorldChunkManagerOutlands(worldObj.getSeed(), worldObj.getWorldInfo().getTerrainType());
        dimensionId = mod_LionKing.idOutlands;
    }
    
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new LKChunkProviderOutlands(worldObj, worldObj.getSeed());
    }

	@Override
    public float calculateCelestialAngle(long l, float f)
    {
        return 0.5F;
    }
	
	@Override
    public int getMoonPhase(long l)
    {
        return 0;
    }

	@Override
    public boolean canRespawnHere()
    {
        return false;
    }
	
	@Override
    public String getWelcomeMessage()
	{
		return "Entering the Outlands";
	}
	
	@Override
    public String getDepartMessage()
	{
		return "Leaving the Outlands";
	}
	
	@Override
    public String getSaveFolder()
	{
		return "Outlands";
	}
	
	@Override
    public String getDimensionName()
    {
        return "Outlands";
    }
	
	@Override
    public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return false;
    }

	@Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return mod_LionKing.idPrideLands;
    }
}

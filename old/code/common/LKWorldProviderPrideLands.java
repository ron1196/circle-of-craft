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
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;

public class LKWorldProviderPrideLands extends WorldProvider 
{
	@Override
    public void registerWorldChunkManager()
    {
        worldChunkMgr = new LKWorldChunkManagerPrideLands(worldObj.getSeed(), worldObj.getWorldInfo().getTerrainType());
        dimensionId = mod_LionKing.idPrideLands;
    }
    
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new LKChunkProviderPrideLands(worldObj, worldObj.getSeed());
    }

	@Override
    public boolean canRespawnHere()
    {
        return true;
    }
	
	@Override
    public String getWelcomeMessage()
	{
		return "Entering the Pride Lands";
	}
	
	@Override
    public String getDepartMessage()
	{
		return "Leaving the Pride Lands";
	}
	
	@Override
    public String getSaveFolder()
	{
		return "PrideLands";
	}
	
	@Override
    public String getDimensionName()
    {
        return "Pride Lands";
    }
	
	@Override
    public ChunkCoordinates getSpawnPoint()
    {
        return new ChunkCoordinates(LKLevelData.homePortalX, LKLevelData.homePortalY, LKLevelData.homePortalZ);
    }

	@Override
    public void setSpawnPoint(int i, int j, int k)
    {
		if (!(i == 8 && j == 64 && k == 8))
		{
			LKLevelData.setHomePortalLocation(i, j, k);
		}
    }
	
	@Override
    public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return false;
    }
}

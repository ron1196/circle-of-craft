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

public class LKWorldProviderUpendi extends WorldProvider 
{
	@Override
    public void registerWorldChunkManager()
    {
        worldChunkMgr = new LKWorldChunkManagerUpendi();
        dimensionId = mod_LionKing.idUpendi;
    }
    
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new LKChunkProviderUpendi(worldObj, worldObj.getSeed());
    }
	
	@Override
    public Vec3 getFogColor(float f, float f1)
    {
		double R = 209;
		double G = 86;
		double B = 234;
        return worldObj.getWorldVec3Pool().getVecFromPool(R / 255, G / 255, B / 255);
    }

	@Override
    public boolean canRespawnHere()
    {
        return false;
    }
	
	@Override
    public String getWelcomeMessage()
	{
		return "Entering Upendi";
	}
	
	@Override
    public String getDepartMessage()
	{
		return "Leaving Upendi";
	}
	
	@Override
    public String getSaveFolder()
	{
		return "Upendi";
	}
	
	@Override
    public String getDimensionName()
    {
        return "Upendi";
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

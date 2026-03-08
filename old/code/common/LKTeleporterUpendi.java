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

import java.util.Random;
import java.util.List;

public class LKTeleporterUpendi extends Teleporter
{
	private List simbaData;
	private WorldServer worldObj;
	
    public LKTeleporterUpendi(WorldServer world, List list)
    {
		super(world);
		worldObj = world;
		simbaData = list;
    }

	@Override
    public void placeInPortal(Entity entity, double d, double d1, double d2, float f)
    {
		int i = MathHelper.floor_double(entity.posX);
		int k = MathHelper.floor_double(entity.posZ);
		int j = worldObj.getTopSolidOrLiquidBlock(i, k);
		entity.setLocationAndAngles((double)i + 0.5D, (double)j + 1D, (double)k + 0.5D, entity.rotationYaw, 0.0F);

		for (int l = 0; l < simbaData.size(); l++)
		{
			LKEntitySimba simba = new LKEntitySimba(worldObj);
			simba.readFromNBT((NBTTagCompound)simbaData.get(l));
			
			simba.setLocationAndAngles((double)i + 0.5D, (double)j + 1D, (double)k + 0.5D, entity.rotationYaw, 0.0F);
			simba.setVelocity(0D, 0D, 0D);
			
			worldObj.spawnEntityInWorld(simba);
			simba.applyTeleportationEffects(entity);
		}
    }
}
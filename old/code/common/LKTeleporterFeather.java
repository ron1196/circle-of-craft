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

public class LKTeleporterFeather extends Teleporter
{
	private Random random = new Random();
	private List simbaData;
	private WorldServer world;
	
    public LKTeleporterFeather(WorldServer worldserver, List list)
    {
		super(worldserver);
		world = worldserver;
		simbaData = list;
    }

	@Override
    public void placeInPortal(Entity entity, double d, double d1, double d2, float f)
    {
		int i = MathHelper.floor_double(entity.posX);
		int k = MathHelper.floor_double(entity.posZ);
		
		int i1 = i - 32 + random.nextInt(65);
		int k1 = k - 32 + random.nextInt(65);
		int j1 = world.getTopSolidOrLiquidBlock(i1, k1) + random.nextInt(12);
		
		entity.setLocationAndAngles((double)i1 + 0.5D, (double)j1 + 1D, (double)k1 + 0.5D, entity.rotationYaw, 0.0F);

		for (int i3 = 0; i3 < simbaData.size(); i3++)
		{
			LKEntitySimba simba = new LKEntitySimba(world);
			simba.readFromNBT((NBTTagCompound)simbaData.get(i3));
			
			simba.setLocationAndAngles((double)i1 + 0.5D, (double)j1 + 1D, (double)k1 + 0.5D, entity.rotationYaw, 0.0F);
			simba.setVelocity(0D, 0D, 0D);
			
			world.spawnEntityInWorld(simba);
			simba.applyTeleportationEffects(entity);
		}
    }
}
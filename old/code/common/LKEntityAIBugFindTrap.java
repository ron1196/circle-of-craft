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

public class LKEntityAIBugFindTrap extends EntityAIBase
{
    private LKEntityBug entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
	private World worldObj;
	private LKTileEntityBugTrap theTrap;

    public LKEntityAIBugFindTrap(LKEntityBug bug, double d)
    {
        entity = bug;
        speed = d;
		worldObj = bug.worldObj;
        setMutexBits(1);
    }

	@Override
    public boolean shouldExecute()
    {
		if (entity.getRNG().nextInt(5) != 0)
        {
            return false;
        }
        else
        {
			for (int i = -8; i <= 8; i++)
			{
				for (int j = -4; j <= 4; j++)
				{
					for (int k = -8; k <= 8; k++)
					{
						int i1 = i + MathHelper.floor_double(entity.posX);
						int j1 = j + MathHelper.floor_double(entity.posY);
						int k1 = k + MathHelper.floor_double(entity.posZ);
						if (worldObj.getBlockId(i1, j1, k1) == mod_LionKing.bugTrap.blockID)
						{
							LKTileEntityBugTrap tileentity = (LKTileEntityBugTrap)worldObj.getBlockTileEntity(i1, j1, k1);
							if (tileentity != null)
							{
								int l = entity.getRNG().nextInt(4);
								if (tileentity.getAverageBaitSaturation() > 0.0F)
								{
									xPosition = i1;
									yPosition = j1;
									zPosition = k1;
									theTrap = tileentity;
									entity.targetTrap = tileentity;
									return true;
								}
							}
						}
					}
				}
			}
        }
		return false;
    }

	@Override
    public boolean continueExecuting()
    {
        return !entity.getNavigator().noPath() && theTrap != null && (theTrap.getAverageBaitSaturation() > 0.0F || entity.trapTick > -1);
    }

	@Override
    public void startExecuting()
    {
        entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }
}

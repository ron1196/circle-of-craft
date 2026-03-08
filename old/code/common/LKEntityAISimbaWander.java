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

public class LKEntityAISimbaWander extends EntityAIBase
{
    private LKEntitySimba theSimba;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;

    public LKEntityAISimbaWander(LKEntitySimba simba, double d)
    {
        theSimba = simba;
        speed = d;
        setMutexBits(1);
    }

	@Override
    public boolean shouldExecute()
    {
		if (theSimba.isSitting())
		{
			return false;
		}
        if (theSimba.getAge() >= 100)
        {
            return false;
        }
        else if (theSimba.getRNG().nextInt(120) != 0)
        {
            return false;
        }
        else
        {
            Vec3 vec = RandomPositionGenerator.findRandomTarget(theSimba, 10, 7);

            if (vec == null)
            {
                return false;
            }
            else
            {
                xPosition = vec.xCoord;
                yPosition = vec.yCoord;
                zPosition = vec.zCoord;
                return true;
            }
        }
    }

	@Override
    public boolean continueExecuting()
    {
        return !theSimba.getNavigator().noPath() && !theSimba.isSitting();
    }

	@Override
    public void startExecuting()
    {
        theSimba.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }
}

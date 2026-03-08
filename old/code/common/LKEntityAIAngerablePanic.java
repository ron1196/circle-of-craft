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

public class LKEntityAIAngerablePanic extends EntityAIBase
{
    private EntityAnimal theAnimal;
    private double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public LKEntityAIAngerablePanic(EntityAnimal animal, double d)
    {
		theAnimal = animal;
        speed = d;
        setMutexBits(1);
    }

	@Override
    public boolean shouldExecute()
    {
		if (!theAnimal.isChild())
		{
			return false;
		}
        if (theAnimal.getAITarget() == null)
        {
            return false;
        }
        else
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(theAnimal, 5, 4);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                randPosX = vec3.xCoord;
                randPosY = vec3.yCoord;
                randPosZ = vec3.zCoord;
                return true;
            }
        }
    }

	@Override
    public void startExecuting()
    {
        theAnimal.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);
    }

    @Override
    public boolean continueExecuting()
    {
        return !theAnimal.getNavigator().noPath();
    }
}

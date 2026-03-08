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

public class LKEntityAIAmbientPanic extends EntityAIBase
{
    private EntityAmbientCreature entity;
    private double speed;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public LKEntityAIAmbientPanic(EntityAmbientCreature creature, double d)
    {
        entity = creature;
        speed = d;
        setMutexBits(1);
    }

	@Override
    public boolean shouldExecute()
    {
        if (entity.getAITarget() == null)
        {
            return false;
        }
        else
        {
            Vec3 v = LKAmbientPositionGenerator.findRandomTarget(entity, 5, 4);

            if (v == null)
            {
                return false;
            }
            else
            {
                xPosition = v.xCoord;
                yPosition = v.yCoord;
                zPosition = v.zCoord;
                return true;
            }
        }
    }

    @Override
    public void startExecuting()
    {
        entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }

    @Override
    public boolean continueExecuting()
    {
        return !entity.getNavigator().noPath();
    }
}

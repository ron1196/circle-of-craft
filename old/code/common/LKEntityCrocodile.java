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

import java.util.List;

public class LKEntityCrocodile extends EntityMob
{
	private static Item[] preyDropsTable = (new Item[]
	{
		mod_LionKing.zebraRaw, mod_LionKing.zebraRaw, mod_LionKing.zebraRaw, mod_LionKing.zebraHide, 
		mod_LionKing.rhinoRaw, mod_LionKing.rhinoRaw, mod_LionKing.horn, mod_LionKing.gemsbokHide, 
		mod_LionKing.gemsbokHide, mod_LionKing.gemsbokHide, mod_LionKing.featherBlue, mod_LionKing.featherBlue,
		mod_LionKing.featherYellow, Item.fishRaw, Item.fishRaw, Item.fishRaw, Item.rottenFlesh, Item.rottenFlesh
	});
	
    public LKEntityCrocodile(World world)
    {
        super(world);
        setSize(1.7F, 0.57F);
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(18D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.5D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(2D);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Integer.valueOf(0));
	}
	
	public int getSnapTime()
	{
		return dataWatcher.getWatchableObjectInt(20);
	}
	
	public void setSnapTime(int i)
	{
		dataWatcher.updateObject(20, Integer.valueOf(i));
	}
	
	@Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }
	
	@Override
    protected Entity findPlayerToAttack()
    {
        float f = getBrightness(1.0F);
        if (f < 0.5F)
        {
            double d = 16.0D;
            return worldObj.getClosestVulnerablePlayerToEntity(this, d);
        }
        else
        {
            return null;
        }
    }

	@Override
    protected String getLivingSound()
    {
        return "lionking:crocodile";
    }
	@Override
    protected String getDeathSound()
    {
        return "lionking:crocodiledeath";
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			setSnapTime(attackTime);
			
			if (inWater)
			{
				getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(4D);
			}
			else
			{
				getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(2D);
			}
		}
		
		if (entityToAttack == null && !hasPath() && worldObj.rand.nextInt(800) == 0)
		{
            List list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getAABBPool().getAABB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(10D, 4D, 10D));
            if (!list.isEmpty())
            {
                EntityAnimal entityanimal = (EntityAnimal)list.get(getRNG().nextInt(list.size()));
				boolean canAttack = true;
				if (entityanimal instanceof LKEntityLionBase)
				{
					canAttack = false;
				}
				if (entityanimal instanceof LKEntityGiraffe && ((LKEntityGiraffe)entityanimal).getSaddled())
				{
					canAttack = false;
				}
				if (canAttack)
				{
					setTarget(entityanimal);
				}
            }
		}
	}

	@Override
    protected int getDropItemId()
    {
        return mod_LionKing.crocodileMeat.itemID;
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
		super.dropFewItems(flag, i);
		if (getRNG().nextInt(5) == 0)
		{
			for(int j = 0; j < 1 + getRNG().nextInt(2) + getRNG().nextInt(1 + i); j++)
			{
				dropItem(preyDropsTable[getRNG().nextInt(preyDropsTable.length)].itemID, 1);
			}
		}
    }
	
	@Override
    protected void attackEntity(Entity entity, float f)
    {
        if (attackTime <= 0 && f < 2.6F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            attackEntityAsMob(entity);
			worldObj.playSoundAtEntity(this, "lionking:crocodilesnap", getSoundVolume(), getSoundPitch());
        }
    }
	
	@Override
    public boolean getCanSpawnHere()
    {
        if (worldObj.checkNoEntityCollision(boundingBox) && isValidLightLevel() && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0)
        {
			for (int i = -6; i < 7; i++)
			{
				for (int j = -6; j < 7; j++)
				{
					for (int k = -6; k < 7; k++)
					{
						int i1 = MathHelper.floor_double(posX) + i;
						int j1 = MathHelper.floor_double(posY) + j;
						int k1 = MathHelper.floor_double(posZ) + k;
						Block block = Block.blocksList[worldObj.getBlockId(i1, j1, k1)];
						if (block != null && block.blockMaterial == Material.water)
						{
							if (posY > 60)
							{
								return true;
							}
							else if (isCrocodileSpawnerNearby())
							{
								return true;
							}
							else if (getRNG().nextInt(3) == 0)
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
    }
	
	private boolean isCrocodileSpawnerNearby()
	{
		int i = MathHelper.floor_double(posX);
		int j =	MathHelper.floor_double(posY) - 1;
		int k = MathHelper.floor_double(posZ);
		for (int i1 = i - 8; i1 < i + 9; i1++)
		{
			for (int j1 = j - 4; j1 < j + 5; j1++)
			{
				for (int k1 = k - 8; k1 < k + 9; k1++)
				{
					if (worldObj.getBlockId(i1, j1, k1) == mod_LionKing.mobSpawner.blockID)
					{
						LKTileEntityMobSpawner spawner = (LKTileEntityMobSpawner)worldObj.getBlockTileEntity(i1, j1, k1);
						if (spawner != null && spawner.getMobID() == LKEntities.getEntityIDFromClass(getClass()))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
		Block block = Block.blocksList[worldObj.getBlockId(i, j-1, k)];
		Block block1 = Block.blocksList[worldObj.getBlockId(i, j, k)];
		if ((block != null && block.blockMaterial == Material.water) || (block1 != null && block1.blockMaterial == Material.water))
		{
			return 20.0F;
        }
		else
		{
			if (isInWater())
			{
				return getRNG().nextInt(6) == 0 ? -99999.0F : 20.0F;
			}
			else
			{
				return getRNG().nextInt(3) == 0 ? -99999.0F : 20.0F;
			}
		}
    }

	@Override
    public void moveEntityWithHeading(float f, float f1)
	{
		if (isInWater() && entityToAttack != null)
		{
            double d = posY;
            moveFlying(f, f1, isAIEnabled() ? 0.04F : 0.02F);
            moveEntity(motionX, motionY, motionZ);
            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
		}
		else
		{
			super.moveEntityWithHeading(f, f1);
		}
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}

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

public class LKEntityScarRug extends Entity
{
	private int talkTick = 40;
	
	public LKEntityScarRug(World world)
	{
		super(world);
		setSize(1.2F, 0.2F);
	}
	
	public LKEntityScarRug(World world, int i)
	{
		this(world);
		setType(i);
	}
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }
	
	@Override
    public void onUpdate()
    {
        super.onUpdate();
		
		if (talkTick < 40)
		{
			talkTick++;
		}
		
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.03999999910593033D;

        pushOutOfBlocks(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
        moveEntity(motionX, motionY, motionZ);
        float f = 0.98F;

        if (onGround)
        {
            f = 0.58800006F;
            int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

            if (i > 0)
            {
                f = Block.blocksList[i].slipperiness * 0.98F;
            }
        }

        motionX *= (double)f;
        motionY *= 0.9800000190734863D;
        motionZ *= (double)f;

        if (onGround)
        {
            motionY *= -0.5D;
        }
    }
	
	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(16, Integer.valueOf(0));
	}
	
	public void setType(int i)
	{
		dataWatcher.updateObject(16, Integer.valueOf(i));
	}
	
	public int getType()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Type", getType());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		setType(nbt.getInteger("Type"));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return boundingBox;
	}
	
	public void dropAsItem()
	{
		worldObj.playSoundAtEntity(this, "lionking:lionangry", 1.0F, (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
		if (!worldObj.isRemote)
		{
			entityDropItem(getRugItem(), 0.0F);
		}
		setDead();
	}
	
	public boolean interact(EntityPlayer entityplayer)
	{
		if (talkTick == 40)
		{
			worldObj.playSoundAtEntity(this, "lionking:lionroar", 1.0F, (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
			if (!worldObj.isRemote)
			{
				entityplayer.addChatMessage(LKCharacterSpeech.giveSpeech(getType() == 0 ? LKCharacterSpeech.RUG_SCAR : LKCharacterSpeech.RUG_ZIRA));
			}
			talkTick = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damage, float f)
	{
		return false;
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return getRugItem();
    }
	
	private ItemStack getRugItem()
	{
		return new ItemStack(getType() == 0 ? mod_LionKing.scarRug : mod_LionKing.ziraRug);
	}
}

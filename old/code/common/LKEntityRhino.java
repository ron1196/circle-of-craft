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

public class LKEntityRhino extends LKEntityQuestAnimal
{
    public LKEntityRhino(World world)
    {
        super(world);
        setSize(1.3F, 1.2F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.5D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1D, Item.wheat.itemID, false));
        tasks.addTask(3, new EntityAITempt(this, 1D, mod_LionKing.corn.itemID, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.3D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(16D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.2D);
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack.itemID == Item.wheat.itemID || itemstack.itemID == mod_LionKing.corn.itemID;
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LKEntityRhino(worldObj);
    }
	
	@Override
    protected int getDropItemId()
    {
        return mod_LionKing.horn.itemID;
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = getRNG().nextInt(2) + getRNG().nextInt(1 + i);
        for (int k = 0; k < j; k++)
        {
			dropItem(mod_LionKing.horn.itemID, 1);
        }
        j = getRNG().nextInt(2) + 1 + getRNG().nextInt(1 + i);
        for (int l = 0; l < j; l++)
        {
			if (isBurning())
			{
				dropItem(mod_LionKing.rhinoCooked.itemID, 1);
			}
			else
			{
				dropItem(mod_LionKing.rhinoRaw.itemID, 1);
			}
        }
    }
	
	@Override
	protected String getLivingSound()
    {
        return "lionking:rhino";
    }

	@Override
    protected String getHurtSound()
    {
        return "lionking:rhinohurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lionking:rhinodeath";
    }
	
	public LKCharacterSpeech getCharacterSpeech()
	{
		return LKCharacterSpeech.RHINO;
	}
	
	public LKCharacterSpeech getChildCharacterSpeech()
	{
		return LKCharacterSpeech.RHINO_CALF;
	}
	
	public String getAnimalName()
	{
		return "Rhino";
	}
	
	public ItemStack getQuestItem()
	{
		int i = getRNG().nextInt(5);
		switch (i)
		{
			default: case 0:
				return new ItemStack(mod_LionKing.leaves, 5 + getRNG().nextInt(8));
			case 1:
				return new ItemStack(Block.deadBush, 5 + getRNG().nextInt(10));
			case 2:
				return new ItemStack(mod_LionKing.cornStalk, 8 + getRNG().nextInt(16));
			case 3:
				return new ItemStack(Item.stick, 40 + getRNG().nextInt(25));
			case 4:
				return new ItemStack(Block.tallGrass, 4 + getRNG().nextInt(7), 2);
		}
	}
}

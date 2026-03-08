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

public class LKEntityAIAngerableAttackableTarget extends EntityAINearestAttackableTarget
{
	public LKEntityAIAngerableAttackableTarget(EntityCreature entity, Class entityclass, int i, boolean flag)
	{
		super(entity, entityclass, i, flag);
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (((LKAngerable)taskOwner).isHostile())
		{
			if (taskOwner instanceof LKEntityOutlander && !((LKEntityOutlander)taskOwner).inMound)
			{
				if (super.shouldExecute())
				{
					if (taskOwner.getAttackTarget() instanceof EntityPlayer)
					{
						EntityPlayer entityplayer = (EntityPlayer)taskOwner.getAttackTarget();
						ItemStack helmet = entityplayer.inventory.armorItemInSlot(3);
						if (helmet != null && helmet.itemID == mod_LionKing.outlandsHelm.itemID)
						{
							return false;
						}
					}
					return true;
				}
				return false;
			}
			return super.shouldExecute();
		}
		return false;
	}
}

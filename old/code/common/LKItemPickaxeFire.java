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

import net.minecraftforge.common.*;

public class LKItemPickaxeFire extends LKItemPickaxe
{
    public LKItemPickaxeFire(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

	@Override
    public boolean onBlockStartBreak(ItemStack itemstack, int i, int j, int k, EntityPlayer entityplayer) 
    {
        if (entityplayer.capabilities.isCreativeMode)
        {
            return false;
        }
		
		World world = entityplayer.worldObj;
		if (ForgeHooks.isToolEffective(itemstack, Block.blocksList[world.getBlockId(i, j, k)], world.getBlockMetadata(i, j, k)))
		{
			ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(world.getBlockId(i, j, k), 1, world.getBlockMetadata(i, j, k)));
			if (smeltingResult != null)
			{
				if (!world.isRemote)
				{
					entityplayer.addStat(StatList.mineBlockStatArray[world.getBlockId(i, j, k)], 1);
					entityplayer.addExhaustion(0.025F);
					for (int l = 0; l < Block.blocksList[world.getBlockId(i, j, k)].quantityDropped(itemRand); l++)
					{
						mod_LionKing.dropItemsFromBlock(world, i, j, k, new ItemStack(smeltingResult.itemID, 1, smeltingResult.getItemDamage()));
					}
					world.playAuxSFX(2001, i, j, k, world.getBlockId(i, j, k) + (world.getBlockMetadata(i, j, k) << 12));
					itemstack.damageItem(1, entityplayer);
					world.setBlockToAir(i, j, k);
				}
				for (int l = 0; l < 6; l++)
				{
					double d = (double)((float)i + itemRand.nextFloat());
					double d1 = (double)((float)j + itemRand.nextFloat());
					double d2 = (double)((float)k + itemRand.nextFloat());
					world.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
				}
				entityplayer.triggerAchievement(LKAchievementList.fireTool);
				return true;
			}
		}
		return false;
	}
}

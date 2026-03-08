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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.server.MinecraftServer;

public class LKItemDust extends LKItem
{
    public LKItemDust(int i)
    {
        super(i);
		setCreativeTab(LKCreativeTabs.tabQuest);
    }

	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
		if (LKQuestBase.rafikiQuest.getQuestStage() == 5 && LKQuestBase.rafikiQuest.isDelayed())
		{
			return false;
		}
		
		if (LKLevelData.hasSimba(entityplayer))
		{
			return false;
		}
		
        int i1 = world.getBlockId(i, j, k);
        if (i1 == mod_LionKing.starAltar.blockID)
		{
			itemstack.stackSize--;
			LKEntityLightning bolt = new LKEntityLightning(entityplayer, world, (double)i, (double)j, (double)k, 0);
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(bolt);
			}
			world.createExplosion(entityplayer, (double)i, (double)j + 1, (double)k, 0F, false);
			LKEntitySimba simba = new LKEntitySimba(world);
			simba.setLocationAndAngles(i + 0.5, j + 1, k + 0.5, 0F, 0F);
			simba.setAge(-36000);
			simba.setHealth(15);
			simba.setOwnerName(entityplayer.username);
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(simba);
			}
			entityplayer.triggerAchievement(LKAchievementList.simba);
			if (LKQuestBase.rafikiQuest.getQuestStage() == 6)
			{
				LKQuestBase.rafikiQuest.progress(7);
				LKIngame.sendMessageToAllPlayers("\u00a7e<Rafiki> \u00a7fYou see? He lives in you! Ohohoho!");
			}
			return true;
		}
		return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack)
    {
		return EnumRarity.uncommon;
    }
}

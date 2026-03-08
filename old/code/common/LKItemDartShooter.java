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

public class LKItemDartShooter extends LKItem
{
    private int shootTick;
	private boolean isSilver;

    public LKItemDartShooter(int i, boolean flag)
    {
        super(i);
        maxStackSize = 1;
        setMaxDamage(flag ? 286 : 214);
		shootTick = 20;
		isSilver = flag;
		setCreativeTab(LKCreativeTabs.tabCombat);
		setFull3D();
    }

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (entityplayer.inventory.hasItem(mod_LionKing.dartPink.itemID) && canShoot())
        {
            LKEntityDart dart = new LKEntityPinkDart(world, entityplayer, 2.0F, isSilver);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.dartPink.itemID);
				itemstack.damageItem(1, entityplayer);
			}
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(dart);
				entityplayer.triggerAchievement(LKAchievementList.shootDart);
				shootTick = 0;
            }
        }
		else if (entityplayer.inventory.hasItem(mod_LionKing.dartBlack.itemID) && canShoot())
        {
            LKEntityDart dart = new LKEntityBlackDart(world, entityplayer, 2.0F, isSilver);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.dartBlack.itemID);
				itemstack.damageItem(1, entityplayer);
			}
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(dart);
				entityplayer.triggerAchievement(LKAchievementList.shootDart);
				shootTick = 0;
            }
        }
		else if (entityplayer.inventory.hasItem(mod_LionKing.dartRed.itemID) && canShoot())
        {
            LKEntityDart dart = new LKEntityRedDart(world, entityplayer, 2.0F, isSilver);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.dartRed.itemID);
				itemstack.damageItem(1, entityplayer);
			}
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(dart);
				entityplayer.triggerAchievement(LKAchievementList.shootDart);
				shootTick = 0;
            }
        }
		else if (entityplayer.inventory.hasItem(mod_LionKing.dartYellow.itemID) && canShoot())
        {
            LKEntityDart dart = new LKEntityYellowDart(world, entityplayer, 2.0F, isSilver);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.dartYellow.itemID);
				itemstack.damageItem(1, entityplayer);
			}
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(dart);
				entityplayer.triggerAchievement(LKAchievementList.shootDart);
				shootTick = 0;
            }
        }
		else if (entityplayer.inventory.hasItem(mod_LionKing.dartBlue.itemID) && canShoot())
        {
            LKEntityDart dart = new LKEntityBlueDart(world, entityplayer, 1.5F, isSilver);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.consumeInventoryItem(mod_LionKing.dartBlue.itemID);
				itemstack.damageItem(1, entityplayer);
			}
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(dart);
				entityplayer.triggerAchievement(LKAchievementList.shootDart);
				shootTick = 0;
            }
        }
		return itemstack;
    }

	@Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if (shootTick < 20)
		{
			shootTick++;
		}
    }

    private boolean canShoot()
    {
		return isSilver ? shootTick >= 12 : shootTick == 20;
    }
}

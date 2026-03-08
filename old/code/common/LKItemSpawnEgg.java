package lionking.common;

import cpw.mods.fml.relauncher.*;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKItemSpawnEgg extends Item
{
    public LKItemSpawnEgg(int i)
    {
        super(i);
        setHasSubtypes(true);
        setCreativeTab(LKCreativeTabs.tabMisc);
    }

	@Override
    public String getItemDisplayName(ItemStack itemstack)
    {
        String itemName = "Spawn";
        String entityName = LKEntities.getStringFromID(itemstack.getItemDamage());

        if (entityName != null)
        {
            itemName = itemName + " " + entityName;
        }

        return itemName;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int i)
    {
        EntityEggInfo info = (EntityEggInfo)LKEntities.creatures.get(Integer.valueOf(itemstack.getItemDamage()));
        return info != null ? (i == 0 ? info.primaryColor : info.secondaryColor) : 16777215;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            int id = world.getBlockId(i, j, k);
            i += Facing.offsetsXForSide[l];
            j += Facing.offsetsYForSide[l];
            k += Facing.offsetsZForSide[l];
            double d = 0.0D;

            if (l == 1 && Block.blocksList[id] != null && Block.blocksList[id].getRenderType() == 11)
            {
                d = 0.5D;
            }

            Entity entity = spawnCreature(world, itemstack.getItemDamage(), (double)i + 0.5D, (double)j + d, (double)k + 0.5D);

			if (entity != null)
            {
                if (entity instanceof EntityLiving && itemstack.hasDisplayName())
                {
                    ((EntityLiving)entity).setCustomNameTag(itemstack.getDisplayName());
                }
				
				if (entity instanceof LKEntitySimba)
				{
					((LKEntitySimba)entity).setOwnerName(entityplayer.username);
				}
				
                if (!entityplayer.capabilities.isCreativeMode)
                {
                    itemstack.stackSize--;
                }
            }

            return true;
        }
    }

    private Entity spawnCreature(World world, int id, double d, double d1, double d2)
    {
        if (!LKEntities.creatures.containsKey(Integer.valueOf(id)))
        {
            return null;
        }
        else
        {
            Entity entity = LKEntities.createEntityByID(id, world);
			if (entity != null && entity instanceof EntityLiving)
			{
				EntityLiving entityliving = (EntityLiving)entity;
				entity.setLocationAndAngles(d, d1, d2, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.onSpawnWithEgg(null);
				world.spawnEntityInWorld(entity);
				entityliving.playLivingSound();
			}
            return entity;
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamageForRenderPass(int i, int j)
    {
        return Item.monsterPlacer.getIconFromDamageForRenderPass(i, j);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister) {}

	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int i, CreativeTabs tab, List list)
    {
        Iterator it = LKEntities.creatures.values().iterator();

        while (it.hasNext())
        {
            EntityEggInfo info = (EntityEggInfo)it.next();
            list.add(new ItemStack(i, 1, info.spawnedID));
        }
    }
}

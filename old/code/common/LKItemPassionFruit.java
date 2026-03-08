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

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

public class LKItemPassionFruit extends LKItemFood
{
    public LKItemPassionFruit(int i)
    {
        super(i, 0, 0.0F, false);
    }

	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize--;
		mod_LionKing.proxy.playPortalFXForUpendi(world);
        if (!world.isRemote && entityplayer instanceof EntityPlayerMP)
        {
            byte dimension = (byte)mod_LionKing.idUpendi;
			if (entityplayer.dimension == mod_LionKing.idUpendi)
			{
				dimension = (byte)mod_LionKing.idPrideLands;
			}
			else
			{
				dimension = (byte)mod_LionKing.idUpendi;
			}
			MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entityplayer, dimension, new LKTeleporterUpendi(DimensionManager.getWorld(dimension), LKIngame.getSimbas(entityplayer)));
        }
		return itemstack;
    }

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (entityplayer.dimension == mod_LionKing.idUpendi || entityplayer.dimension == mod_LionKing.idPrideLands)
		{
			if (entityplayer.capabilities.isCreativeMode || entityplayer.getHealth() == entityplayer.getMaxHealth())
			{
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
		}
        return itemstack;
    }
}

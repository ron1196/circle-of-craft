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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class LKItemVase extends LKItem
{
	@SideOnly(Side.CLIENT)
	private Icon[] vaseIcons;
	private String[] vaseTypes = {"flowerWhite", "flowerBlue", "flowerPurple", "flowerRed", "acacia", "rainforest", "mango", "outshroom", "outshroomGlowing", "passion", "banana"};
	
    public LKItemVase(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
		setCreativeTab(LKCreativeTabs.tabDeco);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int i)
    {
		if (i >= vaseTypes.length)
		{
			i = 0;
		}
		return vaseIcons[i];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
		vaseIcons = new Icon[vaseTypes.length];
		for (int i = 0; i < vaseTypes.length; i++)
		{
			vaseIcons[i] = iconregister.registerIcon("lionking:vase_" + vaseTypes[i]);
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(int i, CreativeTabs tab, List list)
    {    	
		for (int j = 0; j <= 10; j++)
		{
			list.add(new ItemStack(i, 1, j));
		}
    }
	
	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
    {
        int i1 = world.getBlockId(i, j, k);
        if (i1 == Block.snow.blockID)
        {
            l = 1;
        }
        else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID && (Block.blocksList[i1] != null && !Block.blocksList[i1].isBlockReplaceable(world, i, j, k)))
        {
            if (l == 0)
            {
                --j;
            }
            if (l == 1)
            {
                ++j;
            }
            if (l == 2)
            {
                --k;
            }
            if (l == 3)
            {
                ++k;
            }
            if (l == 4)
            {
                --i;
            }
            if (l == 5)
            {
                ++i;
            }
        }
        if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }
		Block block = mod_LionKing.flowerVase;
        if (block.canPlaceBlockAt(world, i, j, k))
        {		
            if (!world.isRemote)
            {
				world.setBlock(i, j, k, mod_LionKing.flowerVase.blockID, itemstack.getItemDamage(), 3);
                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                itemstack.stackSize--;
				List list = world.getEntitiesWithinAABB(LKEntityRafiki.class, entityplayer.boundingBox.addCoord(16F, 5F, 16F).addCoord(-16F, -5F, -16F));
				if (list.size() > 0 && itemstack.getItemDamage() < 4)
				{
					LKIngame.sendMessageToAllPlayers(LKCharacterSpeech.giveSpeech(LKCharacterSpeech.FLOWERS));
					for (Object obj: list)
					{
						((LKEntityRafiki)obj).addHeartFX();
					}
				}
				return true;
			}
		}	
		return false;
    }
}
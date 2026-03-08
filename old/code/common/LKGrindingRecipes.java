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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LKGrindingRecipes
{
    private static LKGrindingRecipes grindingBase = new LKGrindingRecipes();
    private Map grindingList = new HashMap();
	private Map metaGrindingList = new HashMap();
	
    public static LKGrindingRecipes grinding()
    {
        return grindingBase;
    }

    private LKGrindingRecipes()
    {
        addGrinding(mod_LionKing.hyenaBone.itemID, new ItemStack(mod_LionKing.hyenaBoneShard));
        addGrinding(mod_LionKing.mango.itemID, new ItemStack(mod_LionKing.mangoDust));
        addGrinding(mod_LionKing.itemTermite.itemID, new ItemStack(mod_LionKing.termiteDust));
        addGrinding(mod_LionKing.horn.itemID, new ItemStack(mod_LionKing.hornGround));
        addGrinding(mod_LionKing.whiteFlower.blockID, new ItemStack(mod_LionKing.rugDye, 1, 0));
        addGrinding(mod_LionKing.featherBlue.itemID, new ItemStack(mod_LionKing.rugDye, 1, 1));
        addGrinding(mod_LionKing.featherYellow.itemID, new ItemStack(mod_LionKing.rugDye, 1, 2));
        addGrinding(mod_LionKing.featherRed.itemID, new ItemStack(mod_LionKing.rugDye, 1, 3));
        addGrinding(mod_LionKing.purpleFlower.itemID, new ItemStack(mod_LionKing.rugDye, 1, 4));
        addGrinding(mod_LionKing.blueFlower.blockID, new ItemStack(mod_LionKing.rugDye, 1, 5));
        addGrinding(mod_LionKing.leaves.blockID, new ItemStack(mod_LionKing.rugDye, 1, 6));
        addGrinding(mod_LionKing.forestLeaves.blockID, new ItemStack(mod_LionKing.rugDye, 1, 6));
        addGrinding(mod_LionKing.mangoLeaves.blockID, new ItemStack(mod_LionKing.rugDye, 1, 6));
		addGrinding(mod_LionKing.bananaLeaves.blockID, new ItemStack(mod_LionKing.rugDye, 1, 6));
		addGrinding(mod_LionKing.redFlower.itemID, new ItemStack(mod_LionKing.rugDye, 1, 3));
		addGrinding(mod_LionKing.featherBlack.itemID, new ItemStack(mod_LionKing.rugDye, 1, 7));
		addGrinding(mod_LionKing.nukaShard.itemID, new ItemStack(mod_LionKing.poison));
		addGrinding(mod_LionKing.lily.blockID, 0, new ItemStack(mod_LionKing.rugDye, 1, 0));
		addGrinding(mod_LionKing.lily.blockID, 1, new ItemStack(mod_LionKing.rugDye, 1, 8));
		addGrinding(mod_LionKing.lily.blockID, 2, new ItemStack(mod_LionKing.rugDye, 1, 3));
		addGrinding(mod_LionKing.pridestone.blockID, new ItemStack(Block.sand));
		addGrinding(mod_LionKing.featherPink.itemID, new ItemStack(mod_LionKing.rugDye, 1, 9));
		addGrinding(mod_LionKing.passionLeaves.blockID, new ItemStack(mod_LionKing.rugDye, 1, 10));
		
		for (int i = 0; i <= 2; i++)
		{
			addGrinding(mod_LionKing.pridePillar.blockID, i, new ItemStack(mod_LionKing.pridePillar, 1, i + 1));
		}
		
		for (int i = 4; i <= 6; i++)
		{
			addGrinding(mod_LionKing.pridePillar.blockID, i, new ItemStack(mod_LionKing.pridePillar, 1, i + 1));
		}
    }

    private void addGrinding(int i, ItemStack itemstack)
    {
        grindingList.put(Integer.valueOf(i), itemstack);
    }
	
    private void addGrinding(int i, int j, ItemStack itemstack)
    {
        metaGrindingList.put(Arrays.asList(i, j), itemstack);
    }

    public ItemStack getGrindingResult(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return null;
        }
        ItemStack stack = (ItemStack)metaGrindingList.get(Arrays.asList(itemstack.itemID, itemstack.getItemDamage()));
        if (stack != null)
        {
            return stack;
        }
        return (ItemStack)grindingList.get(Integer.valueOf(itemstack.itemID));
    }
}

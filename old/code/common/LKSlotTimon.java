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

public class LKSlotTimon extends Slot
{
	private LKEntityTimon theTimon;
	public int cost;

    public LKSlotTimon(IInventory iinventory, int i, int j, int k, LKEntityTimon timon, int l)
    {
        super(iinventory, i, j, k);
		theTimon = timon;
		cost = l;
    }

	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

	@Override
    public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack)
    {
		for (int i = 0; i < cost; i++)
		{
			entityplayer.inventory.consumeInventoryItem(mod_LionKing.bug.itemID);
		}
		theTimon.setEaten();
		entityplayer.triggerAchievement(LKAchievementList.hakunaMatata);
		super.onPickupFromSlot(entityplayer, itemstack);
    }
}

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

public class LKSlotQuiver extends Slot
{
    public LKSlotQuiver(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }
	
	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
        Item i = itemstack.getItem();
		return i == mod_LionKing.dartBlue || i == mod_LionKing.dartYellow || i == mod_LionKing.dartRed || i == mod_LionKing.dartBlack || i == mod_LionKing.dartPink;
	}
}

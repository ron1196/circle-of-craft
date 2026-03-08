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
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.Random;

public class LKItemSpear extends LKItem
{
	private boolean poisoned;
	
    public LKItemSpear(int i, boolean flag)
    {
        super(i);
        maxStackSize = 1;
        setMaxDamage(160);
		poisoned = flag;
		setCreativeTab(LKCreativeTabs.tabCombat);
		setFull3D();
    }

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        LKEntitySpear spear = poisoned ? new LKEntityPoisonedSpear(world, entityplayer, 2.0F, itemstack.getItemDamage()) : new LKEntityGemsbokSpear(world, entityplayer, 2.0F, itemstack.getItemDamage());
        world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.25F);
		entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(spear);
        }
		return itemstack;
    }
	
	@Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user)
    {
        itemstack.damageItem(1, user);
		if (poisoned && itemRand.nextInt(3) != 0)
		{
			hitEntity.addPotionEffect(new PotionEffect(Potion.poison.id, (itemRand.nextInt(3) + 1) * 20, 0));
		}
        return true;
    }
	
	@Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", poisoned ? 3D : 4D, 0));
        return multimap;
    }
}

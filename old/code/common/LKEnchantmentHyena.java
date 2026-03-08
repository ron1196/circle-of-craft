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

public class LKEnchantmentHyena extends EnchantmentDamage
{
    public LKEnchantmentHyena(int i, int j)
    {
        super(i, j, 0);
    }

	@Override
    public int getMinEnchantability(int i)
    {
        return 5 + (i - 1) * 8;
    }
	
	@Override
    public int getMaxEnchantability(int i)
    {
        return getMinEnchantability(i) + 20;
    }

	@Override
    public int getMaxLevel()
    {
        return 5;
    }

	@Override
    public float calcModifierLiving(int i, EntityLivingBase entity)
    {
        return entity instanceof LKEntityHyena || entity instanceof LKEntitySkeletalHyenaHead ? (float)i * 4F : 0F;
    }

	@Override
    public String getName()
    {
        return "enchantment.damage.hyena";
    }

	@Override
    public boolean canApplyTogether(Enchantment iEnchantment)
    {
        return !(iEnchantment instanceof EnchantmentDamage);
    }
}

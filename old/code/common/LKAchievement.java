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
import java.util.List;

public class LKAchievement extends Achievement
{
    public final int displayColumn;
    public final int displayRow;
    public final LKAchievement parentAchievement;
    private final String achievementDescription;
    private IStatStringFormat statStringFormatter;
    public final ItemStack theItemStack;
    private boolean isSpecial;
	public String lkAchievementTitle;

    public LKAchievement(int i, String s, int j, int k, Item item, LKAchievement achievement)
    {
        this(i, s, j, k, new ItemStack(item), achievement);
    }

    public LKAchievement(int i, String s, int j, int k, Block block, LKAchievement achievement)
    {
        this(i, s, j, k, new ItemStack(block), achievement);
    }

    public LKAchievement(int i, String s, int j, int k, ItemStack itemstack, LKAchievement achievement)
    {
        super(0x19941994 + i, s, j, k, itemstack, achievement);
        theItemStack = itemstack;
        achievementDescription = s;
        displayColumn = j;
        displayRow = k;
        if (j < LKAchievementList.minDisplayColumn)
        {
            LKAchievementList.minDisplayColumn = j;
        }
        if (k < LKAchievementList.minDisplayRow)
        {
            LKAchievementList.minDisplayRow = k;
        }
        if (j > LKAchievementList.maxDisplayColumn)
        {
            LKAchievementList.maxDisplayColumn = j;
        }
        if (k > LKAchievementList.maxDisplayRow)
        {
            LKAchievementList.maxDisplayRow = k;
        }
        parentAchievement = achievement;
    }

    public LKAchievement setIndependent()
    {
        isIndependent = true;
        return this;
    }

    public LKAchievement setSpecial()
    {
        isSpecial = true;
        return this;
    }

    public LKAchievement registerAchievement()
    {
		super.registerAchievement();
		AchievementList.achievementList.remove(this);
        return this;
    }

	@Override
    public boolean isAchievement()
    {
        return true;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public String getName()
    {
        return lkAchievementTitle;
    }

	@Override
    public String getDescription()
    {
        if (statStringFormatter != null)
        {
            return statStringFormatter.formatString(achievementDescription);
        }
        else
        {
            return achievementDescription;
        }
    }

    public LKAchievement setStatStringFormatter(IStatStringFormat istatstringformat)
    {
        statStringFormatter = istatstringformat;
        return this;
    }

	@Override
    public boolean getSpecial()
    {
        return isSpecial;
    }

	@Override
    public StatBase registerStat()
    {
        return registerAchievement();
    }

	@Override
    public StatBase initIndependentStat()
    {
        return setIndependent();
    }
	
	@Override
    public String toString()
    {
        return lkAchievementTitle;
    }
}

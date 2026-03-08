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

public class LKQuestRafiki extends LKQuestBase
{
	public LKQuestRafiki(int i)
	{
		super(i);
	}
	
	public boolean canStart()
	{
		return true;
	}
	
	public String[] getRequirements()
	{
		return null;
	}
	
	public int getNumStages()
	{
		return 7;
	}
	
	public ItemStack getIcon()
	{
		return new ItemStack(mod_LionKing.rafikiStick);
	}
	
	public String getObjectiveByStage(int i)
	{
		switch (i)
		{
			default: return "";
			case 0: return "Speak to Rafiki in his tree at the centre of the world";
			case 1: return "Bring Rafiki a full stack of hyena bones";
			case 2: return "Find and defeat Scar";
			case 3: return "Return to Rafiki";
			case 4: return "Bring Rafiki four ground termites";
			case 5: return "Bring Rafiki four ground mangoes";
			case 6: return "Build a Star Altar and use the Rafiki Dust on it";
			case 7: return "Quest complete!";
		}
	}
}

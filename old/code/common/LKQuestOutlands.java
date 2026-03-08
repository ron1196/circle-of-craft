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

public class LKQuestOutlands extends LKQuestBase
{
	public LKQuestOutlands(int i)
	{
		super(i);
	}
	
	public boolean canStart()
	{
		return LKQuestBase.rafikiQuest.isComplete();
	}
	
	public String[] getRequirements()
	{
		return new String[]{"Complete Rafiki's Quest"};
	}
	
	public int getNumStages()
	{
		return 10;
	}
	
	public ItemStack getIcon()
	{
		return new ItemStack(mod_LionKing.outlandsFeather);
	}
	
	public String getObjectiveByStage(int i)
	{
		switch (i)
		{
			default: return "";
			case 0: return "Use a Rafiki Stick to open the large mound in the Outlands";
			case 1: return "Speak with Zira";
			case 2: return "Bring Zira five kivulite and two silver ingots";
			case 3: return "Throw the ingots into the Outwater";
			case 4: return "Bring Zira three Wayward Feathers";
			case 5: return "Follow the Outlanders back to the Pride Lands";
			case 6: return "Speak to Timon and Pumbaa";
			case 7: return "Obtain the ingredients for Pumbaa";
			case 8: return "Expel the Outlanders from Rafiki's Tree";
			case 9: return "Defeat Zira";
			case 10: return "Quest complete!";
		}
	}
}

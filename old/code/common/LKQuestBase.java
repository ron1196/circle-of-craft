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
import java.nio.ByteBuffer;
import cpw.mods.fml.common.network.*;
import java.util.ArrayList;

public abstract class LKQuestBase
{
	public static LKQuestBase[] allQuests = new LKQuestBase[16];
	
	public static LKQuestBase rafikiQuest = new LKQuestRafiki(0).setName("Rafiki's Quest");
	public static LKQuestBase outlandsQuest = new LKQuestOutlands(1).setName("An Outlandish Scheme");
	
	public int stagesDelayed;
	private String questName;
	public int currentStage;
	public int[] stagesCompleted = new int[getNumStages() + 1];
	public int checked;
	public int questIndex;
	
	public static ArrayList orderedQuests;
	
	static
	{
		orderedQuests = new ArrayList();
		orderedQuests.add(rafikiQuest);
		orderedQuests.add(outlandsQuest);
	}
	
	public LKQuestBase(int i)
	{
		allQuests[i] = this;
		questIndex = i;
	}
	
	public LKQuestBase setName(String name)
	{
		questName = name;
		return this;
	}
	
	public String getName()
	{
		return questName;
	}
	
	public boolean isComplete()
	{
		return currentStage == getNumStages();
	}
	
	public abstract boolean canStart();
	public abstract String[] getRequirements();
	public abstract int getNumStages();
	public abstract ItemStack getIcon();
	public abstract String getObjectiveByStage(int i);
	
	public void progress(int stage)
	{
		if (stage != currentStage + 1)
		{
			return;
		}
		if (currentStage > 0)
		{
			for (int i = 0; i < currentStage; i++)
			{
				if (stagesCompleted[i] == 0)
				{
					return;
				}
			}
		}
		stagesCompleted[currentStage] = 1;
		
		byte[] data = new byte[2];
		data[0] = (byte)questIndex;
		data[1] = (byte)currentStage;
		Packet250CustomPayload packet = new Packet250CustomPayload("lk.questDoStage", data);
		PacketDispatcher.sendPacketToAllPlayers(packet);
		LKLevelData.needsSave = true;
	}
	
	public int getQuestStage()
	{
		return currentStage;
	}
	
	public void setDelayed(boolean flag)
	{
		stagesDelayed = flag ? 1 : 0;
		byte[] data = new byte[2];
		data[0] = (byte)questIndex;
		data[1] = (byte)stagesDelayed;
		Packet250CustomPayload packet = new Packet250CustomPayload("lk.questDelay", data);
		PacketDispatcher.sendPacketToAllPlayers(packet);
		LKLevelData.needsSave = true;
	}
	
	public boolean isDelayed()
	{
		return stagesDelayed == 1;
	}
	
	public boolean isStageComplete(int i)
	{
		return stagesCompleted[i] == 1;
	}
	
	public boolean isChecked()
	{
		return checked == 1;
	}
	
	public void setChecked(boolean flag)
	{
		checked = flag ? 1 : 0;
		byte[] data = new byte[2];
		data[0] = (byte)questIndex;
		data[1] = (byte)checked;
		Packet250CustomPayload packet = new Packet250CustomPayload("lk.questCheck", data);
		PacketDispatcher.sendPacketToAllPlayers(packet);
		LKLevelData.needsSave = true;
	}
	
	public void resetProgress()
	{
		currentStage = 0;
		stagesDelayed = 0;
		for (int j = 0; j < stagesCompleted.length; j++)
		{
			stagesCompleted[j] = 0;
		}
		checked = 0;
	}
	
	public static boolean anyUncheckedQuests()
	{
		for (int i = 0; i < allQuests.length; i++)
		{
			LKQuestBase quest = allQuests[i];
			if (quest == null)
			{
				continue;
			}
			if (quest.canStart() && !quest.isChecked())
			{
				return true;
			}
		}
		return false;
	}
	
	public static void updateAllQuests()
	{
		for (int i = 0; i < allQuests.length; i++)
		{
			LKQuestBase quest = allQuests[i];
			if (quest == null)
			{
				continue;
			}
			if (quest.stagesDelayed == 0 && quest.stagesCompleted[quest.currentStage] == 1 && quest.currentStage < quest.getNumStages())
			{
				quest.currentStage++;
				
				byte[] data = new byte[2];
				data[0] = (byte)quest.questIndex;
				data[1] = (byte)quest.currentStage;
				Packet250CustomPayload packet = new Packet250CustomPayload("lk.questStage", data);
				PacketDispatcher.sendPacketToAllPlayers(packet);
				LKLevelData.needsSave = true;
			}
		}
	}
	
	public static void writeAllQuestsToNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < allQuests.length; i++)
		{
			LKQuestBase quest = allQuests[i];
			if (quest == null)
			{
				continue;
			}
			nbt.setInteger("Quest_" + i + "_Stage", quest.currentStage);
			nbt.setInteger("Quest_" + i + "_Delayed", quest.stagesDelayed);
			for (int j = 0; j < quest.stagesCompleted.length; j++)
			{
				nbt.setInteger("Quest_" + i + "_CompletedStage_" + j, quest.stagesCompleted[j]);
			}
			nbt.setInteger("Quest_" + i + "_Checked", quest.checked);
		}
	}
	
	public static void readAllQuestsFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < allQuests.length; i++)
		{
			LKQuestBase quest = allQuests[i];
			if (quest == null)
			{
				continue;
			}
			quest.currentStage = nbt.getInteger("Quest_" + i + "_Stage");
			quest.stagesDelayed = nbt.getInteger("Quest_" + i + "_Delayed");
			for (int j = 0; j < quest.stagesCompleted.length; j++)
			{
				quest.stagesCompleted[j] = nbt.getInteger("Quest_" + i + "_CompletedStage_" + j);
			}
			quest.checked = nbt.getInteger("Quest_" + i + "_Checked");
		}
	}
}

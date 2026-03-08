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
import net.minecraft.network.*;
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

import cpw.mods.fml.common.network.*;
import net.minecraftforge.common.DimensionManager;
import java.nio.ByteBuffer;
import net.minecraft.src.*;

public class LKPacketHandlerServer implements IPacketHandler
{
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (packet.channel.equals("lk.simbaSit"))
		{
			Entity entity = mod_LionKing.proxy.getEntityFromID(ByteBuffer.wrap(packet.data).getInt(0), DimensionManager.getWorld(packet.data[4]));
			if (entity != null && entity instanceof LKEntitySimba)
			{
				LKEntitySimba simba = (LKEntitySimba)entity;
				simba.setSitting(!simba.isSitting());
			}
		}
		
		else if (packet.channel.equals("lk.damageItem"))
		{
			Entity entity = mod_LionKing.proxy.getEntityFromID(ByteBuffer.wrap(packet.data).getInt(0), DimensionManager.getWorld(packet.data[4]));
			if (entity != null && entity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entity;
				int type = packet.data[5];
				if (type == (byte)0)
				{
					ItemStack boots = entityplayer.inventory.armorItemInSlot(0);
					if (boots != null && boots.getItem() == mod_LionKing.zebraBoots)
					{
						boots.damageItem(1, entityplayer);
						if (boots.getItemDamage() == boots.getMaxDamage())
						{
							LKIngame.sendBreakItemPacket(entityplayer, 0);
							entityplayer.inventory.setInventorySlotContents(36, null);
							entityplayer.addStat(StatList.objectBreakStats[mod_LionKing.zebraBoots.itemID], 1);
						}
					}
				}
				else if (type == (byte)1)
				{
					ItemStack body = entityplayer.inventory.armorItemInSlot(2);
					if (body != null && body.getItem() == mod_LionKing.wings)
					{
						body.damageItem(1, entityplayer);
						if (body.getItemDamage() == body.getMaxDamage())
						{
							LKIngame.sendBreakItemPacket(entityplayer, 1);
							entityplayer.inventory.setInventorySlotContents(38, null);
							entityplayer.addStat(StatList.objectBreakStats[mod_LionKing.wings.itemID], 1);
						}
					}
				}
			}
		}
		
		else if (packet.channel.equals("lk.sendQCheck"))
		{
			LKQuestBase quest = LKQuestBase.allQuests[packet.data[0]];
			quest.setChecked(true);
		}
	}
}

package lionking.common;

import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.packet.*;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.*;

public class LKConnectionHandler implements IConnectionHandler
{
	@Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
	{
		Packet250CustomPayload packet = LKLevelData.getLoginPacket((EntityPlayer)player);
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}

	@Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
	{
		return "";
	}

	@Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}

	@Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

	@Override
    public void connectionClosed(INetworkManager manager) {}

	@Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {}
}

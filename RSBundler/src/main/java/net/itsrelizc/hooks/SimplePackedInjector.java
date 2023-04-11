package net.itsrelizc.hooks;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

import io.netty.channel.Channel;

public class SimplePackedInjector {

	private Field EntityPlayer_playerConnection;
	private Class<?> PlayerConnection;
	private Field PlayerConnection_networkManager;

	private Class<?> NetworkManager;
	private Field k;
	private Field m;

	public SimplePackedInjector() {
		try {
			EntityPlayer_playerConnection = SimpleReflection.getField(SimpleReflection.getClass("{nms}.EntityPlayer"),
					"playerConnection");

			PlayerConnection = SimpleReflection.getClass("{nms}.PlayerConnection");
			PlayerConnection_networkManager = SimpleReflection.getField(PlayerConnection, "networkManager");

			NetworkManager = SimpleReflection.getClass("{nms}.NetworkManager");
			k = SimpleReflection.getField(NetworkManager, "channel");
			m = SimpleReflection.getField(NetworkManager, "m");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void addPlayer(Player p) {
		try {
			Channel ch = getChannel(getNetworkManager(SimpleReflection.getNmsPlayer(p)));
			if (ch.pipeline().get("PacketInjector") == null) {
				SimplePacketHandler h = new SimplePacketHandler(p);
				ch.pipeline().addBefore("packet_handler", "PacketInjector", h);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void removePlayer(Player p) {
		try {
			Channel ch = getChannel(getNetworkManager(SimpleReflection.getNmsPlayer(p)));
			if (ch.pipeline().get("PacketInjector") != null) {
				ch.pipeline().remove("PacketInjector");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private Object getNetworkManager(Object ep) {
		return SimpleReflection.getFieldValue(PlayerConnection_networkManager, SimpleReflection.getFieldValue(EntityPlayer_playerConnection, ep));
	}

	private Channel getChannel(Object networkManager) {
		Channel ch = null;
		try {
			ch = SimpleReflection.getFieldValue(k, networkManager);
		} catch (Exception e) {
			ch = SimpleReflection.getFieldValue(m, networkManager);
		}
		return ch;
	}
}

package net.itsrelizc.npc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.World;

public class NMSHologram extends EntityArmorStand implements Listener {
	
	public Player player;
	public boolean visible;
	public boolean v;
	public int task;
	public Location loc;
	
	public NMSHologram register(Location location, Player player) {
		NMSHologram holo = new NMSHologram((World) location.getWorld(), location.getX(), location.getY(), location.getZ());
		
		World nmsWorld = (World) location.getWorld();
		
		holo.player = player;
		holo.visible = false;
		holo.v = false;
		holo.loc = location;
		
		nmsWorld.addEntity(holo);
		
		return holo;
	}
	
	@SuppressWarnings("deprecation")
	public void startTickingWatcher() {
		final NMSHologram s = this;
		
		this.task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

			@Override
			public void run() {
				
				if (s.player.getLocation().getWorld() != s.loc.getWorld()) {
					s.v = false;
					return;
				}
				
				double distance = s.loc.distance(s.player.getLocation());
				
				if (distance > 48) {
					s.v = false;
				} else {
					if (!s.v) {
						s.sendSpawnPacket(player);
					}
					s.v = true;
				}
				
			}
			
		}, 0L, 2L);
	}
	
	public void sendSpawnPacket(Player player) {
		PacketPlayOutSpawnEntity a = new PacketPlayOutSpawnEntity(this, 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(a);
	}

	public NMSHologram(World world, double d0, double d1, double d2) {
		super(world, d0, d1, d2);
	}
	
	public void _a(PlayerJoinEvent event) {
		
	}
	
	public void _b(PlayerQuitEvent event) {
		
	}

}

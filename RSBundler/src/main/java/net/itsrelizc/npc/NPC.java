package net.itsrelizc.npc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.Rank;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NPC extends EntityPlayer implements Listener {
	
	public static Collection<NPC> npclist = new HashSet<NPC>();
	
	public Location defaul;
	public Location eyeloc;
	public ScoreboardTeam nmsteam;
	public List<Player> visible;
	
	@SuppressWarnings("deprecation")
	public static NPC register(World world, Location location) {
		
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), "B" + RandomStringUtils.randomAlphanumeric(11));
		
		profile.getProperties().clear();
		
		String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3OTE0MDc5MDY2NCwKICAicHJvZmlsZUlkIiA6ICJiMTM1MDRmMjMxOGI0OWNjYWFkZDcyYWVhYmMyNTQ1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUeXBrZW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmU0NTRkYTA4ZmJjMWZkOGQwODliMDVjMzhkNTM2NGE1YmI1NTIwNjdmMmZkNzhiZmNmYTkwMTQ0NzM2MjNkMSIKICAgIH0KICB9Cn0=";
		String sign = "jqQ0VQjdVh6gVvyQgYAkogkZlSblGYZSqss96aoTssOm/BaJCrAgBOjDHs3dx9ygSWIVSpeERzAZXEM1IUXtf0EFwWCodrU3o7mNNtjaptFnUnl/MKRUshRcLrRhLgZSQg+N1/gyfaJbbfrvwgyzwIjJGDPC/quF92Q/nkCHB1qAKc3l/KLPU4yGdmTR41R8y2AKFG9mjWV/xrHlm7vQbvqTjAQ1I/SeenH3lJBWpz+OPPwz9a0kN4h8iN8Ej416Kqb5mJwuyAVSgPsgMVEIy0skFGamtBCHZ6lx/hTGoDjGV+lNQJygIWu0ymWNHecL7IQNmYnUOYnphtles9pDYgVtPMv4pz5UBJ7lThtTgPAwkmdgbv9OO8Y3ylNsAej2LItZ/nRLUjhVo1PclzybJHrFM+gLO/h0RjztlQJEROHtDMj2R37wEMlQC8K8wrWMLN2Fg6wlLENWKYC3gMaMprXP9zgjHyldmv81fpWYdPdkILuiJgMudqhnAgMnIyOOr2FHUv2b3gTLeU5n39kRQNlg1PyEnlcn8HfalzURH10wTTYjt0rQjK7Q2+5fqKQpFUOxfg82S5xicndlKJh3ZRsrb7YNiK/ASAWn4XfByY3iKmEzQ/QP/0LL3hoUXVKH9e0wMEZsARELT4juGG6wAtpK+UsxFLp0msnr70Wf/h4=";
		
		profile.getProperties().put("textures", new Property("textures", texture, sign));
		
		PlayerInteractManager interactManager = new PlayerInteractManager(nmsWorld);
		
		NPC entityPlayer = new NPC(nmsServer, nmsWorld, profile, interactManager);
	    entityPlayer.playerConnection = new PlayerConnection(nmsServer, new NMSNetworkManager(EnumProtocolDirection.CLIENTBOUND), entityPlayer);
	    
	    entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	    
	    nmsWorld.addEntity(entityPlayer);
	    
	    entityPlayer.visible = new ArrayList<Player>();
	    
	    PacketPlayOutPlayerInfo a = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
	    PacketPlayOutNamedEntitySpawn b = new PacketPlayOutNamedEntitySpawn(entityPlayer);
	    PacketPlayOutEntityHeadRotation c = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((location.add(0, 1.62, 0).getYaw() * 256f) / 360f));
	    // PacketPlayOutPlayerInfo d = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
	    
	    for (Player player : Bukkit.getOnlinePlayers()) {
	    	PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
	    	con.sendPacket(a);
	    	con.sendPacket(b);
	    	con.sendPacket(c);
	    }
	    
	    npclist.add(entityPlayer);
	    
	    entityPlayer.defaul = location;
	    entityPlayer.eyeloc = location.clone().add(0, 1.92, 0);
	    
	    Bukkit.getPluginManager().registerEvents(entityPlayer, Bukkit.getPluginManager().getPlugin("RSBundler"));
	    
	    entityPlayer.getBukkitEntity().setPlayerListName("§8[NPC] " + entityPlayer.displayName);
	    
	    Grouping.rankedTeam.get(Rank.NPC).setNameTagVisibility(NameTagVisibility.NEVER);
	    Grouping.rankedTeam.get(Rank.NPC).addEntry(entityPlayer.getName());
	    
//	    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
//	    ScoreboardTeam team = new ScoreboardTeam((net.minecraft.server.v1_8_R3.Scoreboard) scoreboard, "NPC_" + entityPlayer.displayName);
//	    team.setNameTagVisibility(EnumNameTagVisibility.NEVER);
	    
//	    entityPlayer.nmsteam = team;
	    
	    final NPC s = entityPlayer;
	    
	    Bukkit.getScheduler().scheduleAsyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

			@Override
			public void run() {
				List<Entity> near = s.getBukkitEntity().getNearbyEntities(49d, 49d, 49d);
				List<Player> ok = new ArrayList<Player>();
				
				for (Entity e : near) {
					if (e instanceof Player) {
						Player player = (Player) e;
						
						if (!s.visible.contains(player)) {
							s.visible.add(player);
							
							PacketPlayOutPlayerInfo a = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, s);
							((CraftPlayer) player).getHandle().playerConnection.sendPacket(a);
							
							PacketPlayOutNamedEntitySpawn b = new PacketPlayOutNamedEntitySpawn(s);
						    PacketPlayOutEntityHeadRotation c = new PacketPlayOutEntityHeadRotation(s, (byte) ((s.eyeloc.getYaw() * 256f) / 360f));
						    
						    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(b);
						    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(c);
						    
						    Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

								@Override
								public void run() {
									PacketPlayOutPlayerInfo a = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, s);
									((CraftPlayer) player).getHandle().playerConnection.sendPacket(a);
								}
						    	
						    }, 10L);
						}
						
						if (s.visible.contains(player)) {
							ok.add(player);
						}
						
						
					}
				}
				
				for (Player p : new ArrayList<Player>(s.visible)) {
					if (!ok.contains(p)) {
						s.visible.remove(p);
					}
				}
				
				ok.clear();
			}
	    	
	    }, 0L, 2L);
	    
	    return entityPlayer;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void _a(PlayerJoinEvent event) {
		
		//this.spawn(event.getPlayer());
	    
	    
	    
	    
	    //Grouping.rankedTeam.get(Rank.NPC).addEntry(event.getPlayer().getName());
//	    
//	    PacketPlayOutScoreboardTeam d = new PacketPlayOutScoreboardTeam(this.nmsteam, 0);
//	    
//	    List<String> playerToAdd = new ArrayList<String>();
//        playerToAdd.add(this.getName());
//        
//        PacketPlayOutScoreboardTeam e = new PacketPlayOutScoreboardTeam(this.nmsteam, playerToAdd, 3);
//        
//        ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(d);
//	    ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(e);
	}
	
	@EventHandler
	public void _b(EntityDamageEvent event) {
		if (event.getEntity().getUniqueId().toString().equalsIgnoreCase(this.getUniqueID().toString())) { 
			event.setCancelled(true);
		}
	}
	
	public double calculateLookYaw(Location a) {
		
		double angle = getAngle(new Vector(this.defaul.getX(), 0, this.defaul.getZ()), a.toVector());
		if (angle < 0) {
            angle += 360.0F;
        }
		return angle;
	}
	
	private final float getAngle(Vector point1, Vector point2) {
        double dx = point2.getX() - point1.getX();
        double dz = point2.getZ() - point1.getZ();
        float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
        return angle;
    }
	
	public double calculateLookPitch(Location a) {
		
		if (a.getY() == 0) return 0d;
		
		double finalX = this.distance2dX(this.defaul, a);
		double finalY = a.getY() - this.defaul.getY();
		
		double angle = getAngle(new Vector(a.toVector().getX() - finalX, 0, a.toVector().getY() - finalY), new Vector(a.toVector().getX(), 0, a.toVector().getY()));
		
		return 90 - (angle + 180);
	}
	
	public double distance3d(Location a, Location b) {
		return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2) + Math.pow(b.getZ() - a.getZ(), 2));
	}
	
	public double distance2dX(Location a, Location b) {
		return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getZ() - a.getZ(), 2));
	}

	@SuppressWarnings("deprecation")
	public NPC(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
		
		final NPC s = this;
		
		this._tickLookAtNearest();
	}
	
	public void _tickLookAtNearest() {
		
		
		if (this.defaul != null) {
			List<Entity> near = this.getBukkitEntity().getNearbyEntities(5d, 5d, 5d);
			
			double mindist = 999999d;
			Player mindude = null;
			
			for (Entity e : near) {
				if (e instanceof Player) {
					Player player = (Player) e;
					
					double dist = player.getLocation().add(0, 1.62, 0).distance(new Location(player.getLocation().getWorld(), this.locX, this.locY, this.locZ).add(0, 1.62, 0));
					if (dist < mindist) {
						mindist = dist;
						mindude = player;
					}
				}
			}
			
			if (mindude == null) {
				PacketPlayOutEntityHeadRotation d = new PacketPlayOutEntityHeadRotation(this, (byte) ((this.defaul.getYaw() * 256f) / 360f)); // set head yaw
		        PacketPlayOutEntityLook nc = new PacketPlayOutEntityLook(this.getId(), (byte) ((this.defaul.getYaw() * 256f) / 360f), (byte) ((this.defaul.getPitch() * 256f) / 360f), true); // set head pitch and body yaw
		        
		        for (Player player : Bukkit.getOnlinePlayers()) {
		        	((CraftPlayer) player).getHandle().playerConnection.sendPacket(nc);
		        	((CraftPlayer) player).getHandle().playerConnection.sendPacket(d);
		        }
			} else {
				
				this.yaw = (float) this.calculateLookYaw(mindude.getEyeLocation());
				this.pitch = (float) this.calculateLookPitch(mindude.getEyeLocation());

		        PacketPlayOutEntityHeadRotation d = new PacketPlayOutEntityHeadRotation(this, (byte) ((this.yaw * 256f) / 360f)); // set head yaw
		        PacketPlayOutEntityLook nc = new PacketPlayOutEntityLook(this.getId(), (byte) ((this.yaw * 256f) / 360f), (byte) ((this.pitch * 256f) / 360f), true); // set head pitch and body yaw
		         
		        for (Player player : Bukkit.getOnlinePlayers()) {
		        	((CraftPlayer) player).getHandle().playerConnection.sendPacket(nc);
		        	((CraftPlayer) player).getHandle().playerConnection.sendPacket(d);
		        }
			}
		}
		
		final NPC s = this;
		
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

			@Override
			public void run() {
				s._tickLookAtNearest();
			}
		}, new Random().nextLong(8));
	}


	public static String r(int l) {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = l;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();

	    return generatedString;
	}
	

	@Override
	public boolean isSpectator() {
		// TODO Auto-generated method stub
		return false;
	}

}

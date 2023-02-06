package net.itsrelizc.npc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import net.itsrelizc.global.MuteUtils;

/**
 * 
 * @author Relizc
 *
 */
public class NPC implements BaseNPC {
	
	private Object packetEntityId;
	private String skinUUID;
	private String displayName;
	private int entityId;
	private Boolean useSignData;
	
	/**
	 * Creates a simple NPC class.
	 */
	public NPC() {
		this.packetEntityId = null;
		this.entityId = new Random().nextInt(Integer.MAX_VALUE);
	}
	
	@Deprecated
	public NPC(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public void setDisplayName(String name) {
		this.displayName = name;
	}
	
	@Override
	public void setUseEntityBlockPositionSignMetadata(Boolean value) {
		this.useSignData = value;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setSkinName(String name) throws IOException {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

			@Override
			public void run() {
				URL url = null;
				try {
					url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpURLConnection con = null;
				try {
					con = (HttpURLConnection) url.openConnection();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					con.setRequestMethod("GET");
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					con.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int status = 0;
				try {
					status = con.getResponseCode();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (status == 200) {
					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String inputLine = null;
					StringBuffer content = new StringBuffer();
					try {
						while ((inputLine = in.readLine()) != null) {
							content.append(inputLine);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					con.disconnect();
					
					Bukkit.broadcastMessage(content.toString());
				}
			}}, 0L);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setSkinThenSpawn(String skinName, final Location loc) {
		
		final NPC self = this;
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

			@Override
			public void run() {
				URL url = null;
				try {
					url = new URL("https://api.mojang.com/users/profiles/minecraft/" + skinName);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpURLConnection con = null;
				try {
					con = (HttpURLConnection) url.openConnection();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					con.setRequestMethod("GET");
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					con.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int status = 0;
				try {
					status = con.getResponseCode();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (status == 200) {
					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String inputLine = null;
					StringBuffer content = new StringBuffer();
					try {
						while ((inputLine = in.readLine()) != null) {
							content.append(inputLine);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					con.disconnect();
					
					JSONParser parser = new JSONParser();
					JSONObject data = null;
					try {
						data = (JSONObject) parser.parse(content.toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					self.skinUUID = (String) data.get("id");
					self.spawn(loc);
				}
			}}, 0L);
	}
	
	@Override
	public void spawn(Location loc) {
//		WrapperPlayServerPlayerInfo wrap = new WrapperPlayServerPlayerInfo();
//		wrap.setAction(PlayerInfoAction.ADD_PLAYER);
//		
//		List<PlayerInfoData> infos = new ArrayList<PlayerInfoData>();
//		infos.add(new PlayerInfoData(new WrappedGameProfile(skinUUID.replaceFirst( 
//		        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5" 
//			    ), displayName), 0, NativeGameMode.SURVIVAL, WrappedChatComponent.fromLegacyText("§8[NPC] " + MuteUtils.generateRandomID().toLowerCase())));
//		wrap.setData(infos);
//		
//		WrapperPlayServerNamedEntitySpawn entity = new WrapperPlayServerNamedEntitySpawn();
//		entity.setEntityID(this.entityId);
//		entity.setPlayerUUID(UUID.fromString(skinUUID.replaceFirst( 
//		        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5" 
//			    )));
//		entity.setPosition(loc.toVector());
//		entity.setYaw(loc.getYaw());
//		entity.setPitch(loc.getPitch());
//		
//		WrappedDataWatcher watcher = new WrappedDataWatcher();
//	    watcher.setObject(0, (byte) 0x20);
//	    watcher.setObject(6, (float) 0.5);
//	    watcher.setObject(11, (byte) 1);
//	    entity.setMetadata(watcher);
//	    
//	    for (Player player : Bukkit.getOnlinePlayers()) {
//			wrap.sendPacket(player);
//			entity.sendPacket(player);
//		}
	    
	    PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

        // add player in player list for player
        connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this));
        // make player spawn in world
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        // change head rotation
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((loc.getYaw() * 256f) / 360f)));
        // now remove player from tab list
        connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this));
        // here the entity is showed, you can show item in hand like that :
        // connection.sendPacket(new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(itemInHand)));
	}

	@Override
	public void despawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLookAtNearestPlayer(Boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLookAtNearestPlayer(Boolean value, Integer radius) {
		// TODO Auto-generated method stub
		
	}
}

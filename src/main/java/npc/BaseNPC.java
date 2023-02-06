package npc;

import java.io.IOException;

import org.bukkit.Location;

public interface BaseNPC {
	
	/**
	 * Sets the skin of the NPC by a player's Minecraft user name.
	 * @param name The name of the player skin.
	 * @throws IOException ...
	 */
	public void setSkinName(String name) throws IOException;
	
	public void setDisplayName(String name);
	
	public void setUseEntityBlockPositionSignMetadata(Boolean value);
	
	public void spawn(Location loc);
	
	public void despawn();
	
	public void setLookAtNearestPlayer(Boolean value);
	
	public void setLookAtNearestPlayer(Boolean value, Integer radius);

	public void setSkinThenSpawn(String skinName, Location loc);
	
}

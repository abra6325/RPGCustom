package net.itsrelizc.global;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.itsrelizc.commands.tests.SpawnProtocolPlayer;

public class RawMain extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable() {
		getLogger().info("SFN Global Library had been sucessfully installed!");
		
		Bukkit.getPluginCommand("npc").setExecutor(new SpawnProtocolPlayer());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("libraryversion")) {
			String version = this.getDescription().getVersion();
			String message = "§aThis server is running on a ";
			String msg = "§b" + version.split("-")[1] + "§a ";
			if (version.contains("alpha")) {
				message += msg + "§c§lALPHA TEST VERSION§r§a";
			} else if (version.contains("alpha")) {
				message += msg + "§e§lBETA RELEASE VERSION§r§a";
			} else if (version.contains("stable")) {
				message += msg + "§a§lSTABLE VERSION§r§a";
			} else if (version.contains("final")) {
				message += msg + "§b§lFINAL VERSION§r§a";
			}
			message += " of SFNLibrary modded by Salty Fish Network Developers.";
			sender.sendMessage(message);
			return true;
		}
		return false;
	}
	
}

package net.itsrelizc.commands.tests;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.npc.NPC;

public class SpawnProtocolPlayer implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		NPC npc = new NPC();
		npc.setDisplayName("Nether_Blazes");
		npc.setSkinThenSpawn("Nether_Blazes", ((Player) sender).getLocation());
		return true;
	}

}

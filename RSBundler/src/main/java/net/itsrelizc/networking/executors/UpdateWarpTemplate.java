package net.itsrelizc.networking.executors;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.global.Me;
import net.itsrelizc.global.RawMain;
import net.itsrelizc.networking.CommunicationInput;

public class UpdateWarpTemplate implements Runnable {
	
	private CommunicationInput input;

	public UpdateWarpTemplate(CommunicationInput input) {
		this.input = input;
	}

	@Override
	public void run() {
		Player player = Bukkit.getPlayer(this.input.readString());
		Inventory inv = player.getOpenInventory().getTopInventory();
		short item = (short) this.input.readShort();
		int slot = 10;
		for (int i = 0; i < item; i ++) {
			String n = this.input.readString();
			String p = this.input.readString();
			short players = (short) this.input.readShort();
			short mplayers = (short) this.input.readShort();
			String typ = this.input.readString();
			
			ItemStack it = null;
			if (typ.equalsIgnoreCase("verify")) {
				it = new ItemStack(Material.GREEN_RECORD);
			}
			
			ItemMeta im = it.getItemMeta();
			im.setDisplayName("§7[RS-§e§l" + n + "§r§7] §a" + typ);
			
			im.addItemFlags(ItemFlag.values());
			
			List<String> li = new ArrayList<String>();
			li.add("§eName: §b" + p);
			li.add("§ePlayers: §b" + players + "§7/§b" + mplayers);
			li.add(" ");
			if (Me.getStaticCode().equalsIgnoreCase(n)) {
				li.add("§cYou are already connected to this server!");
			} else {
				li.add("§eClick to connect to this server!");
			}
			im.setLore(li);
			
			it.setItemMeta(im);
			
			inv.setItem(slot, it);
			
			if (slot == 43) break;
			
			if ((slot + 2) % 9 == 0) {
				slot += 2;
			}
			
			slot ++;
		}
	}

}

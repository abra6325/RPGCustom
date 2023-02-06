package net.itsrelizc.global;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TPSUtils {
	
	private static Double currenttps = 20.00;
	private static Long lastms = System.currentTimeMillis();
	private static Double lastgaps = 1.00;
	
	private static Long lastminms = System.currentTimeMillis();
	private static Integer violations = 0;
	
	private static Double expectedminTPS = 10.00;
	
	public static void startRecording() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Global.getPlugin(), new Runnable() {

			@Override
			public void run() {
				Long thisms = System.currentTimeMillis();
				Float gapms = (float) (thisms - lastms);
				Float gaps = (float) (gapms / 1000.00);
				if (gaps < 0.5) {
					return;
				}
				currenttps = (double) (20 / gaps);
				lastms = System.currentTimeMillis();
			}
			
		}, 0L, 20L);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Global.getPlugin(), new Runnable() {

			@Override
			public void run() {
				Long gap = System.currentTimeMillis() - lastminms;
				Float gaps = (float) (gap / 1000.00);
				Double currenttps = (double) (300 / gaps);
				if (currenttps < expectedminTPS	) {
					DecimalFormat df = new DecimalFormat("0.00");
					Float current = Float.valueOf(df.format(currenttps));
					violations++;
					ChatUtils.broadcastSystemMessage("§2§lSERVER TPS", "§6Server TPS too low! Minimum TPS of §e" + expectedminTPS + "§6 was expected, but the current TPS is §e" + currenttps + "§6.");
					if (violations > 5) {
						ChatUtils.broadcastSystemMessage("§2§lSERVER TPS", "§6Server TPS is way too low! Closing server!");
						Bukkit.getScheduler().scheduleSyncDelayedTask(Global.getPlugin(), new Runnable() {

							@Override
							public void run() {
								Bukkit.shutdown();
							}
							
						}, 5L);
					}
				}
				lastminms = System.currentTimeMillis();
			}
			
		}, 0L, 300L);
	}
	
	public static Float getTPS() {
		return currenttps.floatValue();
	}
}

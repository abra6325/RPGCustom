package com.lifestealrpg.rpgcustoms;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;

public final class RPGCustoms extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Plugin RPGCustoms has started["+ System.currentTimeMillis()+"]");

        getServer().getPluginManager().registerEvents(new EventListeners(),this);
    }
}

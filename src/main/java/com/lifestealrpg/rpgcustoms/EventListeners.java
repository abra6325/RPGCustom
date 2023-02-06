package com.lifestealrpg.rpgcustoms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.EventListener;

public class EventListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        e.setJoinMessage("Welcome to the RPG, "+e.getPlayer().getDisplayName());

    }
}

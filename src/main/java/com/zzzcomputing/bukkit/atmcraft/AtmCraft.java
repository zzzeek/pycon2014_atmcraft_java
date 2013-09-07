package com.zzzcomputing.bukkit.atmcraft;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AtmCraft extends JavaPlugin implements Listener{

	
    private FileConfiguration config;

	@Override
    public void onEnable() {

        config = getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);        
        Logger.info("AtmCraft online hooray !");
    }
	
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Util.sendPlayerMessage(event.getPlayer(), "AtmCraft enabled.");
    }
	
}

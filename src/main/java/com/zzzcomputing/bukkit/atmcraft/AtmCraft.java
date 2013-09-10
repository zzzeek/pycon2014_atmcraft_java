package com.zzzcomputing.bukkit.atmcraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AtmCraft extends JavaPlugin implements Listener{

	
    protected FileConfiguration config;

	@Override
    public void onEnable() {

        config = getConfig();
        saveConfig();
        Bukkit.getPluginManager().registerEvents(this, this);        
        Logger.info("AtmCraft online hooray !");
    }
	
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Util.sendPlayerMessage(event.getPlayer(), "AtmCraft enabled.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception {
    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		Block block = event.getClickedBlock();
    		if (isAtmBlock(block)) {
                Util.sendPlayerMessage(event.getPlayer(), "seems like an ATM!");
                AtmSession atmSession;
                try {
                    atmSession = new AtmSession(this, event.getPlayer());
                }
                catch (Exception e) {
                    Util.sendPlayerMessage(event.getPlayer(), "Exception on login: " + e.toString());
                    throw e;
                }
                atmSession.display();
    		}
    	}
    }
    
    private boolean isAtmBlock(Block block) {
    	return (
    			block != null &&
    			block.getType() == Material.CHEST &&
    			getBlockTypeAbove(block, 1) == Material.REDSTONE_LAMP_ON &&
    			getBlockTypeAbove(block, -1) == Material.QUARTZ_BLOCK
    	);
    }
    
    private Material getBlockTypeAbove(Block block, int offset) {
    	Block aboveBlock = block.getRelative(0, offset, 0);
    	if (aboveBlock != null) {
    		return aboveBlock.getType();
    	}
    	else {
    		return null;
    	}
    }
}
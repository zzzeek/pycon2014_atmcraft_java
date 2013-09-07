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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		Block block = event.getClickedBlock();
    		if (isAtmBlock(block)) {
                Util.sendPlayerMessage(event.getPlayer(), "seems like an ATM!");
                AtmSession atmSession = new AtmSession(event.getPlayer());
                atmSession.display();
    		}
    	}
    }
    
    private boolean isAtmBlock(Block block) {
    	if (block != null) {
        	Logger.info("blocktype is chest: " + (block.getType() == Material.CHEST));
        	Logger.info("above block: " + getBlockTypeAbove(block, 1));
        	Logger.info("below block: " + getBlockTypeAbove(block, -1));
    	}
    	
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
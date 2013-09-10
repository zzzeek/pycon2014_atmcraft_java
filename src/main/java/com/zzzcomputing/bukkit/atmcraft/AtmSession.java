package com.zzzcomputing.bukkit.atmcraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AtmSession implements InventoryHolder {
	private Inventory inventory;
	private Player player;
	private AtmCraft parent;

	public AtmSession(AtmCraft parent, Player player) {
		this.player = player;
		this.parent = parent;
	}

	private void authenticate() {
		
	}
	public String getBankName() {
		return this.parent.config.getString("bankname");
	}
	
	@Override
	public Inventory getInventory() {
		if (inventory == null) {
			inventory = Bukkit.createInventory(this, 18, getBankName() + " - Welcome " + player.getDisplayName() + "!");
		}
		return inventory;
	}
	
	public void display() {
        if (getInventory().getViewers().contains(player)) {
            throw new IllegalArgumentException(player.getName() + " is already viewing " + getInventory().getTitle());
        }
        player.openInventory(getInventory());
	}
	
	
}




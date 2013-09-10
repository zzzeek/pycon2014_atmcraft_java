package com.zzzcomputing.bukkit.atmcraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.json.JSONObject;

public class AtmSession implements InventoryHolder {
	private Inventory inventory;
	private Player player;
	private AtmCraft parent;
	private String authToken;
	
	public AtmSession(AtmCraft parent, Player player) throws Exception {
		this.player = player;
		this.parent = parent;
		authenticate();
	}
	
	private void authenticate() throws Exception {
		String urlParameters = "account_name=" + this.player.getName();
		urlParameters += "&identifier=" + getServiceIdentifier();
		urlParameters += "&secret=" + getServiceSecret();
		String url = getServiceURL() + "/login";
		JSONObject result = Util.postToJson(url, urlParameters);
		authToken = result.getString("auth_token");
	}
	
	private String getServiceURL() {
		return this.parent.config.getString("serverurl");
	}
	private String getServiceIdentifier() {
		return this.parent.config.getString("identifier");
	}
	private String getServiceSecret() {
		return this.parent.config.getString("secret");
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




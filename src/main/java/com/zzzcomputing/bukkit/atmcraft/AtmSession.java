package com.zzzcomputing.bukkit.atmcraft;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

public class AtmSession implements InventoryHolder {
	private Inventory inventory;
	private Player player;
	private AtmCraft parent;
	private String authToken;
	private HashMap<Material,Integer> stackAmounts;
	
	public AtmSession(AtmCraft parent, Player player) throws Exception {
		this.player = player;
		this.parent = parent;
		authenticate();
		populateBalance();
	}
	
	public Player getPlayer() {
		return player;
	}
	private void authenticate() throws Exception {
		String urlParameters = "account_name=" + this.player.getName();
		urlParameters += "&identifier=" + getServiceIdentifier();
		urlParameters += "&secret=" + getServiceSecret();
		String url = getServiceURL() + "/login";
		JSONObject result = Util.postToJson(url, urlParameters);
		authToken = result.getString("auth_token");
		Util.sendPlayerMessage(player, "Authenticated as " + player.getName());
	}
	
	private void populateBalance() throws Exception {
		String urlParameters = "auth_token=" + authToken;
		String url = getServiceURL() + "/balance";
		stackAmounts = new HashMap<Material,Integer>();
		JSONObject result = Util.getToJson(url, urlParameters);
		JSONArray names = result.names();
		if (names == null) {
			return;
		}
		for (int i=0; i<names.length(); i++) {
			String name = names.getString(i);
			int balance = new Double(result.getDouble(name)).intValue();
			if (balance == 0) {
				continue;
			}
			Material material;
			try {
				material = Material.valueOf(name);
			}
			catch (IllegalArgumentException iae) {
				Util.sendPlayerMessage(player, "Don't recognize item type: " + name);
				continue;
			}
			getInventory().addItem(new ItemStack(material, balance));
			if (stackAmounts.containsKey(material)) {
				stackAmounts.put(material, stackAmounts.get(material).intValue() + balance);
			}
			else {
				stackAmounts.put(material, balance);
			}
		}
	}

	private HashMap<Material,Integer> addUpItems() {
		HashMap<Material,Integer> amounts = new HashMap<Material,Integer>();
		for (ItemStack stack: getInventory()) {
			if (stack == null) {
				continue;
			}
			Material material = stack.getType();
			int amount = stack.getAmount();
			if (amounts.containsKey(material)) {
				amounts.put(material, amounts.get(material).intValue() + amount);
			}
			else {
				amounts.put(material, amount);
			}
		}
		return amounts;
	}
	
	protected void inventoryChanged() {
		HashMap<Material,Integer> amounts = addUpItems();
		for (Material clicked: amounts.keySet()) {
			if (stackAmounts.containsKey(clicked)) {
				int value = stackAmounts.remove(clicked);
				if (value < amounts.get(clicked)) {
					// deposit
					deposit(clicked, amounts.get(clicked) - value);
				}
				else if (value > amounts.get(clicked)) {
					// withdrawal
					withdraw(clicked, value - amounts.get(clicked));
				}
			}
			else {
				// deposit
				deposit(clicked, amounts.get(clicked));
			}
		}
		for (Material remaining: stackAmounts.keySet()) {
			int value = stackAmounts.get(remaining);
			// withdrawal
			withdraw(remaining, value);
		}
		// replace
		stackAmounts = amounts;
	}
	
	private void deposit(Material material, int amount) {
		withdraw_or_deposit(material, amount, false);
	}
	
	private void withdraw(Material material, int amount) {
		withdraw_or_deposit(material, amount, true);
	}
	
	private void withdraw_or_deposit(Material material, int amount, boolean withdraw) {
		String verb;
		if (withdraw) {
			verb = "Withdraw";
		}
		else {
			verb = "Deposit";
		}
		Logger.info(verb + " " + String.valueOf(amount) + " of " + material.toString());

		String urlParameters = "auth_token=" + authToken;
		urlParameters += "&type=" + String.valueOf(material);
		urlParameters += "&amount=" + String.valueOf(amount);
		String url = getServiceURL() + (withdraw ? "/withdraw" : "/deposit");
		try {
			Util.postToJson(url, urlParameters);
			Util.sendPlayerMessage(player, verb + " " + String.valueOf(amount) + " of " + material.toString());
		}
		catch (Exception e) {
			Util.sendPlayerMessage(player, verb + " failed: " + e);
			e.printStackTrace();
		}
		
		
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




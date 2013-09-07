package com.zzzcomputing.bukkit.atmcraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {

	public static String [] slice(String [] src, int start) {
		String [] dest = new String[src.length - start];
		System.arraycopy(src, start, dest, 0, dest.length);
		return dest;
	}

	public static String [] sliceEnd(String [] src, int end) {
		String [] dest = new String[src.length - end];
		System.arraycopy(src, 0, dest, 0, dest.length);
		return dest;
	}

	public static void sendPlayerMessage(Player player, Object ... tokens) {
		String msg = ChatColor.DARK_PURPLE + "[AC] " + ChatColor.AQUA;
		for (Object token: tokens) {
			msg = msg.concat(token.toString());
		}
		player.sendMessage(msg);
	}

}

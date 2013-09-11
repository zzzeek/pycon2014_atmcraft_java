package com.zzzcomputing.bukkit.atmcraft;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class Util {

	public static void sendPlayerMessage(Player player, Object ... tokens) {
		String msg = ChatColor.DARK_PURPLE + "[AC] " + ChatColor.AQUA;
		for (Object token: tokens) {
			msg = msg.concat(token.toString());
		}
		player.sendMessage(msg);
	}

	public static JSONObject getToJson(String urlString, String paramString) throws Exception {
		return httpToJson(urlString, paramString, false);
	}

	public static JSONObject postToJson(String urlString, String paramString) throws Exception {
		return httpToJson(urlString, paramString, true);
	}

	private static JSONObject httpToJson(String urlString, String paramString, boolean post) throws Exception {
		// WTG JDK!   this is very succinct, yup
		URL url;
		if (post) {
			url = new URL(urlString);
		}
		else {
			url = new URL(urlString + "?" + paramString);
		}
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (post) {
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(paramString.getBytes().length));
			connection.setUseCaches (false);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(paramString);
			wr.flush();
			wr.close();
		}
		InputStream is = connection.getInputStream();
		String data = convertStreamToString(is);
		connection.disconnect();
		return new JSONObject(data);
	}
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}


}

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
	
	public static void main(String [] argv) throws Exception {
		String urlParameters = "account_name=zzzeek&identifier=atm1&secret=frogpile";
		String url = "http://localhost:6543/login";
		JSONObject result = postToJson(url, urlParameters);
		String authToken = result.getString("auth_token");
		url = "http://localhost:6543/balance";
		urlParameters = "auth_token=" + authToken;
		result = getToJson(url, urlParameters);
		System.out.println(result);
		
	}
		/*


for (JsonNode userJsonNode : userJsonNodes) {



		 */
	
}

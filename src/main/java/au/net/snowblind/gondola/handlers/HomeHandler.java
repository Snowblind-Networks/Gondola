package au.net.snowblind.gondola.handlers;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class HomeHandler {
	// Get list of homes
	public static Set<String> getHomes(Player p) {
		return Gondola.jedis.smembers("user:" + p.getUniqueId().toString() + ":homes");
	}
	
	// Gets home, or null if not found
	public static Location getHome(Player p, String home) {
		if (!getHomes(p).contains(home)) {
			return null;
		} else {
			return RedisHandler.getLocation("home:" + p.getUniqueId().toString() + ":" + home);
		}
	}
	
	// Sets home
	public static void setHome(Player p, String home) {
		RedisHandler.setLocation("home:" + p.getUniqueId().toString() + ":" + home, p.getLocation());
		Gondola.jedis.sadd("user:" + p.getUniqueId().toString() + ":homes", home);
	}
	
	// Deletes home
	public static void delHome(Player p, String home) {
		Gondola.jedis.del("home:" + p.getUniqueId().toString() + ":" + home);
		Gondola.jedis.srem("user:" + p.getUniqueId().toString() + ":homes", home);
	}
}

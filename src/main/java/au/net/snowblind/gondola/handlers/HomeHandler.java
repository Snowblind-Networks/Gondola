package au.net.snowblind.gondola.handlers;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.PlayerData;

public class HomeHandler {
	// Get list of homes
	public static Set<String> getHomes(Player p) {
		PlayerData pd = new PlayerData(p);
		return pd.playerData.getConfigurationSection("home").getKeys(false);
	}
	
	// Gets home, or null if not found
	public static Location getHome(Player p, String home) {
		PlayerData pd = new PlayerData(p);
		ConfigurationSection cs;
		
		if ((cs = pd.playerData.getConfigurationSection("home." + home)) != null) {
			return new Location(Gondola.plugin.getServer().getWorld(cs.getString("world")), cs.getDouble("x"), cs.getDouble("y"), cs.getDouble("z"));
		} else {
			return null;
		}
	}
	
	// Sets home
	public static void setHome(Player p, String home) {
		PlayerData pd = new PlayerData(p);
		ConfigurationSection cs = pd.playerData.createSection("home." + home);
		
		cs.set("world", p.getWorld().getName());
		cs.set("x", p.getLocation().getX());
		cs.set("y", p.getLocation().getY());
		cs.set("z", p.getLocation().getZ());
	}
}

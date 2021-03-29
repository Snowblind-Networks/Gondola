package au.net.snowblind.gondola.handlers;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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
			return ConfigHandler.getLocation(cs);
		} else {
			return null;
		}
	}
	
	// Sets home
	public static void setHome(Player p, String home) {
		PlayerData pd = new PlayerData(p);
		ConfigurationSection cs = pd.playerData.createSection("home." + home);
		
		ConfigHandler.setLocation(cs, p.getLocation());
		pd.savePlayerData();
	}
	
	// Deletes home
	public static void delHome(Player p, String home) {
		PlayerData pd = new PlayerData(p);
		pd.playerData.set("home." + home, null);;
		pd.savePlayerData();
	}
}

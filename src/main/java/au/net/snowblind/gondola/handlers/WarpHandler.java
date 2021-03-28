package au.net.snowblind.gondola.handlers;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import au.net.snowblind.gondola.Gondola;

public class WarpHandler {
	// Get list of warps
	public static Set<String> getWarps() {
		ConfigurationSection cs;
		if ((cs = Gondola.plugin.getConfig().getConfigurationSection("warps")) != null)
			return cs.getKeys(false);
		else
			return null;
	}
	
	// Gets warp, or null if not found
	public static Location getWarp(String warp) {
		ConfigurationSection cs;
		
		if ((cs = Gondola.plugin.getConfig().getConfigurationSection("warps." + warp)) != null) {
			return ConfigHandler.getLocation(cs);
		} else {
			return null;
		}
	}
	
	// Sets warp
	public static void setWarp(String warp, Location loc) {
		ConfigurationSection cs = Gondola.plugin.getConfig().createSection("warps." + warp);
		
		ConfigHandler.setLocation(cs, loc);
		Gondola.plugin.saveConfig();
	}
	
	// Deletes warp
	public static void delWarp(String warp) {
		Gondola.plugin.getConfig().set("warps." + warp, null);
		Gondola.plugin.saveConfig();
	}
}

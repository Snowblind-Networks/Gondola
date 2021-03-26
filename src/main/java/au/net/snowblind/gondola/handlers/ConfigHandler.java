package au.net.snowblind.gondola.handlers;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import au.net.snowblind.gondola.Gondola;

public class ConfigHandler {
	public static Location getLocation(ConfigurationSection cs) {
		return new Location(Gondola.plugin.getServer().getWorld(cs.getString("world")),
				cs.getDouble("x"), cs.getDouble("y"), cs.getDouble("z"),
				(float) cs.getDouble("yaw"), (float) cs.getDouble("pitch"));
	}
	
	public static void setLocation(ConfigurationSection cs, Location loc) {
		cs.set("world", loc.getWorld().getName());
		cs.set("x", loc.getX());
		cs.set("y", loc.getY());
		cs.set("z", loc.getZ());
		cs.set("yaw", loc.getYaw());
		cs.set("pitch", loc.getPitch());
	}
}

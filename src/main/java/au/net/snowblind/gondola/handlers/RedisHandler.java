package au.net.snowblind.gondola.handlers;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.BannerMeta;

import au.net.snowblind.gondola.Gondola;

public class RedisHandler {
	public static Location getLocation(String key) {
		Map<String, String> locMap = Gondola.jedis.hgetAll(key);
		return new Location(Gondola.plugin.getServer().getWorld(locMap.get("world")), Double.valueOf(locMap.get("x")),
				Double.valueOf(locMap.get("y")), Double.valueOf(locMap.get("z")),
				Float.valueOf(locMap.get("yaw")), Float.valueOf(locMap.get("pitch")));
	}
	
	public static void setLocation(String key, Location loc) {
		Gondola.jedis.hset(key, "world", loc.getWorld().getName());
		Gondola.jedis.hset(key, "x", Double.toString(loc.getX()));
		Gondola.jedis.hset(key, "y", Double.toString(loc.getY()));
		Gondola.jedis.hset(key, "z", Double.toString(loc.getZ()));
		Gondola.jedis.hset(key, "yaw", Float.toString(loc.getYaw()));
		Gondola.jedis.hset(key, "pitch", Float.toString(loc.getPitch()));
	}
	
	public static BannerMeta getMeta(String key, String field) {
		String configSerialized = Gondola.jedis.hget(key, field);
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.loadFromString(configSerialized);
		} catch (InvalidConfigurationException e) {
			return null;
		}
		return (BannerMeta) config.get("meta");
	}
	
	public static void setMeta(String key, String field, BannerMeta meta) {
		YamlConfiguration config = new YamlConfiguration();
		config.set("meta", meta);
		Gondola.jedis.hset(key, field, config.saveToString());
	}
}
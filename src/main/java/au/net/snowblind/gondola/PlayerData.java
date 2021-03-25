package au.net.snowblind.gondola;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {
	public static File defaultPlayerDataFile;
	public static FileConfiguration defaultPlayerData = new YamlConfiguration();
	public static File playerDataFolder = new File(Gondola.plugin.getDataFolder(), "playerdata");
	private File playerDataFile;
	public FileConfiguration playerData;

	public PlayerData (Player p) {
		loadDefaults();
		
		File f = new File(playerDataFolder, p.getUniqueId().toString() + ".yml");
		if (!f.exists())
			createPlayerData(p);
		loadPlayerData(p);
	}
	
	private void loadDefaults() {
		if (defaultPlayerDataFile == null || defaultPlayerData == null) {
			defaultPlayerDataFile = new File(playerDataFolder, "default.yml");
			if (!defaultPlayerDataFile.exists()) {
				playerDataFolder.mkdir();
			}
			Gondola.plugin.saveResource("playerdata/default.yml", true);
			try {
				defaultPlayerData.load(defaultPlayerDataFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createPlayerData(Player p) {
		Gondola.plugin.getLogger().info("Creating playerdata for " + p.getName());

		
		playerDataFile = new File(playerDataFolder, p.getUniqueId().toString() + ".yml");
		if (!playerDataFile.exists()) {
			playerDataFolder.mkdir();
		}
		
		playerData = new YamlConfiguration();
		playerData.setDefaults(defaultPlayerData);
		
		try {
			playerData.save(playerDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPlayerData(Player p) {
		playerDataFile = new File(playerDataFolder, p.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerDataFile);
		playerData.setDefaults(defaultPlayerData);
	}
	
	public void savePlayerData() {
		try {
			playerData.save(playerDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

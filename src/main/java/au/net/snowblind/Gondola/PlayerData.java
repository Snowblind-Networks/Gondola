package au.net.snowblind.Gondola;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {
	public static File defaultPlayerDataFile;
	public static FileConfiguration defaultPlayerData = new YamlConfiguration();
	private File playerDataFile;
	private FileConfiguration playerData;

	public PlayerData (Player p) {
		createPlayerData(p);
	}
	
	private void createPlayerData(Player p) {
		if (defaultPlayerDataFile == null || defaultPlayerData == null) {
			defaultPlayerDataFile = new File(Gondola.plugin.getDataFolder(), "/playerdata/default.yml");
			if (!defaultPlayerDataFile.exists()) {
				defaultPlayerDataFile.getParentFile().mkdirs();
				Gondola.plugin.saveResource("/playerdata/default.yml", false);
				
				try {
					defaultPlayerData.load(defaultPlayerDataFile);
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
		
		playerDataFile = new File(Gondola.plugin.getDataFolder(), "/playerdata/" + p.getUniqueId().toString() + ".yml");
		if (!playerDataFile.exists()) {
			playerDataFile.getParentFile().mkdirs();
		}
		
		playerData = new YamlConfiguration();
		
		playerData.setDefaults(defaultPlayerData);
		
		try {
			playerData.load(playerDataFile);
			playerData.save(playerDataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}

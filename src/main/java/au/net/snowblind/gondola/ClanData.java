package au.net.snowblind.gondola;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ClanData {
	private File clanDataFile;
	public FileConfiguration clanData;

	public ClanData () {
		clanDataFile = new File(Gondola.plugin.getDataFolder(), "clans.yml");
		if (!clanDataFile.exists())
			Gondola.plugin.saveResource("clans.yml", false);
		loadClanData();
		
	}
	
	public void createClan(String clan, Player owner) {
		ConfigurationSection cs = clanData.getConfigurationSection("clan");
		cs.set(clan + ".owner", owner.getUniqueId().toString());
		saveClanData();
	}
	
	public void deleteClan(String clan) {
		ConfigurationSection cs = clanData.getConfigurationSection("clan");
		cs.set(clan, null);
		saveClanData();
	}
	
	private void loadClanData() {
		clanData = YamlConfiguration.loadConfiguration(clanDataFile);
	}
	
	private void saveClanData() {
		try {
			clanData.save(clanDataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean contains(String clan) {
		return clanData.getConfigurationSection("clan").getKeys(false).contains(clan);
	}
	
	public boolean isMember(Player p, String clan) {
		ConfigurationSection cs = clanData.getConfigurationSection("clan." + clan);
		return cs.getString("owner").equals(p.getUniqueId().toString()) ||
				cs.getStringList("officers").contains(p.getUniqueId().toString()) ||
				cs.getStringList("members").contains(p.getUniqueId().toString());
	}
	
	public String getMembership(Player p) {
		return Gondola.players.get(p).playerData.getString("clan");
		
		/*
		ConfigurationSection cs = clanData.getConfigurationSection("clan");
		Set<String> clans = cs.getKeys(false);
		
		for (String clan : clans)
			if (isMember(p, clan))
				return clan;
		return null;
		*/
	}
	
	public OfflinePlayer getOwner(String clan) {
		return Gondola.plugin.getServer().getOfflinePlayer(UUID.fromString(clanData.getString("clan." + clan + ".owner")));
	}
	
	public void setOwner(String clan, Player p) {
		clanData.set("clan." + clan + ".owner", p.getUniqueId().toString());
		saveClanData();
	}
	
	public List<OfflinePlayer> getOfficers(String clan) {
		List<String> playerUUIDs = clanData.getStringList("clan." + clan + ".officers");
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for (String uuid : playerUUIDs) {
			players.add(Gondola.plugin.getServer().getOfflinePlayer(UUID.fromString(uuid)));
		}
		return players;
	}
	
	public void addOfficer(String clan, Player p) {
		List<String> officers = clanData.getStringList("clan." + clan + ".officers");
		officers.add(p.getUniqueId().toString());
		clanData.set("clan." + clan + ".officers", officers);
	}
	
	public void deleteOfficer(String clan, Player p) {
		List<String> officers = clanData.getStringList("clan." + clan + ".officers");
		officers.remove(p.getUniqueId().toString());
		clanData.set("clan." + clan + ".officers", officers);
	}
	
	public List<OfflinePlayer> getMembers(String clan) {
		List<String> playerUUIDs = clanData.getStringList("clan." + clan + ".members");
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for (String uuid : playerUUIDs) {
			players.add(Gondola.plugin.getServer().getOfflinePlayer(UUID.fromString(uuid)));
		}
		return players;
	}
	
	public void addMember(String clan, Player p) {
		List<String> members = clanData.getStringList("clan." + clan + ".officers");
		members.add(p.getUniqueId().toString());
		clanData.set("clan." + clan + ".officers", members);
	}
	
	public void deleteMember(String clan, Player p) {
		List<String> members = clanData.getStringList("clan." + clan + ".members");
		members.remove(p.getUniqueId().toString());
		clanData.set("clan." + clan + ".members", members);
	}
}

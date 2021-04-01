package au.net.snowblind.gondola;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;

import net.md_5.bungee.api.ChatColor;

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
		cs.set(clan + ".officers", new ArrayList<String>());
		cs.set(clan + ".members", new ArrayList<String>());
		cs.set(clan + ".colour", "WHITE");
		cs.set(clan + ".bannertype", "WHITE_BANNER");
		Gondola.players.get(owner).playerData.set("clan.name", clan);
		Gondola.players.get(owner).playerData.set("clan.position", "owner");
		saveClanData();
	}
	
	public void deleteClan(String clan, Player owner) {
		ConfigurationSection cs = clanData.getConfigurationSection("clan");
		Gondola.players.get(owner).playerData.set("clan", null);
		cs.set(clan, null);
		for (Player p : Gondola.plugin.getServer().getOnlinePlayers()) {
			PlayerData data = Gondola.players.get(p);
			if(!Gondola.clans.isMember(p, data.playerData.getString("clan"))) {
				data.playerData.set("clan", null);
			}
		}
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
		loadClanData();
	}
	
	public boolean contains(String clan) {
		Set<String> clans = clanData.getConfigurationSection("clan").getKeys(false);
		for (String c : clans) {
			if (c.equalsIgnoreCase(clan)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isMember(Player p, String clan) {
		if (clan == null) return false;
		ConfigurationSection cs = clanData.getConfigurationSection("clan." + clan);
		return cs.getString("owner").equals(p.getUniqueId().toString()) ||
				cs.getStringList("officers").contains(p.getUniqueId().toString()) ||
				cs.getStringList("members").contains(p.getUniqueId().toString());
	}
	
	public String getMembership(Player p) {
		return Gondola.players.get(p).playerData.getString("clan.name");
		
		/*
		ConfigurationSection cs = clanData.getConfigurationSection("clan");
		Set<String> clans = cs.getKeys(false);
		
		for (String clan : clans)
			if (isMember(p, clan))
				return clan;
		return null;
		*/
	}
	
	public String getPosition(Player p) {
		return Gondola.players.get(p).playerData.getString("clan.position");
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
		saveClanData();
	}
	
	public void deleteOfficer(String clan, Player p) {
		List<String> officers = clanData.getStringList("clan." + clan + ".officers");
		officers.remove(p.getUniqueId().toString());
		clanData.set("clan." + clan + ".officers", officers);
		saveClanData();
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
		saveClanData();
	}
	
	public void deleteMember(String clan, Player p) {
		List<String> members = clanData.getStringList("clan." + clan + ".members");
		members.remove(p.getUniqueId().toString());
		clanData.set("clan." + clan + ".members", members);
		saveClanData();
	}
	
	public ChatColor getColour(String clan) {
		Color colour = new Color(DyeColor.valueOf(clanData.getString("clan." + clan + ".colour")).getColor().asRGB());
		return ChatColor.of(colour);
	}
	
	public void setColour(String clan, DyeColor colour) {
		clanData.set("clan." + clan + ".colour", colour.toString());
		saveClanData();
	}
	
	public BannerMeta getBanner(String clan) {
		return (BannerMeta) clanData.get("clan." + clan + ".banner");
	}
	
	public Material getBannerType(String clan) {
		return Material.valueOf(clanData.getString("clan." + clan + ".bannertype"));
	}
	
	public void setBanner(String clan, BannerMeta meta, Material type) {
		clanData.set("clan." + clan + ".banner", meta);
		clanData.set("clan." + clan + ".bannertype", type.toString());
		saveClanData();
	}
}

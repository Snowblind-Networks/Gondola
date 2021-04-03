package au.net.snowblind.gondola;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;

import au.net.snowblind.gondola.handlers.RedisHandler;
import net.md_5.bungee.api.ChatColor;

public class Clans {
	public HashMap<Player, String> invites;
	
	public Clans () {
		invites = new HashMap<Player, String>();
		
	}
	
	public void createClan(String clan, Player owner) {
		String id = Gondola.jedis.incr("clanid").toString();
		Gondola.jedis.hset("clan:" + id, "name", clan);
		Gondola.jedis.hset("clan:" + id, "owner", owner.getUniqueId().toString());
		Gondola.jedis.hset("clan:" + id, "colour", "WHITE");
		Gondola.jedis.hset("clan:" + id, "bannertype", "WHITE_BANNER");
		Gondola.jedis.hset("user:" + owner.getUniqueId().toString() + ":clan", "id", id);
		Gondola.jedis.hset("user:" + owner.getUniqueId().toString() + ":clan", "name", clan);
		Gondola.jedis.hset("user:" + owner.getUniqueId().toString() + ":clan", "position", "owner");
		Gondola.jedis.sadd("clans", id);
	}
	
	public void deleteClan(String clanId) {
		String owner = Gondola.jedis.hget("clan:" + clanId, "owner");
		Set<String> officers = Gondola.jedis.smembers("clan:" + clanId + ":officers");
		Set<String> members = Gondola.jedis.smembers("clan:" + clanId + ":members");
		
		Gondola.jedis.del("user:" + owner + ":clan");
		
		for (String officer : officers) {
			Gondola.jedis.del("user:" + officer + ":clan");
		}
		
		for (String member : members) {
			Gondola.jedis.del("user:" + member + ":clan");
		}
		
		Gondola.jedis.del("clan:" + clanId);
		Gondola.jedis.del("clan:" + clanId + ":officers");
		Gondola.jedis.del("clan:" + clanId + ":members");
		Gondola.jedis.srem("clans", clanId);
	}
	
	public Map<String, String> getClans() {
		Set<String> clanIds = Gondola.jedis.smembers("clans");
		HashMap<String, String> clans = new HashMap<String, String>();
		for (String id : clanIds) {
			clans.put(Gondola.jedis.hget("clans:" + id, "name"), id);
		}
		return clans;
	}
	
	public boolean contains(String clanId) {
		return Gondola.jedis.smembers("clans").contains(clanId);
	}
	
	public boolean isMember(Player p, String clanId) {
		return (Gondola.jedis.hget("user:" + p.getUniqueId().toString() + ":clan", "id")
				.equalsIgnoreCase(clanId));
	}
	
	public String getMembership(Player p) {
		return Gondola.jedis.hget("user:" + p.getUniqueId().toString() + ":clan", "id");
	}
	
	public void removeFromClan(String clanId, Player p) {
		deleteOfficer(clanId, p);
		deleteMember(clanId, p);
	}
	
	public String getName(String clanId) {
		return Gondola.jedis.hget("clan:" + clanId, "name");
	}
	
	public String getPosition(Player p) {
		return Gondola.jedis.hget("user:" + p.getUniqueId().toString() + ":clan", "position");
	}
	
	public String getOwner(String clanId) {
		return Gondola.jedis.hget("clan:" + clanId, "owner");
	}
	
	public String getOwnerName(String clanId) {
		String id = getOwner(clanId);
		return Gondola.jedis.hget("user:" + id, "nickname");
	}
	
	public void setOwner(String clanId, Player p) {
		Gondola.jedis.hset("clan:" + clanId, "owner", p.getUniqueId().toString());
		Gondola.jedis.hset("user:" + p.getUniqueId() + ":clan", "id", clanId);
		Gondola.jedis.hset("user:" + p.getUniqueId() + ":clan", "name", Gondola.jedis.hget("clan:" + clanId, "name"));
		Gondola.jedis.hset("user:" + p.getUniqueId() + ":clan", "position", "owner");
	}
	
	public Set<String> getOfficers(String clanId) {
		return Gondola.jedis.smembers("clan:" + clanId + ":officers");
	}

	public Set<String> getOfficerNames(String clanId) {
		Set<String> ids = getOfficers(clanId);
		Set<String> names = new HashSet<String>();
		for(String id : ids) {
			names.add(Gondola.jedis.hget("user:" + id, "nickname"));
		}
		return names;
	}
	
	public void addOfficer(String clanId, Player p) {
		Gondola.jedis.sadd("clan:" + clanId + ":officers", p.getUniqueId().toString());
		Gondola.jedis.hset("user:" + p.getUniqueId() + ":clan", "id", clanId);
		Gondola.jedis.hset("user:" + p.getUniqueId() + ":clan", "name", Gondola.jedis.hget("clan:" + clanId, "name"));
		Gondola.jedis.hset("user:" + p.getUniqueId() + ":clan", "position", "officer");
	}
	
	public void deleteOfficer(String clanId, Player p) {
		Gondola.jedis.srem("clan:" + clanId + ":officers", p.getUniqueId().toString());
		Gondola.jedis.del("user:" + p.getUniqueId() + ":clan");
	}
	
	public Set<String> getMembers(String clanId) {
		return Gondola.jedis.smembers("clan:" + clanId + ":members");
	}
	
	public Set<String> getMemberNames(String clanId) {
		Set<String> ids = getMembers(clanId);
		Set<String> names = new HashSet<String>();
		for(String id : ids) {
			names.add(Gondola.jedis.hget("user:" + id, "nickname"));
		}
		return names;
	}
	
	public void addMember(String clanId, Player p) {
		Gondola.jedis.sadd("clan:" + clanId + ":members", p.getUniqueId().toString());
		Gondola.jedis.hset("user:" + p.getUniqueId().toString() + ":clan", "id", clanId);
		Gondola.jedis.hset("user:" + p.getUniqueId().toString() + ":clan", "name", getName(clanId));
		Gondola.jedis.hset("user:" + p.getUniqueId().toString() + ":clan", "position", "member");
	}
	
	public void deleteMember(String clanId, Player p) {
		Gondola.jedis.srem("clan:" + clanId + ":members", p.getUniqueId().toString());
		Gondola.jedis.del("user:" + p.getUniqueId() + ":clan");
	}
	
	public ChatColor getColour(String clanId) {
		Color colour = new Color(DyeColor.valueOf(Gondola.jedis.hget("clan:" + clanId, "colour")).getColor().asRGB());
		return ChatColor.of(colour);
	}
	
	public void setColour(String clanId, DyeColor colour) {
		Gondola.jedis.hset("clan:" + clanId, "colour", colour.toString());
	}
	
	public BannerMeta getBanner(String clanId) {
		return RedisHandler.getMeta("clan:" + clanId, "banner");
	}
	
	public Material getBannerType(String clanId) {
		return Material.valueOf(Gondola.jedis.hget("clan:" + clanId, "bannertype"));
	}
	
	public void setBanner(String clanId, BannerMeta meta, Material type) {
		RedisHandler.setMeta("clan:" + clanId, "banner", meta);
		Gondola.jedis.hset("clan:" + clanId, "bannertype", type.toString());
	}
	
	public Location getHome(String clanId) {
		return RedisHandler.getLocation("clanhome:" + clanId);
	}
	
	public void setHome(String clanId, Location loc) {
		RedisHandler.setLocation("clanhome:" + clanId, loc);
	}
}

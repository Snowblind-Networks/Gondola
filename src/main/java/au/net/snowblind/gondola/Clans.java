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

/**
 * Represents all the clans and provides facilities for their management.
 */
public class Clans {
	/**
	 * A map of players to the clan they've last been invited to.
	 */
	public HashMap<Player, String> invites;
	
	public Clans () {
		invites = new HashMap<Player, String>();
	}
	
	/**
	 * Creates a new clan and updates corresponding data.
	 * @param clan The clan's name.
	 * @param owner The player creating the clan.
	 */
	public void createClan(String clan, Player owner) {
		String id = Gondola.jedis.incr("clanid").toString();
		// Create clan data
		Gondola.jedis.hset("clan:" + id, Map.of(
				"name", clan,
				"owner", owner.getUniqueId().toString(),
				"colour", "WHITE",
				"bannertype", "WHITE_BANNER"));
		// Update user's data to include clan stuff
		Gondola.jedis.hset("user:" + owner.getUniqueId().toString() + ":clan", Map.of(
				"id", id,
				"name", clan,
				"position", "owner"));
		// Current existing clans
		Gondola.jedis.sadd("clans", id);
	}
	
	/**
	 * Deletes a clan and updates corresponding data.
	 * @param clanId The ID of the clan to delete.
	 */
	public void deleteClan(String clanId) {
		// Delete clan info from user data
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
		
		// Delete clan data
		Gondola.jedis.del("clan:" + clanId);
		Gondola.jedis.del("clan:" + clanId + ":officers");
		Gondola.jedis.del("clan:" + clanId + ":members");
		Gondola.jedis.srem("clans", clanId);
	}
	
	/**
	 * Gets existing clans.
	 * @return a map of clanIDs to clan names for each existing clan.
	 */
	public Map<String, String> getClans() {
		Set<String> clanIds = Gondola.jedis.smembers("clans");
		HashMap<String, String> clans = new HashMap<String, String>();
		
		for (String id : clanIds) {
			clans.put(Gondola.jedis.hget("clans:" + id, "name"), id);
		}
		return clans;
	}
	
	/**
	 * Tests whether a clan exists.
	 * @param clanId The clan's ID.
	 * @return whether the clan currently exists.
	 */
	public boolean contains(String clanId) {
		return Gondola.jedis.smembers("clans").contains(clanId);
	}
	
	/**
	 * Sees whether player is a member of a certain clan.
	 * @param p The player
	 * @param clanId The clan
	 * @return Whether p is in the clan referred to by clanId
	 */
	public boolean isMember(Player p, String clanId) {
		return (Gondola.jedis.hget("user:" + p.getUniqueId().toString() + ":clan", "id")
				.equalsIgnoreCase(clanId));
	}
	
	/**
	 * Gets the clan a player is in.
	 * @param p The player to check.
	 * @return Which clan p is in, or null if none.
	 */
	public String getMembership(Player p) {
		return Gondola.jedis.hget("user:" + p.getUniqueId().toString() + ":clan", "id");
	}
	
	public Set<String> getAllPlayers(String clanId) {
		String owner = Gondola.jedis.hget("clan:" + clanId, "owner");
		Set<String> officers = Gondola.jedis.smembers("clan:" + clanId + ":officers");
		Set<String> members = Gondola.jedis.smembers("clan:" + clanId + ":members");
		
		Set<String> players = new HashSet<String>();
		players.add(owner);
		players.addAll(officers);
		players.addAll(members);
		return players;
	}
	
	/**
	 * Remove a player from a clan.
	 * @param clanId The clan's ID.
	 * @param p The player to remove.
	 */
	public void removeFromClan(String clanId, Player p) {
		deleteOfficer(clanId, p);
		deleteMember(clanId, p);
	}
	
	/**
	 * Get the name of a clan.
	 * @param clanId The clan's ID.
	 * @return the clan's name.
	 */
	public String getName(String clanId) {
		return Gondola.jedis.hget("clan:" + clanId, "name");
	}
	
	/**
	 * Get a player's position in a clan.
	 * @param p The player.
	 * @return the player's position (eg. owner).
	 */
	public String getPosition(Player p) {
		return Gondola.jedis.hget("user:" + p.getUniqueId().toString() + ":clan", "position");
	}
	
	/**
	 * Get a clan's owner.
	 * @param clanId The clan's ID.
	 * @return the clan owner's UUID as a String.
	 */
	public String getOwner(String clanId) {
		return Gondola.jedis.hget("clan:" + clanId, "owner");
	}
	
	/**
	 * Get the nickname of the clan's owner.
	 * @param clanId The clan's ID.
	 * @return the nickname of the clan's owner.
	 */
	public String getOwnerName(String clanId) {
		String id = getOwner(clanId);
		return Gondola.jedis.hget("user:" + id, "nickname");
	}
	
	/**
	 * Set the owner of a clan.
	 * @param clanId The clan's ID.
	 * @param p The player.
	 */
	public void setOwner(String clanId, Player p) {
		Gondola.jedis.hset("clan:" + clanId, "owner", p.getUniqueId().toString());
		Gondola.jedis.hset("user:" + p.getUniqueId().toString() + ":clan", Map.of(
				"id", clanId,
				"name", Gondola.jedis.hget("clan:" + clanId, "name"),
				"position", "owner"));
	}
	
	/**
	 * Get the officers of a clan.
	 * @param clanId The clan's ID.
	 * @return a set of the UUIDs of the clan's officers.
	 */
	public Set<String> getOfficers(String clanId) {
		return Gondola.jedis.smembers("clan:" + clanId + ":officers");
	}

	/**
	 * Get the nicknames of a clan's officers.
	 * @param clanId The clan's ID.
	 * @return a set of the nicknames of the clan's officers.
	 */
	public Set<String> getOfficerNames(String clanId) {
		Set<String> ids = getOfficers(clanId);
		Set<String> names = new HashSet<String>();
		for(String id : ids) {
			names.add(Gondola.jedis.hget("user:" + id, "nickname"));
		}
		return names;
	}
	
	/**
	 * Add an officer to a clan.
	 * @param clanId The clan's ID.
	 * @param p The player.
	 */
	public void addOfficer(String clanId, Player p) {
		Gondola.jedis.sadd("clan:" + clanId + ":officers", p.getUniqueId().toString());
		Gondola.jedis.hset("user:" + p.getUniqueId().toString() + ":clan", Map.of(
				"id", clanId,
				"name", Gondola.jedis.hget("clan:" + clanId, "name"),
				"position", "officer"));
	}
	
	/**
	 * Remove an officer from a clan.
	 * @param clanId The clan's ID.
	 * @param p The officer.
	 */
	public void deleteOfficer(String clanId, Player p) {
		Gondola.jedis.srem("clan:" + clanId + ":officers", p.getUniqueId().toString());
		Gondola.jedis.del("user:" + p.getUniqueId() + ":clan");
	}
	
	/**
	 * Get the members of the clan (not including officers and owner).
	 * @param clanId The clan's ID.
	 * @return a set of the UUIDs of the clan's members.
	 */
	public Set<String> getMembers(String clanId) {
		return Gondola.jedis.smembers("clan:" + clanId + ":members");
	}
	
	/**
	 * Get the nicknames of the members of the clan (not including officers and owner).
	 * @param clanId The clan's ID,
	 * @return a set of the nicknames of the clan's members.
	 */
	public Set<String> getMemberNames(String clanId) {
		Set<String> ids = getMembers(clanId);
		Set<String> names = new HashSet<String>();
		for(String id : ids) {
			names.add(Gondola.jedis.hget("user:" + id, "nickname"));
		}
		return names;
	}
	
	/**
	 * Adds a member to a clan.
	 * @param clanId The clan's ID.
	 * @param p The player.
	 */
	public void addMember(String clanId, Player p) {
		Gondola.jedis.sadd("clan:" + clanId + ":members", p.getUniqueId().toString());
		Gondola.jedis.hset("user:" + p.getUniqueId().toString() + ":clan", Map.of(
				"id", clanId,
				"name", Gondola.jedis.hget("clan:" + clanId, "name"),
				"position", "member"));
	}
	
	/**
	 * Removes a member from a clan.
	 * @param clanId The clan's ID.
	 * @param p The player.
	 */
	public void deleteMember(String clanId, Player p) {
		Gondola.jedis.srem("clan:" + clanId + ":members", p.getUniqueId().toString());
		Gondola.jedis.del("user:" + p.getUniqueId() + ":clan");
	}
	
	/**
	 * Gets the colour of the clan (for use in clan tags).
	 * @param clanId The clan's ID.
	 * @return the ChatColour of the clan.
	 */
	public ChatColor getColour(String clanId) {
		/*
		 * Colour is retrieved as a DyeColor string, converted to org.bukkit.Color,
		 * converted to an RGB int, constructed into a java.awt.Colour,
		 * and converted into a Bungee ChatColor.
		 */
		Color colour = new Color(DyeColor.valueOf(Gondola.jedis.hget("clan:" + clanId, "colour")).getColor().asRGB());
		return ChatColor.of(colour);
	}
	
	/**
	 * Sets the colour of a clan.
	 * @param clanId The clan's ID.
	 * @param colour The colour.
	 */
	public void setColour(String clanId, DyeColor colour) {
		Gondola.jedis.hset("clan:" + clanId, "colour", colour.toString());
	}
	
	/**
	 * Gets the clan banner's metadata.
	 * @param clanId The clan's ID.
	 * @return the BannerMeta of the clan's banner.
	 */
	public BannerMeta getBanner(String clanId) {
		return RedisHandler.getMeta("clan:" + clanId, "banner");
	}
	
	/**
	 * Gets the clan banner's type.
	 * @param clanId The clan's ID.
	 * @return the clan banner's Material type.
	 */
	public Material getBannerType(String clanId) {
		return Material.valueOf(Gondola.jedis.hget("clan:" + clanId, "bannertype"));
	}
	
	/**
	 * Set the clan's banner meta and Material type.
	 * @param clanId The clan's ID.
	 * @param meta The banner's BannerMeta.
	 * @param type The banner's Material type.
	 */
	public void setBanner(String clanId, BannerMeta meta, Material type) {
		RedisHandler.setMeta("clan:" + clanId, "banner", meta);
		Gondola.jedis.hset("clan:" + clanId, "bannertype", type.toString());
	}
	
	/**
	 * Get the clan's home.
	 * @param clanId The clan's ID.
	 * @return the clan's home as a Location.
	 */
	public Location getHome(String clanId) {
		return RedisHandler.getLocation("clanhome:" + clanId);
	}
	
	/**
	 * Set the clan's home.
	 * @param clanId The clan's ID.
	 * @param loc The clan's new home.
	 */
	public void setHome(String clanId, Location loc) {
		RedisHandler.setLocation("clanhome:" + clanId, loc);
	}
}
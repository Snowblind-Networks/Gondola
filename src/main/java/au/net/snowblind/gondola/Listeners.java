package au.net.snowblind.gondola;

import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import au.net.snowblind.gondola.handlers.ChatHandler;

public class Listeners implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();
		// Set display name to saved nickname, if there is one
		if (Gondola.jedis.hexists("user:" + uuid, "nickname")) {
			e.getPlayer().setDisplayName(Gondola.jedis.hget(
					"user:" + uuid, "nickname"));
		}
		
		// Consistency checks
		if(Gondola.jedis.exists("user:" + uuid + ":clan")) {
			String clanId = Gondola.clans.getMembership(p);
			Set<String> players = Gondola.clans.getAllPlayers(clanId);
			
			if (!players.contains(uuid)) {
				p.sendMessage(ChatHandler.error("Your playerdata is corrupted! Contact an admin!"));
				Gondola.plugin.getLogger().warning("The playerdata for " + uuid +
						" contains clan " + clanId + " but that clan's data does not contain the player.");
			}
			
			String position = Gondola.clans.getPosition(p);
			switch (position) {
			case "owner":
				if (!Gondola.clans.getOwner(clanId).equalsIgnoreCase(uuid)) {
					p.sendMessage(ChatHandler.error("Your playerdata is corrupted! Contact an admin!"));
					Gondola.plugin.getLogger().warning("The playerdata for " + uuid +
							" has position 'owner' but that clan doesn't have this player as owner.");
				}
				break;
			case "officer":
				if (!Gondola.clans.getOfficers(clanId).contains(uuid)) {
					p.sendMessage(ChatHandler.error("Your playerdata is corrupted! Contact an admin!"));
					Gondola.plugin.getLogger().warning("The playerdata for " + uuid +
							" has position 'officer' but that clan doesn't have this player as an officer.");
				}
				break;
			case "member":
				if (!Gondola.clans.getMembers(clanId).contains(uuid)) {
					p.sendMessage(ChatHandler.error("Your playerdata is corrupted! Contact an admin!"));
					Gondola.plugin.getLogger().warning("The playerdata for " + uuid +
							" has position 'member' but that clan doesn't have this player as a member.");
				}
				break;
			default:
				p.sendMessage(ChatHandler.error("Your playerdata is corrupted! Contact an admin!"));
				Gondola.plugin.getLogger().warning("The playerdata for " + uuid +
						" has an invalid position '" + position + "' within its clan.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Gondola.clans.invites.remove(p);
		Gondola.teleports.remove(p);
	}
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		// Provide a random server icon
		Random rand = new Random();
		int index = rand.nextInt(Icons.icons.size());
		try {
			e.setServerIcon(Bukkit.loadServerIcon(Icons.icons.get(index)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		// Apply chat formats
		e.setFormat(ChatHandler.getFormat(e.getPlayer()));
		e.setMessage(ChatHandler.processMessage(e.getMessage()));
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		// Drop player head
		Player p = e.getEntity();
		if (p.isDead() && p.getKiller() instanceof Player) {
	    	ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
	    	SkullMeta sm = (SkullMeta) head.getItemMeta();
	    	sm.setOwningPlayer(p);
	    	head.setItemMeta(sm);
			e.getDrops().add(head);
		}
	}
}

package au.net.snowblind.gondola;

import java.util.Random;

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
		if (!Gondola.jedis.hexists("user:" + e.getPlayer().getUniqueId().toString(), "nickname")) {
			Gondola.jedis.hset("user:" + e.getPlayer().getUniqueId().toString(), "nickname", e.getPlayer().getDisplayName());
		} else {
			e.getPlayer().setDisplayName(Gondola.jedis.hget(
					"user:" + e.getPlayer().getUniqueId().toString(), "nickname"));
		}
		
		// Consistency checks
		/*
		if(!Gondola.clans.isMember(p, data.playerData.getString("clan.name"))) {
			data.playerData.set("clan", null);
		} else if (Gondola.clans.getPosition(p) != data.playerData.getString("clan.position")) {
			data.playerData.set("clan.position", Gondola.clans.getPosition(p));
		}
		*/
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Gondola.clans.invites.remove(p);
	}
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
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
		e.setFormat(ChatHandler.getFormat(e.getPlayer()));
		e.setMessage(ChatHandler.processMessage(e.getMessage()));
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
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

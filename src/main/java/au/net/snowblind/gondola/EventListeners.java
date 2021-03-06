package au.net.snowblind.gondola;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import au.net.snowblind.gondola.events.FlagCaptureStartEvent;
import au.net.snowblind.gondola.handlers.ChatHandler;
import net.md_5.bungee.api.ChatColor;

public class EventListeners implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();
		// Set display name to saved nickname, if there is one
		if (Gondola.jedis.hexists("user:" + uuid, "nickname")) {
			e.getPlayer().setDisplayName(Gondola.jedis.hget(
					"user:" + uuid, "nickname"));
		} else {
			Gondola.jedis.hset("user:" + uuid, "nickname", e.getPlayer().getDisplayName());
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
		
		String clan = Gondola.clans.getMembership(p);
		if (clan != null) {
			for (FlagCapture cap : Gondola.captures.values()) {
				if (clan.equals(cap.getAttackingClan()))
					cap.addAttackingPlayer(p);
				else if (clan.equals(cap.getDefendingClan()))
					cap.addDefendingPlayer(p);
			}
		}
		
		// Display join message only if more than 20 players are on
		if (Gondola.plugin.getServer().getOnlinePlayers().size() > 20) {
			e.setJoinMessage(null);
		} else {
			e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + p.getDisplayName());
		}
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Gondola.clans.invites.remove(p);
		Gondola.teleports.remove(p);
		
		String clan = Gondola.clans.getMembership(p);
		if (clan != null) {
			for (FlagCapture cap : Gondola.captures.values()) {
				if (clan.equals(cap.getAttackingClan()))
					cap.delAttackingPlayer(p);
				else if (clan.equals(cap.getDefendingClan()))
					cap.delDefendingPlayer(p);
			}
		}
		
		// Display quit message only if more than 20 players are on
		if (Gondola.plugin.getServer().getOnlinePlayers().size() > 20) {
			e.setQuitMessage(null);
		} else {
			e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + p.getDisplayName());
		}
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
		Player p = e.getPlayer();
		
		// Handle muted player
		if (Gondola.mutes.containsKey(p)) {
			if (Gondola.mutes.get(p).isBefore(LocalDateTime.now())) {
				Gondola.mutes.remove(p);
			} else {
				p.sendMessage(ChatHandler.warn("You are muted!"));
				e.setCancelled(true);
			}
		}
		
		// Apply chat formats
		e.setFormat(ChatHandler.getFormat(p));
		e.setMessage(ChatHandler.processMessage(e.getMessage()));
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (p.isDead() && p.getKiller() instanceof Player) {
			// Drop player head
	    	ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
	    	SkullMeta sm = (SkullMeta) head.getItemMeta();
	    	sm.setOwningPlayer(p);
	    	head.setItemMeta(sm);
			e.getDrops().add(head);
			
			// Give points to the winner's clan
			String killerClan = Gondola.clans.getMembership(p.getKiller());
			String killedClan = Gondola.clans.getMembership(p);
			
			long stolen = 0;
			if (killerClan != null && killedClan != null) {
				stolen += Gondola.clans.stealPoints(killerClan, killedClan, 1);
			}
			String influenceClan = Gondola.clans.getInfluence(p.getLocation());
			if (influenceClan != null && killedClan != null) {
				stolen += Gondola.clans.stealPoints(influenceClan, killedClan, 1);
			}
			
			if (stolen > 0)
				e.setDeathMessage(e.getDeathMessage() + " and lost " + stolen + " point(s)");
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (!(e.isBedSpawn() || e.isAnchorSpawn())) {
			e.setRespawnLocation(Gondola.plugin.getConfig().getLocation("spawn.point"));
		}
	}
	
	@EventHandler
	public void onFirstSpawn(PlayerSpawnLocationEvent e) {
		if (!e.getPlayer().hasPlayedBefore())
			e.setSpawnLocation(Gondola.plugin.getConfig().getLocation("spawn.point"));
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			String clanTo = Gondola.clans.getMembership((Player) e.getEntity());
			String clanFrom = Gondola.clans.getMembership((Player) e.getDamager());
			if (clanTo != null && clanFrom != null && clanTo.equals(clanFrom)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (Gondola.flags.containsKey(e.getBlock()) || Gondola.flags.containsKey(e.getBlock().getRelative(BlockFace.UP))) {
			e.setCancelled(true);
			Block b = e.getBlock();
			String attackingClan = Gondola.clans.getMembership(e.getPlayer());
			String defendingClan = Gondola.flags.get(b);
			if (!defendingClan.equals(attackingClan)) {
				Bukkit.getPluginManager().callEvent(new FlagCaptureStartEvent(defendingClan, attackingClan, b));
			}
		}
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent e) {
		if (Gondola.flags.containsKey(e.getBlock()) || Gondola.flags.containsKey(e.getBlock().getRelative(BlockFace.UP))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		Set<Block> blocks = new HashSet<Block>();
		for (Block b : e.blockList()) {
			blocks.add(b.getRelative(BlockFace.UP));
			blocks.add(b);
		}
		
		blocks.retainAll(Gondola.flags.keySet());
		if (blocks.size() > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		Set<Block> blocks = new HashSet<Block>();
		for (Block b : e.blockList()) {
			blocks.add(b.getRelative(BlockFace.UP));
			blocks.add(b);
		}
		
		blocks.retainAll(Gondola.flags.keySet());
		if (blocks.size() > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPistoned(BlockPistonExtendEvent e) {
		Set<Block> blocks = new HashSet<Block>();
		for (Block b : e.getBlocks()) {
			blocks.add(b.getRelative(BlockFace.UP));
			blocks.add(b);
		}
		
		blocks.retainAll(Gondola.flags.keySet());
		if (blocks.size() > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPistoned(BlockPistonRetractEvent e) {
		Set<Block> blocks = new HashSet<Block>();
		for (Block b : e.getBlocks()) {
			blocks.add(b.getRelative(BlockFace.UP));
			blocks.add(b);
		}
		
		blocks.retainAll(Gondola.flags.keySet());
		if (blocks.size() > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e) {
		Block b = e.getBlock();
		if (Tag.BANNERS.isTagged(b.getType())) {
			if (Gondola.flags.containsKey(b)) {
				b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onVotifierEvent(VotifierEvent e) {
		Vote vote = e.getVote();
		@SuppressWarnings("deprecation")
		OfflinePlayer p = Gondola.plugin.getServer().getOfflinePlayer(vote.getUsername());
		
		if (p != null) {
			String msg = ChatColor.DARK_AQUA + vote.getUsername() + " just voted and got $500";
			
			String uuid = p.getUniqueId().toString();
			String clan = Gondola.clans.getMembership(uuid);
			if (clan != null) {
				Gondola.clans.addPoints(clan, 2);
				msg += " and 2 points for their clan";
			}
			Gondola.vault.economy.depositPlayer(p, 500);
			
			Gondola.plugin.getServer().broadcastMessage(msg);
		}
	}
	
	@EventHandler
	public void onFlagCaptureStart(FlagCaptureStartEvent e) {
		if (Gondola.captures.containsKey(e.getDefendingClan())) {
			e.setCancelled(true);
			return;
		}
		
		FlagCapture cap = new FlagCapture(e.getDefendingClan(), e.getAttackingClan(), Gondola.clans.getHome(e.getDefendingClan()));
		BukkitTask task = Gondola.plugin.getServer().getScheduler().runTaskTimerAsynchronously(Gondola.plugin, cap, 10, 10);
		cap.setTask(task);
		
		Gondola.captures.put(e.getDefendingClan(), cap);
	}
}

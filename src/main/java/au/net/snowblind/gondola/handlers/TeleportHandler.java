package au.net.snowblind.gondola.handlers;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import au.net.snowblind.gondola.Gondola;

public class TeleportHandler {
	public static HashMap<Player, Location> teleports = new HashMap<Player, Location>();
	
	public static void teleport(CommandSender cs, final Player p, final Location dest) {
		final Location src = p.getLocation();;
		
		if (Gondola.vault.permission.has(cs, "gondola.teleport.instant")) {
			p.teleport(dest, TeleportCause.PLUGIN);
		} else {
			if (!teleports.containsKey(p)) {
				p.sendMessage("Teleporting in 5 seconds.");
				
				Gondola.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Gondola.plugin, new Runnable() {
					@Override
					public void run() {
						p.getWorld().spawnParticle(Particle.PORTAL, src, 100);
					}
				}, 50L);
				
				teleports.put(p, dest);
				Gondola.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Gondola.plugin, new Runnable() {
					@Override
					public void run() {
						if (p.getLocation().distanceSquared(src) < 0.2) {
							if (teleports.get(p) == dest) {
								p.teleport(dest, TeleportCause.PLUGIN);
							} else if (!teleports.containsKey(p)) {
								p.sendMessage("Teleport has been cancelled.");
							} else {
								p.sendMessage("Too many teleport requests!");
							}
						} else {
							p.sendMessage("No moving during teleports!");
						}
						teleports.remove(p);
					}
				}, 100L);
			} else {
				p.sendMessage("You already have a teleport queued.");
			}
		}
	}
}

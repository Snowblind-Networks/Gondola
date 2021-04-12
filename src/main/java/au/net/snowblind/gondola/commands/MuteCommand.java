package au.net.snowblind.gondola.commands;

import java.time.LocalDateTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class MuteCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : "Console";
		if (args.length != 1) return false;
		
		if (sender instanceof Player && !Gondola.clans.getMembership(((Player) sender).getUniqueId().toString()).equals(Gondola.clans.getTopClan())) {
			sender.sendMessage(ChatHandler.error("You don't have permission to mute players."));
			return true;
		}
		
		Player p = Gondola.plugin.getServer().getPlayer(args[0]);
		if (p == null) {
			sender.sendMessage(ChatHandler.error("Cannot find player " + args[0]));
		} else {
			if (Gondola.mutes.containsKey(p)) {
				sender.sendMessage(ChatHandler.warn("Player " + args[0] + " is already muted."));
			} else {
				Gondola.plugin.getServer().broadcastMessage(ChatHandler.warn(p.getDisplayName() + " has been muted by " + name + " for 10 minutes."));
				Gondola.mutes.put(p, LocalDateTime.now().plusMinutes(10));
			}
		}
		return true;
	}
}

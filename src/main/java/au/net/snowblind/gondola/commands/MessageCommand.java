package au.net.snowblind.gondola.commands;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class MessageCommand implements CommandExecutor {
	public static HashMap<Player, Player> prevMessager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg")) { // in /msg command
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (Gondola.mutes.containsKey(p)) {
					if (Gondola.mutes.get(p).isBefore(LocalDateTime.now())) {
						Gondola.mutes.remove(p);
					} else {
						sender.sendMessage(ChatHandler.warn("You are muted!"));
						return true;
					}
				}
			}
			
			if (args.length < 2) {
				return false;
			}
			
			Player p = Gondola.plugin.getServer().getPlayer(args[0]);
			
			if (p == null) {
				sender.sendMessage(ChatHandler.error("Player " + args[0] + " not found."));
				return false;
			}
			
			String msg = ChatHandler.processMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length))); 

			if (sender instanceof Player) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + ((Player) sender).getDisplayName() + ">> " + msg);
				p.sendMessage(ChatColor.LIGHT_PURPLE + ">> " + ((Player) sender).getDisplayName() + ": " + msg);
				prevMessager.put(p, (Player) sender); // prevMessager[receiver] == sender
				return true;
			} else {
				p.sendMessage(ChatColor.LIGHT_PURPLE + ">> Console: " + msg);
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("r")) { // in /r command
			if (args.length == 0) {
				return false;
			}
			
			Player p = Gondola.plugin.getServer().getPlayer(prevMessager.get((Player) sender).getName());
			if (p != null && p.isOnline()) {
				String msg = ChatHandler.processMessage(String.join(" ", args)); 
				sender.sendMessage(ChatColor.LIGHT_PURPLE + ((Player) sender).getDisplayName() + ">> " + msg);
				p.sendMessage(ChatColor.LIGHT_PURPLE + ">> " + ((Player) sender).getDisplayName() + ": " + msg);
				prevMessager.put(p, (Player) sender);
				return true;
			} else {
				sender.sendMessage(ChatHandler.error("You have nobody to reply to."));
				return false;
			}
		} else {
			Gondola.plugin.getLogger().warning("Something went wrong with au.net.snowblind.gondola.commands.MessageCommand.");
			return false;
		}
	}
}

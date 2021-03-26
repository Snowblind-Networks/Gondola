package au.net.snowblind.gondola.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class MessageCommand implements CommandExecutor {
	public static HashMap<Player, Player> prevMessager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg")) { // in /msg command
			if (args.length < 2) {
				return false;
			}
			
			Player p = Gondola.plugin.getServer().getPlayer(args[0]);
			
			if (p == null) {
				sender.sendMessage("Player " + args[0] + " not found!");
				return false;
			}
			
			String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length)); 
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
				String msg = String.join(" ", args); 
				sender.sendMessage(ChatColor.LIGHT_PURPLE + ((Player) sender).getDisplayName() + ">> " + msg);
				p.sendMessage(ChatColor.LIGHT_PURPLE + ">> " + ((Player) sender).getDisplayName() + ": " + msg);
				return true;
			} else {
				sender.sendMessage("You have nobody to reply to.");
				return false;
			}
		} else {
			Gondola.plugin.getLogger().warning("Something went wrong with au.net.snowblind.gondola.commands.MessageCommand!");
			return false;
		}
	}
}

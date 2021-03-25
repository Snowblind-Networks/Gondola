package au.net.snowblind.gondola.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class MessageCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
			return true;
		} else {
			p.sendMessage(ChatColor.LIGHT_PURPLE + ">> Console: " + msg);
			return true;
		}
	}
}

package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;


public class CreateClanCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else {
				if (Gondola.clans.getMembership((Player) sender) != null) {
					sender.sendMessage("You are already in a clan!");
					return true;
				}
				if (Gondola.clans.contains(args[0])) {
					sender.sendMessage("The clan name " + args[0] + " is already taken!");
					return true;
				}
				Gondola.clans.createClan(args[0], ((Player) sender));
				sender.sendMessage("Clan " + args[0] + " created!");
			}
			
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

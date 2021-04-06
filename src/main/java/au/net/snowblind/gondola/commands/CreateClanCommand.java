package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;


public class CreateClanCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else {
				if (Gondola.clans.getMembership((Player) sender) != null) {
					sender.sendMessage(ChatHandler.error("You are already in a clan."));
					return true;
				}
				if (Gondola.clans.containsName(args[0])) {
					sender.sendMessage(ChatHandler.error("The clan name " + args[0] + " is already taken."));
					return true;
				}
				if (args[0].length() > 10) {
					sender.sendMessage(ChatHandler.error("Clan name can't be more than 10 characters."));
				} else {
					Gondola.clans.createClan(args[0], ((Player) sender));
					sender.sendMessage(ChatHandler.info("Clan " + args[0] + " created."));
				}
			}
			
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

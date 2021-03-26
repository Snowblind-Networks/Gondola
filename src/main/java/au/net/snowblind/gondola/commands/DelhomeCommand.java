package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.handlers.HomeHandler;


public class DelhomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else {
				if (args[0].equals("default")) {
					sender.sendMessage("You cannot delete your default home!");
					return false;
				} else {
					HomeHandler.delHome((Player) sender, args[0]);
					sender.sendMessage("Deleted home " + args[0]);
					return true;
				}
			}
			
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

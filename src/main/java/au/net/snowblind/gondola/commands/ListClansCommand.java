package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class ListClansCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 0) {
				return false;
			} else {
				sender.sendMessage("Clans: " + String.join(",", Gondola.clans.getClans()));
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

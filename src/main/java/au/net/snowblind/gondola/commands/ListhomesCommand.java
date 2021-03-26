package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.HomeHandler;

public class ListhomesCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 1 && Gondola.vault.permission.playerInGroup((Player) sender, "admin")) {
				Player p = Gondola.plugin.getServer().getPlayer(args[0]);
				if (p == null) {
					sender.sendMessage("Player not found: " + args[0]);
					return false;
				}
				sender.sendMessage("Homes: " + String.join(", ", HomeHandler.getHomes(Gondola.plugin.getServer().getPlayer(args[0]))));
				return true;
			} else if (args.length == 0) {
				sender.sendMessage("Homes: " + String.join(", ", HomeHandler.getHomes((Player) sender)));
				return true;
			} else {
				return false;
			}
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

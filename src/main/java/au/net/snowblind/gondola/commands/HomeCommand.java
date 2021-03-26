package au.net.snowblind.gondola.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.HomeHandler;
import au.net.snowblind.gondola.handlers.TeleportHandler;

public class HomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Location home;
			if (args.length == 2 && Gondola.vault.permission.playerInGroup((Player) sender, "admin")) {
				if ((home = HomeHandler.getHome(Gondola.plugin.getServer().getPlayer(args[0]), args[1])) == null) {
					sender.sendMessage(ChatColor.RED + "Invalid home: " + args[0]);
					return false;
				} else {
					TeleportHandler.teleport(sender, (Player) sender, home);
					return true;
				}
			} else if (args.length > 1) {
				return false;
			} else if (args.length == 0) {
				if ((home = HomeHandler.getHome((Player) sender, "default")) != null) {
					TeleportHandler.teleport(sender, (Player) sender, home);
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Your default home is not set!");
					return false;
				}
			} else if (HomeHandler.getHomes((Player) sender).size() == 0) {
				sender.sendMessage(ChatColor.RED + "You don't have any homes set!");
				return false;
			} else if ((home = HomeHandler.getHome((Player) sender, args[0])) == null) {
				sender.sendMessage(ChatColor.RED + "Invalid home: " + args[0]);
				return false;
			} else {
				TeleportHandler.teleport(sender, (Player) sender, home);
				return true;
			}
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

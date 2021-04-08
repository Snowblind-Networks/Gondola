package au.net.snowblind.gondola.commands;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import au.net.snowblind.gondola.handlers.HomeHandler;
import au.net.snowblind.gondola.handlers.TeleportHandler;

public class HomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Location home;
			if (args.length == 2 && Gondola.vault.permission.playerInGroup((Player) sender, "admin")) {
				if ((home = HomeHandler.getHome(Gondola.plugin.getServer().getPlayer(args[0]), args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Invalid home: " + args[0] + "."));
				} else {
					TeleportHandler.teleport(sender, (Player) sender, home);
				}
			} else if (args.length > 1) {
				return false;
			} else if (args.length == 0) {
				Set<String> homes = HomeHandler.getHomes((Player) sender);
				if (homes.size() > 1) {
					sender.sendMessage(ChatHandler.info("Homes: " + String.join(", ", homes)));
				} else if (homes.size() == 1) {
					sender.sendMessage(ChatHandler.info("Teleporting home."));
					TeleportHandler.teleport(sender, (Player) sender, 
							HomeHandler.getHome((Player) sender, homes.iterator().next()));
				} else {
					sender.sendMessage(ChatHandler.error("You don't have any homes set."));
				}
			} else if (HomeHandler.getHomes((Player) sender).size() == 0) {
				sender.sendMessage(ChatHandler.error("You don't have any homes set."));
			} else if ((home = HomeHandler.getHome((Player) sender, args[0])) == null) {
				sender.sendMessage(ChatHandler.error("Invalid home: " + args[0] + "."));
			} else {
				sender.sendMessage(ChatHandler.info("Teleporting to home " + args[0] + "."));
				TeleportHandler.teleport(sender, (Player) sender, home);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

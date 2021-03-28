package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.handlers.HomeHandler;
import au.net.snowblind.gondola.handlers.WarpHandler;

public class SetwarpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else {
				WarpHandler.setWarp(args[0], ((Player) sender).getLocation());
				return true;
			}
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

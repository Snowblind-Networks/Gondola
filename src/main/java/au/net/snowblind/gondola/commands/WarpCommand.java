package au.net.snowblind.gondola.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.handlers.TeleportHandler;
import au.net.snowblind.gondola.handlers.WarpHandler;

public class WarpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Location warp;
			if (args.length > 1) {
				return false;
			} else if (args.length == 0) {
				Set<String> warps;
				if ((warps = WarpHandler.getWarps()) != null && warps.size() > 0) {
					sender.sendMessage("Warps: " + String.join(", ", warps));
				} else {
					sender.sendMessage("There are no warps set!");
				}
			} else if ((warp = WarpHandler.getWarp(args[0])) == null) {
				sender.sendMessage(ChatColor.RED + "Invalid warp: " + args[0]);
			} else {
				TeleportHandler.teleport(sender, (Player) sender, warp);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

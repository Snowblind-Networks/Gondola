package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.handlers.WarpHandler;


public class DelwarpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else if (WarpHandler.getWarp(args[0]) != null) {
				WarpHandler.delWarp(args[0]);
				sender.sendMessage("Deleted warp " + args[0]);
			} else {
				sender.sendMessage("Warp not found: " + args[0]);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import au.net.snowblind.gondola.handlers.WarpHandler;


public class DelwarpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (!Gondola.vault.permission.has(sender, "gondola.delwarp")) {
				sender.sendMessage(ChatHandler.error("You don't have permission to run this command."));
			} else if (args.length != 1) {
				return false;
			} else if (WarpHandler.getWarp(args[0]) != null) {
				WarpHandler.delWarp(args[0]);
				sender.sendMessage(ChatHandler.warn("Deleted warp " + args[0] + "."));
			} else {
				sender.sendMessage(ChatHandler.error("Warp not found: " + args[0] + "."));
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

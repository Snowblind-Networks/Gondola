package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.TeleportHandler;

public class TeleportCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				sender.sendMessage("Usage: /tp <player>");
				return false;
			}
			Player p = Gondola.plugin.getServer().getPlayer(args[0]);
			
			if (p == null) {
				sender.sendMessage("Player " + args[0] + " not found!");
				return false;
			}
			
			TeleportHandler.teleport(sender, (Player) sender, p.getLocation());
			return true;
		} else {
			sender.sendMessage("Only players can use this command!");
			return false;
		}
	}
}
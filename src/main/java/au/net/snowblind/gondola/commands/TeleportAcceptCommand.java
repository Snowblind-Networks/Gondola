package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.TeleportHandler;

public class TeleportAcceptCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 0) {
				return false;
			}
			Player p = Gondola.teleports.get((Player) sender);
			
			if (p == null) {
				sender.sendMessage("You don't have any teleport requests!");
				return true;
			}
			
			sender.sendMessage("Teleport request accepted. Teleporting...");
			TeleportHandler.teleport(p, p, ((Player) sender).getLocation());
			Gondola.teleports.remove((Player) sender);
		} else {
			sender.sendMessage("Only players can use this command!");
		}
		return true;
	}
}
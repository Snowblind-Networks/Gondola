package au.net.snowblind.gondola.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.TeleportHandler;

public class SpawnCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 0) {
				return false;
			} else {
				Location spawn = Gondola.plugin.getConfig().getLocation("spawn.point");
				TeleportHandler.teleport(sender, (Player) sender, spawn);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}
		

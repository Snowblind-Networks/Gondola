package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class SetSpawnCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 0) {
				return false;
			} else {
				Gondola.plugin.getConfig().set("spawn.point", ((Player) sender).getLocation());
				sender.sendMessage("Spawn point set.");
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}
		

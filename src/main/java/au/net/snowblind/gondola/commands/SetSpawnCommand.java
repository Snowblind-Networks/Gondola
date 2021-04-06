package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class SetSpawnCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (!Gondola.vault.permission.has(sender, "gondola.setspawn")) {
				sender.sendMessage(ChatHandler.error("You don't have permission to run this command."));
				return true;
			}
			if (args.length != 0) {
				return false;
			} else {
				Gondola.plugin.getConfig().set("spawn.point", ((Player) sender).getLocation());
				sender.sendMessage(ChatHandler.info("Spawn point set."));
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}
		

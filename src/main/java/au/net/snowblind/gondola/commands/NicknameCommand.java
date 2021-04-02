package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class NicknameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else {
				Gondola.jedis.hset("user:" + ((Player)sender).getUniqueId().toString(), "nickname", args[0]);
				((Player) sender).setDisplayName(args[0]);
				sender.sendMessage("Nickname set to " + args[0]);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

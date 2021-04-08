package au.net.snowblind.gondola.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class RealnameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 1) {
			return false;
		} else {
			for (Player p : Gondola.plugin.getServer().getOnlinePlayers()) {
				if (Gondola.jedis.hget("user:" + p.getUniqueId().toString(), "nickname").equals(args[0])) {
					sender.sendMessage(ChatHandler.info("The real name of " + args[0] + " is " + p.getName()));
					return true;
				}
			}
			sender.sendMessage("There is no player with nickname " + args[0] + ".");
		}
		return true;
	}
}

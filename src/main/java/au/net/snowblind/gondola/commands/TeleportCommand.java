package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class TeleportCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			}
			Player p = Gondola.plugin.getServer().getPlayer(args[0]);
			
			if (p == null) {
				sender.sendMessage(ChatHandler.error("Player " + args[0] + " not found."));
				return false;
			}
			Gondola.teleports.put(p, (Player) sender);
			sender.sendMessage(ChatHandler.info("Sent teleport request to " + p.getDisplayName()));
			p.sendMessage(ChatHandler.info(((Player) sender).getDisplayName() +
					" has requested to teleport to you. Type /tpaccept to accept."));
			return true;
		} else {
			sender.sendMessage("Only players can use this command!");
			return false;
		}
	}
}
package au.net.snowblind.gondola.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import au.net.snowblind.gondola.handlers.HomeHandler;

public class SethomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length > 1) {
				return false;
			} else if (args.length == 0) {
				sender.sendMessage(ChatHandler.info("Home set."));
				HomeHandler.setHome((Player) sender, "default");
			} else {
				Location loc = ((Player) sender).getLocation();
				String clanId = Gondola.clans.getInfluence(loc);
				if (clanId != null && !clanId.equals(Gondola.clans.getMembership((Player) sender))) {
					sender.sendMessage(ChatHandler.error("You cannot set a home here, this area is under the influence of clan " +
							Gondola.clans.getColor(clanId) + Gondola.clans.getName(clanId)));
				} else {
					sender.sendMessage(ChatHandler.info("Home " + args[0] + " set."));
					HomeHandler.setHome((Player) sender, args[0]);
				}
			}
			return true;
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

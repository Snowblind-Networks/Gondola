package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import redis.clients.jedis.Tuple;

public class ListClansCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 0) {
				return false;
			} else {
				// Display clans sorted by top score
				String clans = "Clans: \n";
				for (Tuple clan : Gondola.clans.getClansSorted()) {
					clans = clans + Gondola.clans.getName(clan.getElement()) + " (" + Math.round(clan.getScore()) + "), ";
				}
				sender.sendMessage(ChatHandler.info(clans.substring(0, clans.length() - 2)));
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

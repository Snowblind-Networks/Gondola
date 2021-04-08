package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class ClanLeaveCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length > 0) {
				return false;
			} else {
				String clan = Gondola.clans.getMembership((Player) sender);
				if (clan == null) {
					sender.sendMessage(ChatHandler.error("You aren't in a clan."));
					return true;
				}
				
				if (Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("You must either choose a new clan owner or delete the clan."));
				} else {
					Gondola.clans.removeFromClan(clan, (Player) sender);
					sender.sendMessage(ChatHandler.warn("You have left your clan."));
				}
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

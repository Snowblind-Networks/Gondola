package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import net.md_5.bungee.api.ChatColor;

public class ClanInfoCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 1) {
			return false;
		} else if (args.length == 0) {
			String clan = Gondola.clans.getMembership((Player) sender);
			if (clan == null) {
				return false;
			} else {
				sender.sendMessage(clanInfo(clan));
				return true;
			}
		} else {
			String clan = Gondola.clans.contains(args[0]) ? args[0] : null;
			if (clan == null) {
				sender.sendMessage(ChatHandler.error("Clan does not exist!"));
				return true;
			}
			
			sender.sendMessage(clanInfo(clan));
			return true;
		}	
	}
	
	private static String clanInfo(String clan) {
		String clanId = Gondola.clans.getClans().get(clan);
		String message = "";
		message += Gondola.clans.getColour(clanId) + clan + ChatColor.RESET + "\n";
		message += "Owner: " + Gondola.clans.getOwnerName(clanId) + "\n";
		message += "Officers: " + String.join(", ", Gondola.clans.getOfficerNames(clanId)) + "\n";
		message += "Members: " + String.join(", ", Gondola.clans.getMemberNames(clanId)) + "\n";
		return message;
	}
}

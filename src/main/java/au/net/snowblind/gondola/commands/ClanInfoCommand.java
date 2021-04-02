package au.net.snowblind.gondola.commands;

import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
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
				sender.sendMessage("Clan does not exist!");
				return true;
			}
			
			sender.sendMessage(clanInfo(clan));
			return true;
		}	
	}
	
	private static String clanInfo(String clan) {
		String message = "";
		message += Gondola.clans.getColour(clan) + clan + ChatColor.RESET + "\n";
		message += "Owner: " + Gondola.clans.getOwner(clan).getName() + "\n";
		message += "Officers: " + String.join(", ",
				Gondola.clans.getOfficers(clan).stream()
				.map(player -> player.getName())
				.collect(Collectors.toList())) + "\n";
		message += "Members: " + String.join(", ",
				Gondola.clans.getMembers(clan).stream()
				.map(player -> player.getName())
				.collect(Collectors.toList())) + "\n";
		return message;
	}
}

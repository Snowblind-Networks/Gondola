package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class SetClanScoreCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0 || args.length > 2) {
			return false;
		} else {
			String clan = Gondola.clans.getClans().get(args[0]);
			if (clan == null) {
				sender.sendMessage(ChatHandler.error("No such clan: " + args[0] + "."));
				return true;
			}
			
			long curpoints = Gondola.clans.getPoints(clan);
			long points;
			
			try {
				points = Long.valueOf(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("Invalid score: " + args[1] + ".");
				return false;
			}
			
			Gondola.clans.addPoints(clan, points - curpoints);
			sender.sendMessage(ChatHandler.info("Clan points set to " + args[1] + "."));
		}
		return true;
	}
}

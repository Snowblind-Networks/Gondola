package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;


public class DeleteClanCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 0) {
				return false;
			} else {
				String clan = Gondola.clans.getMembership((Player) sender);
				if (clan == null) {
					sender.sendMessage("You aren't in a clan!");
					return true;
				}
				if (!(Gondola.clans.getPosition((Player)sender).equalsIgnoreCase("owner"))) {
					sender.sendMessage("Only the clan owner can delete the clan!");
					return true;
				}
				String name = Gondola.clans.getName(clan);
				Gondola.clans.deleteClan(clan);
				sender.sendMessage("Clan " + name + " deleted!");
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class PromoteCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				return false;
			} else {
				String clan = Gondola.clans.getMembership((Player) sender);
				if (clan == null) {
					sender.sendMessage("You aren't in a clan!");
					return true;
				}

				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[0])) == null) {
					sender.sendMessage("Can't find player " + args[0] + ".");
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage("Only the clan owner can promote members!");
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage("You aren't in the same clan as " + args[0] + ".");
				} else if (!Gondola.clans.getPosition(p).equalsIgnoreCase("member")) {
					sender.sendMessage("You can't promote " + args[0] + " further.");
				} else {
					Gondola.clans.deleteMember(clan, p);
					Gondola.clans.addOfficer(clan, p);
				}
				return true;
			}
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

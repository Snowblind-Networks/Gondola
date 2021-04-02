package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class ClanKickCommand implements CommandExecutor {
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
				} else if (Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("member")) {
					sender.sendMessage("Only the clan owner and officers can invite members.");
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage("You are not in the same clan as " + args[0] + ".");
				} else if (Gondola.clans.getPosition(p).equalsIgnoreCase(
						Gondola.clans.getPosition((Player) sender))) {
					sender.sendMessage("You don't have permission to kick " + args[0] + ".");
				} else {
					Gondola.clans.removeFromClan(clan, p);
					p.sendMessage("You have been kicked from your clan by "
							+ ((Player) sender).getDisplayName() + ".");
				}
				return true;
			}
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

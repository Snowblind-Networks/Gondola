package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class ClanInviteCommand implements CommandExecutor {
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
				} else if (((Player) sender).equals(p)) {
					sender.sendMessage("You can't invite yourself!");
				} else {
					Gondola.clans.invites.put(p, clan);
					p.sendMessage("You have just been invited to " + clan + " by "
							+ ((Player) sender).getDisplayName()
							+ ". Type /invaccept to accept.");
				}
				return true;
			}
		} else {
			sender.sendMessage("Only players can run this command.");
			return false;
		}
	}
}

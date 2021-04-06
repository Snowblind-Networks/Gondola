package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class InviteAcceptCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			String clan;
			if (args.length != 0) {
				return false;
			} else if (!Gondola.clans.invites.containsKey(p)) {
				sender.sendMessage(ChatHandler.error("You have not received any invites."));
			} else if (!Gondola.clans.contains((clan = Gondola.clans.invites.get(p)))) {
				sender.sendMessage(ChatHandler.error("That clan no longer exists."));
			} else {
				sender.sendMessage(ChatHandler.info("You have successfully joined clan " + clan + "."));
				Gondola.clans.addMember(clan, p);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

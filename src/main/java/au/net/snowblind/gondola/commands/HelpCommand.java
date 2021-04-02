package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 1) {
			return false;
		} else if ((args.length == 1) && (args[0].equalsIgnoreCase("clan"))) {
			String message = "Clan commands:\n";
			message += "/listclans - List existing clans\n";
			message += "/claninfo [clan] - Get info on a clan\n";
			message += "/createclan <name> - Create a new clan\n";
			message += "/deleteclan - Delete the clan you own\n";
			message += "/setclancolour <colour> - Set the clan's colour\n";
			message += "/setclanbanner - Set your clan's banner to the one in your hand\n";
			message += "/claninvite <player> - Invite player to your clan\n";
			message += "/acceptinvite - Accept the latest clan invite\n";
			message += "/clankick - Kick a player from your clan\n";
			sender.sendMessage(message);
		} else {
			String message = "Useful commands:\n";
			message += "/home <home> - Teleport to one of your homes\n";
			message += "/listhomes - List your homes\n";
			message += "/delhome <home> - Delete one of your homes\n";
			message += "/msg <player> - Send someone a private message\n";
			message += "/r - Reply to a private message\n";
			message += "/warp [warp] - Teleport to one of the warps\n";
			message += "/tp <player> - Request to teleport to player\n";
			message += "/tpaccept - Accept latest teleport request\n";
			message += "\n/help clan - List clan commands\n";
			sender.sendMessage(message);
		}
		return true;
	}
}

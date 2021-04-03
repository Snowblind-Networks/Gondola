package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String message;
		if (args.length > 1) {
			return false;
		} else if ((args.length == 1) && (args[0].equalsIgnoreCase("clan"))) {
			message = ChatColor.BOLD + "Clan commands" + ChatColor.RESET + ":\n";
			message += entry("/listclans", "", "List existing clans");
			message += entry("/claninfo", "[clan]", "Get info on a clan");
			message += entry("/createclan", "<name>", "Create a new clan");
			message += entry("/deleteclan", "", "Delete your own clan");
			message += entry("/clanhome", "", "Teleport to your clan's home");
			message += entry("/clan", "", "Clan admin commands");
		} else if ((args.length == 1) && (args[0].equalsIgnoreCase("teleport"))) {
			message = ChatColor.BOLD + "Teleportation commands" + ChatColor.RESET + ":\n";
			message += entry("/spawn", "", "Teleport to spawn");
			message += entry("/home", "[home]", "Teleport to one of your homes");
			message += entry("/listhomes", "", "List your homes");
			message += entry("/delhome", "<home>", "Delete one of your homes");
			message += entry("/warp", "[warp]", "Teleport to one of the warps");
			message += entry("/tp", "<player>", "Request to teleport to player");
		} else if ((args.length == 1) && (args[0].equalsIgnoreCase("economy"))) {
			message = ChatColor.BOLD + "Economy commands" + ChatColor.RESET + ":\n";
			message += entry("/balance", "[player]", "Check your current balance");
			message += entry("/pay", "<player> <amount>", "Pay another player");
			message += entry("/balancetop", "", "See the balance leaderboard");
		} else if ((args.length == 1) && (args[0].equalsIgnoreCase("chat"))) {
			message = ChatColor.BOLD + "Chat commands" + ChatColor.RESET + ":\n";
			message += entry("/msg", "<player>", "Send someone a private message");
			message += entry("/r", "", "Reply to a private message");
			message += entry("/nickname", "", "Set your nickname");
		} else {
			message = ChatColor.BOLD + "Useful commands" + ChatColor.RESET + ":\n";
			message += entry("/spawn", "", "Teleport to spawn");
			message += entry("/home", "<home>", "Teleport to one of your homes");
			message += entry("/msg", "<player>", "Send someone a private message");
			message += entry("/tp", "<player>", "Request to teleport to player");
			message += entry("/help", "clan", "List clan commands");
			message += entry("/help", "teleport", "List teleporation commands");
			message += entry("/help", "chat", "List chat commands");
			message += entry("/help", "economy", "List economy commands");
		}
		sender.sendMessage(message);
		return true;
	}
	
	public static String entry(String command, String args, String description) {
		if (args != "") args = " " + args;
		return ChatColor.BLUE + command + ChatColor.GRAY + args +
				ChatColor.WHITE + " - " + description + "\n";
	}
}
package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import au.net.snowblind.gondola.Gondola;
import net.md_5.bungee.api.ChatColor;

public class RulesCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String message = ChatColor.BOLD + "Rules" + ChatColor.RESET + ":\n";
		message += "Server rule: Do not post anything illegal under US or Australian law\n";
		message += "Winning clan rule: " + Gondola.clans.getTopClanRule() + "\n";
		sender.sendMessage(message);
		return true;
	}
}

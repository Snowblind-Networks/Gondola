package au.net.snowblind.gondola.commands;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import net.md_5.bungee.api.ChatColor;

public class ClanChatCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			String clan = Gondola.clans.getMembership((Player) sender);
			
			if (clan == null) {
				sender.sendMessage(ChatHandler.error("You aren't in a clan."));
				return true;
			}
			
			ChatColor clanColor = Gondola.clans.getColor(clan);
			String name = Gondola.clans.getName(clan);
			
			if (args.length == 0)
				return false;
			
			String msg = ChatHandler.processMessage(String.join(" ", Arrays.copyOfRange(args, 0, args.length))); 
			msg = clanColor + "[" + name + "] " + ChatColor.WHITE + ((Player) sender).getDisplayName() + clanColor + ": " + msg;
			Set<String> players = Gondola.clans.getAllPlayers(clan);
			for (String uuid : players) {
				Player p = Gondola.plugin.getServer().getPlayer(UUID.fromString(uuid));
				if (p != null) p.sendMessage(msg);
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

package au.net.snowblind.gondola.handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class ChatHandler {
	public static String getFormat(Player p) {
		String format = "%1$s" + ChatColor.RESET + "≫ %2$s";
		String group = Gondola.vault.permission.getPrimaryGroup(p);
		
		// Set name colour
		if (group.equalsIgnoreCase("admin")) {
			format = ChatColor.DARK_RED + format;
		} else if (group.equalsIgnoreCase("vip")) {
			format = ChatColor.AQUA + format;
		} else {
			format = ChatColor.GRAY + format;
		}
		
		// Set clan tag and separator (. for members, * for officers, ** for master)
		String clan;
		if ((clan = Gondola.clans.getMembership(p)) != null) {
			switch (Gondola.clans.getPosition(p)) {
				case "owner":
					format = "**" + format;
					break;
				case "officer":
					format = "*" + format;
					break;
				case "member":
					format = "." + format;
					break;
				default:
					Gondola.plugin.getLogger().warning("Player data configuration broken! Player " + p.getName() + " has a nonexistent position in its clan!");
			}
			
			format = Gondola.clans.getColour(clan) + clan + ChatColor.DARK_GRAY + format;
		}
		
		return format;
	}
	
	public static String processMessage(String base) {
		String msg = base;
		if(msg.startsWith(">")) {
			msg = ChatColor.GREEN + msg;
		} else if (msg.startsWith("<")) {
			msg = ChatColor.RED + msg;
		}
		
		return msg;
	}
}

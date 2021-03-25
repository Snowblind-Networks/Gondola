package au.net.snowblind.gondola.handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import au.net.snowblind.gondola.Gondola;

public class ChatHandler {
	public static String getFormat(Player p) {
		String format = "%1$s" + ChatColor.RESET + "≫ %2$s";
		String group = Gondola.plugin.vault.permission.getPrimaryGroup(p);
		
		// Set name colour
		if (group.equalsIgnoreCase("admin")) {
			format = ChatColor.DARK_RED + format;
		} else if (group.equalsIgnoreCase("vip")) {
			format = ChatColor.AQUA + format;
		}
		
		// TODO: Set clan tag and separator (. for members, * for officers, ** for master)
		
		return format;
	}
}

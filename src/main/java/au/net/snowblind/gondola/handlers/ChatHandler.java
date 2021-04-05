package au.net.snowblind.gondola.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
import java.util.regex.Matcher;
import java.util.regex.Pattern;
*/

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
		String clanId;
		if ((clanId = Gondola.clans.getMembership(p)) != null) {
			String clan = Gondola.clans.getName(clanId);
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
					Gondola.plugin.getLogger().warning("Player data broken! Player " + p.getName() + " has a nonexistent position in its clan!");
			}
			
			format = Gondola.clans.getColour(clanId) + clan + ChatColor.DARK_GRAY + format;
		}
		
		return format;
	}
	
	// Pattern to match *bold*.
	public static Pattern p = Pattern.compile("\\*[^*]*\\*");
	
	public static String processMessage(String base) {
		String msg = base;
		
		// >Greentext and <redtext
		if(msg.startsWith(">")) {
			msg = ChatColor.GREEN + msg;
		} else if (msg.startsWith("<")) {
			msg = ChatColor.RED + msg;
		}
		
		// *BOLD*
		Matcher m = p.matcher(msg);
		while (m.find()) {
			int i = m.start();
			String format = ChatColor.getLastColors(msg.substring(0, i));
			String newFormat = format + ChatColor.BOLD;
			msg = msg.substring(0, i) + newFormat + msg.substring(m.start() + 1, m.end() - 1) +
					ChatColor.RESET + format + msg.substring(m.end());
			m = p.matcher(msg);
		}
		
		return msg;
	}
	
	public static String error(String msg) {
		return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + ChatColor.BOLD + "!!"
				+ ChatColor.RESET + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + msg;
	}

	public static String warn(String msg) {
		return ChatColor.GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "!"
				+ ChatColor.RESET + ChatColor.GRAY + "] " + ChatColor.RED + msg;
	}

	public static String info(String msg) {
		return "[" + ChatColor.GREEN + ChatColor.BOLD + "*" + ChatColor.RESET + "] " + ChatColor.GREEN + msg;
	}
}
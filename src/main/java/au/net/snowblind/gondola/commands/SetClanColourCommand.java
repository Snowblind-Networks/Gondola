package au.net.snowblind.gondola.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.DyeColor;

import au.net.snowblind.gondola.Gondola;


public class SetClanColourCommand implements CommandExecutor {
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
				if (!(Gondola.clans.getPosition((Player)sender).equalsIgnoreCase("owner"))) {
					sender.sendMessage("Only the clan owner can change the clan colour!");
					return true;
				}
				
				DyeColor colour;
				try {
					colour = DyeColor.valueOf(args[0]);
				} catch (Exception e) {
					sender.sendMessage("Invalid colour " + args[0] + "!");
					String msg = "Valid colours are:";
					for (DyeColor c : DyeColor.values())
						msg = msg + " " + c.toString();
					
					sender.sendMessage(msg);
					return true;
				}
				
				Gondola.clans.setColour(clan, colour);
				sender.sendMessage("Clan colour set!");
			}
			
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

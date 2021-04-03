package au.net.snowblind.gondola.commands;

import org.bukkit.DyeColor;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import net.md_5.bungee.api.ChatColor;

public class ClanCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			String clan = Gondola.clans.getMembership((Player) sender);
			if (clan == null) {
				sender.sendMessage(ChatHandler.error("You aren't in a clan!"));
				return true;
			}
			
			if (args.length > 2) {
				return false;
			} else if (args.length == 0) {
				sender.sendMessage(usage());
			} else if (args[0].equalsIgnoreCase("sethome")) {
				Gondola.clans.setHome(clan, ((Player)sender).getLocation());
				sender.sendMessage(ChatHandler.info("Clan home set to your current location."));
			} else if (args[0].equalsIgnoreCase("setcolour")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan setcolour <colour>"));
					return true;
				}
				
				DyeColor colour;
				try {
					colour = DyeColor.valueOf(args[1]);
				} catch (Exception e) {
					sender.sendMessage(ChatHandler.error("Invalid colour: " + args[1] + "."));
					String msg = "Valid colours are:";
					for (DyeColor c : DyeColor.values())
						msg = msg + " " + c.toString();
					
					sender.sendMessage(msg);
					return true;
				}
				
				Gondola.clans.setColour(clan, colour);
				sender.sendMessage(ChatHandler.info("Clan colour set to " + args[1] + "."));
			} else if (args[0].equalsIgnoreCase("setbanner")) {
				if (Tag.BANNERS.isTagged(((Player)sender).getInventory().getItemInMainHand().getType())) {
					ItemStack banner = ((Player)sender).getInventory().getItemInMainHand();
					BannerMeta meta = (BannerMeta) banner.getItemMeta();
				
					Gondola.clans.setBanner(Gondola.clans.getMembership((Player)sender), meta, banner.getType());
					sender.sendMessage(ChatHandler.info("Clan banner set."));
				} else {
					sender.sendMessage(ChatHandler.warn("Hold a banner in your hand and run this command again!"));
				}
			} else if (args[0].equalsIgnoreCase("invite")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan invite <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("member")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner and officers can invite members."));
				} else if (((Player) sender).equals(p)) {
					sender.sendMessage(ChatHandler.error("You can't invite yourself!"));
				} else {
					Gondola.clans.invites.put(p, clan);
					p.sendMessage(ChatHandler.info("You have just been invited to " + clan + " by "
							+ ((Player) sender).getDisplayName()
							+ ". Type /acceptinvite to accept."));
				}
			} else if (args[0].equalsIgnoreCase("kick")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan kick <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("member")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner and officers can invite members."));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You are not in the same clan as " + args[1] + "."));
				} else if (Gondola.clans.getPosition(p).equalsIgnoreCase(
						Gondola.clans.getPosition((Player) sender))) {
					sender.sendMessage(ChatHandler.error("You don't have permission to kick " + args[1] + "."));
				} else {
					Gondola.clans.removeFromClan(clan, p);
					p.sendMessage(ChatHandler.warn("You have been kicked from your clan by "
							+ ((Player) sender).getDisplayName() + "."));
				}
			} else if (args[0].equalsIgnoreCase("promote")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan kick <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can promote members!"));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You aren't in the same clan as " + args[1] + "."));
				} else if (!Gondola.clans.getPosition(p).equalsIgnoreCase("member")) {
					sender.sendMessage(ChatHandler.error("You can't promote " + args[1] + " further."));
				} else {
					Gondola.clans.deleteMember(clan, p);
					Gondola.clans.addOfficer(clan, p);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("demote")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan kick <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can demote members!"));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You aren't in the same clan as " + args[1] + "."));
				} else if (!Gondola.clans.getPosition(p).equalsIgnoreCase("officer")) {
					sender.sendMessage(ChatHandler.error("You can't demote " + args[1] + " further."));
				} else {
					Gondola.clans.deleteOfficer(clan, p);
					Gondola.clans.addMember(clan, p);
				}
			} else if (args[0].equalsIgnoreCase("delete")) {
				if (!(Gondola.clans.getPosition((Player)sender).equalsIgnoreCase("owner"))) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can delete the clan!"));
					return true;
				}
				String name = Gondola.clans.getName(clan);
				Gondola.clans.deleteClan(clan);
				sender.sendMessage(ChatHandler.warn("Clan " + name + " deleted!"));
			} else {
				sender.sendMessage(usage());
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
	
	private String usage() {
		String message = ChatColor.BOLD + "Clan admin commands" + ChatColor.RESET + ":\n";
		message += HelpCommand.entry("/clan", "sethome", "Set your clan's home");
		message += HelpCommand.entry("/clan", "setcolour <colour>", "Set the clan's colour");
		message += HelpCommand.entry("/clan", "setbanner", "Set your clan's banner to the one in your hand");
		message += HelpCommand.entry("/clan", "invite <player>", "Invite player to your clan");
		message += HelpCommand.entry("/clan", "kick <player>", "Kick a player from your clan");
		message += HelpCommand.entry("/clan", "promote <player>", "Promote a player in your clan");
		message += HelpCommand.entry("/clan", "demote <player>", "Demote a player in your clan");
		message += HelpCommand.entry("/clan", "delete", "Delete your clan");
		return message;
	}
}
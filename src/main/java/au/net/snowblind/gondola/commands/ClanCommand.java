package au.net.snowblind.gondola.commands;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBanner;
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
				sender.sendMessage(ChatHandler.error("You aren't in a clan."));
				return true;
			}
			
			if (args.length == 0) {
				sender.sendMessage(usage());
			} else if (args[0].equalsIgnoreCase("sethome")) {
				// Remove old home
				Location home = Gondola.clans.getHome(clan);
				if (home != null) {
					Gondola.flags.remove(home.getBlock());
					home.getBlock().setType(Material.AIR);
				}
				
				Location loc = ((Player) sender).getLocation();
				Gondola.clans.setHome(clan, loc);
				sender.sendMessage(ChatHandler.info("Clan home set to your current location."));
				
				// Put the clan's banner up at their clan home
				BannerMeta meta = Gondola.clans.getBanner(clan);
				Block b = loc.getBlock();
				CraftBanner banner;
				
				if (Tag.BANNERS.isTagged(b.getType()) && !b.getType().toString().contains("WALL")) {
					banner = new CraftBanner(b);
					String color = Gondola.clans.getBannerType(clan).toString().replaceFirst("_BANNER$", "").toLowerCase();
					BlockData bd = Gondola.plugin.getServer().createBlockData(banner.getBlockData().getAsString()
							.replaceAll(":(.*)(?=_banner)", ":" + color.toLowerCase()));
					banner.setBlockData(bd);
				} else {
					b.setType(Gondola.clans.getBannerType(clan));
					banner = new CraftBanner(b);
				}
				
				if (meta != null)
					banner.setPatterns(meta.getPatterns());
				else
					banner.setPatterns(new ArrayList<Pattern>());
				
				banner.update(true);
				
				Gondola.flags.put(b, clan);
			} else if (args[0].equalsIgnoreCase("setcolor")) {
				if (args.length > 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan setcolor <color>"));
					return true;
				}
				
				DyeColor color;
				try {
					color = DyeColor.valueOf(args[1]);
				} catch (Exception e) {
					String msg = "Valid colors are:";
					for (DyeColor c : DyeColor.values())
						msg = msg + " " + c.toString();
					
					sender.sendMessage(ChatHandler.error(msg));
					return true;
				}
				
				Gondola.clans.setColor(clan, color);
				sender.sendMessage(ChatHandler.info("Clan color set to " + args[1] + "."));
			} else if (args[0].equalsIgnoreCase("setbanner")) {
				if (Tag.BANNERS.isTagged(((Player)sender).getInventory().getItemInMainHand().getType())) {
					ItemStack banner = ((Player)sender).getInventory().getItemInMainHand();
					BannerMeta meta = (BannerMeta) banner.getItemMeta();
				
					Gondola.clans.setBanner(Gondola.clans.getMembership((Player)sender), meta, banner.getType());
					sender.sendMessage(ChatHandler.info("Clan banner set."));
				} else {
					sender.sendMessage(ChatHandler.warn("Hold a banner in your hand and run this command again."));
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
					sender.sendMessage(ChatHandler.error("You can't invite yourself."));
				} else {
					Gondola.clans.invites.put(p, clan);
					((Player) sender).sendMessage(ChatHandler.info("You have invited " + p.getDisplayName() + " to your clan."));
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
					((Player) sender).sendMessage(ChatHandler.info("You have kicked " + p.getDisplayName() + " to your clan."));
					p.sendMessage(ChatHandler.warn("You have been kicked from your clan by "
							+ ((Player) sender).getDisplayName() + "."));
				}
			} else if (args[0].equalsIgnoreCase("setowner")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan promote <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can choose a new one."));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You aren't in the same clan as " + args[1] + "."));
				} else {
					((Player) sender).sendMessage(ChatHandler.info("You have made " + p.getDisplayName() + " the clan owner."));
					p.sendMessage(ChatHandler.info("You have just been make the clan owner by "
							+ ((Player) sender).getDisplayName() + "."));
					Gondola.clans.setOwner(clan, p);
					Gondola.clans.addOfficer(clan, (Player) sender);
				}
			} else if (args[0].equalsIgnoreCase("promote")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan promote <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can promote members."));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You aren't in the same clan as " + args[1] + "."));
				} else if (!Gondola.clans.getPosition(p).equalsIgnoreCase("member")) {
					sender.sendMessage(ChatHandler.error("You can't promote " + args[1] + " further."));
				} else {
					((Player) sender).sendMessage(ChatHandler.info("You have promoted " + p.getDisplayName() + " to clan officer."));
					p.sendMessage(ChatHandler.info("You have just been promoted to clan officer by "
							+ ((Player) sender).getDisplayName() + "."));
					Gondola.clans.deleteMember(clan, p);
					Gondola.clans.addOfficer(clan, p);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("demote")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan demote <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can demote members."));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You aren't in the same clan as " + args[1] + "."));
				} else if (!Gondola.clans.getPosition(p).equalsIgnoreCase("officer")) {
					sender.sendMessage(ChatHandler.error("You can't demote " + args[1] + " further."));
				} else {
					((Player) sender).sendMessage(ChatHandler.info("You have demoted " + p.getDisplayName() + " to clan member."));
					p.sendMessage(ChatHandler.info("You have just been demoted to clan member by "
							+ ((Player) sender).getDisplayName() + "."));
					Gondola.clans.deleteOfficer(clan, p);
					Gondola.clans.addMember(clan, p);
				}
			} else if (args[0].equalsIgnoreCase("delete")) {
				if (!(Gondola.clans.getPosition((Player)sender).equalsIgnoreCase("owner"))) {
					sender.sendMessage(ChatHandler.error("Only the clan owner can delete the clan."));
					return true;
				}
				String name = Gondola.clans.getName(clan);
				Gondola.clans.deleteClan(clan);
				sender.sendMessage(ChatHandler.warn("Clan " + name + " deleted."));
			} else if (args[0].equalsIgnoreCase("changeowner")) {
				if (args.length != 2) {
					sender.sendMessage(ChatHandler.warn("Usage: /clan changeowner <player>"));
					return true;
				}
				
				Player p;
				if ((p = Gondola.plugin.getServer().getPlayer(args[1])) == null) {
					sender.sendMessage(ChatHandler.error("Can't find player " + args[1] + "."));
				} else if (!Gondola.clans.getPosition((Player) sender).equalsIgnoreCase("owner")) {
					sender.sendMessage(ChatHandler.error("Only the clan owner designate the new clan owner."));
				} else if (!Gondola.clans.getMembership(p).equalsIgnoreCase(clan)) {
					sender.sendMessage(ChatHandler.error("You aren't in the same clan as " + args[1] + "."));
				} else {
					sender.sendMessage(ChatHandler.info("Clan owner set to " + args[1] + "."));
					p.sendMessage(ChatHandler.info("You are the new owner of your clan."));
					Gondola.clans.removeFromClan(clan, p);
					Gondola.clans.setOwner(clan, p);
					Gondola.clans.addOfficer(clan, ((Player) sender));
				}
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
		message += HelpCommand.entry("/clan", "setcolor <color>", "Set the clan's color");
		message += HelpCommand.entry("/clan", "setbanner", "Set your clan's banner to the one in your hand");
		message += HelpCommand.entry("/clan", "invite <player>", "Invite player to your clan");
		message += HelpCommand.entry("/clan", "kick <player>", "Kick a player from your clan");
		message += HelpCommand.entry("/clan", "setowner <player>", "Set the owner of the clan");
		message += HelpCommand.entry("/clan", "promote <player>", "Promote a player in your clan");
		message += HelpCommand.entry("/clan", "demote <player>", "Demote a player in your clan");
		message += HelpCommand.entry("/clan", "delete", "Delete your clan");
		return message;
	}
}
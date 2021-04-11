package au.net.snowblind.gondola.commands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBanner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;
import au.net.snowblind.gondola.handlers.TeleportHandler;

public class ClanHomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length > 0) {
				return false;
			} else {
				String clan = Gondola.clans.getMembership((Player) sender);
				if (clan == null) {
					sender.sendMessage(ChatHandler.error("You aren't in a clan."));
					return true;
				}
				
				Location home = Gondola.clans.getHome(clan);
				
				// Put the clan's banner up at their clan home
				BannerMeta meta = Gondola.clans.getBanner(clan);
				Block b = home.getBlock();
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
				
				TeleportHandler.teleport(sender, (Player) sender, Gondola.clans.getHome(clan));
				sender.sendMessage(ChatHandler.info("Teleporting to clan home."));
			}
		} else {
			sender.sendMessage("Only players can run this command.");
		}
		return true;
	}
}

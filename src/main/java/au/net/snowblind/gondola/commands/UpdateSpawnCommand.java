package au.net.snowblind.gondola.commands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBanner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.BoundingBox;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class UpdateSpawnCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (!Gondola.vault.permission.has(sender, "gondola.updatespawn")) {
				sender.sendMessage(ChatHandler.error("You don't have permission to run this command."));
				return true;
			}
			ConfigurationSection cs = Gondola.plugin.getConfig().getConfigurationSection("spawn");
			Location min = cs.getLocation("min");
			Location max = cs.getLocation("max");
			BoundingBox bb = BoundingBox.of(min, max);
			
			String clan = Gondola.clans.getMembership((Player)sender);
			BannerMeta meta = Gondola.clans.getBanner(clan);
			String colour = Gondola.clans.getBannerType(clan).toString().replaceFirst("_BANNER$", "").toLowerCase();
			
			for (int x = (int) bb.getMinX(); x < bb.getMaxX(); x++) {
				for (int y = (int) bb.getMinY(); y < bb.getMaxY(); y++) {
					for (int z = (int) bb.getMinZ(); z < bb.getMaxZ(); z++) {
						Block b = ((Player) sender).getWorld().getBlockAt(x, y, z);
						if (Tag.BANNERS.isTagged(b.getType())) {	
							if (isExcluded(x, y, z)) continue;
							CraftBanner banner = new CraftBanner(b);
							
							BlockData bd = Gondola.plugin.getServer().createBlockData(replaceBanners(banner.getBlockData().getAsString(), colour));
							banner.setBlockData(bd);
							
							if (meta != null)
								banner.setPatterns(meta.getPatterns());
							else
								banner.setPatterns(new ArrayList<Pattern>());
							
							banner.update(true);
						} else if (b.getType().toString().endsWith("_CONCRETE")) {
							String clanColour = Gondola.clans.getColourString(clan);
							b.setType(Material.valueOf(clanColour + "_CONCRETE"));
						} else {
							continue;
						}
					}
				}
			}
		} else {
			sender.sendMessage("This command can only be run by a player!");
		}
		return true;
	}
	
	private String replaceBanners(String msg, String colour) {
		String res = msg.replaceAll(":(.*)(_wall_banner)", ":" + colour.toLowerCase() + "_wall_FDFSASDF");
		res = res.replaceAll(":(.*)(?=_banner)", ":" + colour.toLowerCase());
		res = res.replaceAll("_wall_FDFSASDF", "_wall_banner");
		return res;
	}
	
	private boolean isExcluded(int x, int y, int z) {
		if ((Math.abs(x) <= 10 && Math.abs(z) >= 124 && Math.abs(z) <= 134) ||
				(Math.abs(z) <= 10 && Math.abs(x) >= 124 && Math.abs(x) <= 134)) return true;
		return false;
	}
}

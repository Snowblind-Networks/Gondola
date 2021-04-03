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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBanner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.BoundingBox;

import au.net.snowblind.gondola.Gondola;

public class SetbannerCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			ConfigurationSection cs = Gondola.plugin.getConfig().getConfigurationSection("spawn");
			Location min = cs.getLocation("min");
			Location max = cs.getLocation("max");
			BoundingBox bb = BoundingBox.of(min, max);
			
			String clan = Gondola.clans.getMembership((Player)sender);
			BannerMeta meta = Gondola.clans.getBanner(clan);
			String colour = Gondola.clans.getBannerType(clan).toString().replaceFirst("_.*$", "").toLowerCase();
			
			for (int x = (int) bb.getMinX(); x < bb.getMaxX(); x++) {
				for (int y = (int) bb.getMinY(); y < bb.getMaxY(); y++) {
					for (int z = (int) bb.getMinZ(); z < bb.getMaxZ(); z++) {
						Block bannBlock = ((Player) sender).getWorld().getBlockAt(x, y, z);
						if (!Tag.BANNERS.isTagged(bannBlock.getType()))
							continue;
						
						CraftBanner banner = new CraftBanner(bannBlock);
						BlockData bd = Gondola.plugin.getServer().createBlockData(banner.getBlockData().getAsString().replaceAll(":[a-z]*_", ":" + colour + "_"));
						banner.setBlockData(bd);
						
						if (meta != null)
							banner.setPatterns(meta.getPatterns());
						else
							banner.setPatterns(new ArrayList<Pattern>());
						
						banner.update(true);
					}
				}
			}
		} else {
			sender.sendMessage("This command can only be run by a player!");
		}
		return true;
	}
}

package au.net.snowblind.gondola.commands;

import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBanner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.BoundingBox;

import au.net.snowblind.gondola.Gondola;
import au.net.snowblind.gondola.handlers.ConfigHandler;

public class SetbannerCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("replace")) {
					Location min = ConfigHandler.getLocation(Gondola.plugin.getConfig().getConfigurationSection("spawn.min"));
					Location max = ConfigHandler.getLocation(Gondola.plugin.getConfig().getConfigurationSection("spawn.max"));
					BoundingBox bb = BoundingBox.of(min, max);
					
					for (int x = (int) bb.getMinX(); x < bb.getMaxX(); x++) {
						for (int y = (int) bb.getMinY(); y < bb.getMaxY(); y++) {
							for (int z = (int) bb.getMinZ(); z < bb.getMaxZ(); z++) {
								Block bannBlock = ((Player) sender).getWorld().getBlockAt(x, y, z);
								
								if (!Tag.BANNERS.isTagged(bannBlock.getType()))
									continue;
								
								CraftBanner banner = new CraftBanner(bannBlock);
								String clan = Gondola.clans.getMembership((Player)sender);
								
								BannerMeta meta = Gondola.clans.getBanner(clan);
								String colour = Gondola.clans.getBannerType(clan).toString().replaceFirst("_.*$", "").toLowerCase();
								
								BlockData bd = Gondola.plugin.getServer().createBlockData(banner.getBlockData().getAsString().replaceAll(":[a-z]*_", ":" + colour + "_"));
								banner.setBlockData(bd);
								banner.setPatterns(meta.getPatterns());
								banner.update(true);
							}
						}
					}
				}
			} else {
				if (Tag.BANNERS.isTagged(((Player)sender).getInventory().getItemInMainHand().getType())) {
					ItemStack banner = ((Player)sender).getInventory().getItemInMainHand();
					BannerMeta meta = (BannerMeta) banner.getItemMeta();
				
					Gondola.clans.setBanner(Gondola.clans.getMembership((Player)sender), meta, banner.getType());
				}
			}
		} else {
			sender.sendMessage("This command can only be run by a player!");
		}
		return true;
	}
}

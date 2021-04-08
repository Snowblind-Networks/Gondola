package au.net.snowblind.gondola;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class GondolaExpansion extends PlaceholderExpansion {
	@Override
    public boolean canRegister() {
        return true;
    }
	
	@Override
    public String getAuthor() {
        return "insyder1201";
    }
	
	@Override
	public String getIdentifier() {
		return "gondola";
	}
	
	@Override
	public String getVersion() {
		return "0.2";
	}
	
	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		if (identifier.contentEquals("nickname")) {
			return ((Player) player).getDisplayName();
		}
		return null;
	}
}

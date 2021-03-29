package au.net.snowblind.gondola;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import au.net.snowblind.gondola.commands.DelhomeCommand;
import au.net.snowblind.gondola.commands.DelwarpCommand;
import au.net.snowblind.gondola.commands.HomeCommand;
import au.net.snowblind.gondola.commands.ListhomesCommand;
import au.net.snowblind.gondola.commands.MessageCommand;
import au.net.snowblind.gondola.commands.SetbannerCommand;
import au.net.snowblind.gondola.commands.SethomeCommand;
import au.net.snowblind.gondola.commands.SetwarpCommand;
import au.net.snowblind.gondola.commands.TeleportCommand;
import au.net.snowblind.gondola.commands.WarpCommand;

public class Gondola extends JavaPlugin {
	public static Gondola plugin;
	public static HashMap<Player, PlayerData> players = new HashMap<Player, PlayerData>();
	public static VaultProviders vault;
	public static ClanData clans;
	
	@Override
	public void onEnable() {
		Gondola.plugin = this;
		vault = new VaultProviders();
		clans = new ClanData();
		saveDefaultConfig();
		Icons.loadIcons();
		MessageCommand.prevMessager = new HashMap<Player, Player>();
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		registerCommands();
	}
	
	@Override
	public void onDisable() {
		players.clear();
	}
	
	private void registerCommands() {
		getCommand("tp").setExecutor(new TeleportCommand());
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("r").setExecutor(new MessageCommand());
		getCommand("home").setExecutor(new HomeCommand());
		getCommand("sethome").setExecutor(new SethomeCommand());
		getCommand("listhomes").setExecutor(new ListhomesCommand());
		getCommand("delhome").setExecutor(new DelhomeCommand());
		getCommand("warp").setExecutor(new WarpCommand());
		getCommand("setwarp").setExecutor(new SetwarpCommand());
		getCommand("delwarp").setExecutor(new DelwarpCommand());
		getCommand("setbanner").setExecutor(new SetbannerCommand());
	}
}
package au.net.snowblind.gondola;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import au.net.snowblind.gondola.commands.MessageCommand;
import au.net.snowblind.gondola.commands.TeleportCommand;
import au.net.snowblind.gondola.handlers.ChatHandler;

public class Gondola extends JavaPlugin implements Listener {
	public static Gondola plugin;
	public static HashMap<Player, PlayerData> players = new HashMap<Player, PlayerData>();
	public static VaultProviders vault;
	
	@Override
	public void onEnable() {
		Gondola.plugin = this;
		vault = new VaultProviders();
		saveDefaultConfig();
		Icons.loadIcons();
		getServer().getPluginManager().registerEvents(this, this);
		registerCommands();
	}
	
	@Override
	public void onDisable() {
		players.clear();
	}
	
	private void registerCommands() {
		getCommand("tp").setExecutor(new TeleportCommand());
		getCommand("msg").setExecutor(new MessageCommand());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerData data = new PlayerData(p);
		players.put(p, data);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		players.get(e.getPlayer()).savePlayerData();
	}
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		Random rand = new Random();
		int index = rand.nextInt(Icons.icons.size());
		try {
			e.setServerIcon(Bukkit.loadServerIcon(Icons.icons.get(index)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		e.setFormat(ChatHandler.getFormat(e.getPlayer()));
	}
}
package au.net.snowblind.gondola;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import au.net.snowblind.gondola.commands.ClanCommand;
import au.net.snowblind.gondola.commands.ClanHomeCommand;
import au.net.snowblind.gondola.commands.ClanInfoCommand;
import au.net.snowblind.gondola.commands.CreateClanCommand;
import au.net.snowblind.gondola.commands.DelhomeCommand;
import au.net.snowblind.gondola.commands.DelwarpCommand;
import au.net.snowblind.gondola.commands.HelpCommand;
import au.net.snowblind.gondola.commands.HomeCommand;
import au.net.snowblind.gondola.commands.InviteAcceptCommand;
import au.net.snowblind.gondola.commands.ListClansCommand;
import au.net.snowblind.gondola.commands.MessageCommand;
import au.net.snowblind.gondola.commands.NicknameCommand;
import au.net.snowblind.gondola.commands.SetSpawnCommand;
import au.net.snowblind.gondola.commands.SetbannerCommand;
import au.net.snowblind.gondola.commands.SethomeCommand;
import au.net.snowblind.gondola.commands.SetwarpCommand;
import au.net.snowblind.gondola.commands.SpawnCommand;
import au.net.snowblind.gondola.commands.TeleportAcceptCommand;
import au.net.snowblind.gondola.commands.TeleportCommand;
import au.net.snowblind.gondola.commands.WarpCommand;
import redis.clients.jedis.Jedis;

public class Gondola extends JavaPlugin {
	public static Gondola plugin;
	public static VaultProviders vault;
	public static Clans clans;
	public static HashMap<Player, Player> teleports;
	public static Jedis jedis;
	
	@Override
	public void onEnable() {
		Gondola.plugin = this;
		vault = new VaultProviders();
		clans = new Clans();
		teleports = new HashMap<Player, Player>();
		jedis = new Jedis();
		saveDefaultConfig();
		Icons.loadIcons();
		MessageCommand.prevMessager = new HashMap<Player, Player>();
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		registerCommands();
	}
	
	@Override
	public void onDisable() {
		jedis.save();
	}
	
	private void registerCommands() {
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("setspawn").setExecutor(new SetSpawnCommand());
		getCommand("nickname").setExecutor(new NicknameCommand());
		getCommand("tp").setExecutor(new TeleportCommand());
		getCommand("tpaccept").setExecutor(new TeleportAcceptCommand());
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("r").setExecutor(new MessageCommand());
		getCommand("home").setExecutor(new HomeCommand());
		getCommand("sethome").setExecutor(new SethomeCommand());
		getCommand("delhome").setExecutor(new DelhomeCommand());
		getCommand("warp").setExecutor(new WarpCommand());
		getCommand("setwarp").setExecutor(new SetwarpCommand());
		getCommand("delwarp").setExecutor(new DelwarpCommand());
		getCommand("setbanners").setExecutor(new SetbannerCommand());
		getCommand("createclan").setExecutor(new CreateClanCommand());
		getCommand("clan").setExecutor(new ClanCommand());
		getCommand("clanhome").setExecutor(new ClanHomeCommand());
		getCommand("listclans").setExecutor(new ListClansCommand());
		getCommand("acceptinvite").setExecutor(new InviteAcceptCommand());
		getCommand("claninfo").setExecutor(new ClanInfoCommand());
	}
}
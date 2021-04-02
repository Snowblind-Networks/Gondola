package au.net.snowblind.gondola;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import au.net.snowblind.gondola.commands.ClanInfoCommand;
import au.net.snowblind.gondola.commands.ClanInviteCommand;
import au.net.snowblind.gondola.commands.ClanKickCommand;
import au.net.snowblind.gondola.commands.CreateClanCommand;
import au.net.snowblind.gondola.commands.DeleteClanCommand;
import au.net.snowblind.gondola.commands.DelhomeCommand;
import au.net.snowblind.gondola.commands.DelwarpCommand;
import au.net.snowblind.gondola.commands.HelpCommand;
import au.net.snowblind.gondola.commands.HomeCommand;
import au.net.snowblind.gondola.commands.InviteAcceptCommand;
import au.net.snowblind.gondola.commands.ListClansCommand;
import au.net.snowblind.gondola.commands.ListhomesCommand;
import au.net.snowblind.gondola.commands.MessageCommand;
import au.net.snowblind.gondola.commands.SetClanColourCommand;
import au.net.snowblind.gondola.commands.SetbannerCommand;
import au.net.snowblind.gondola.commands.SethomeCommand;
import au.net.snowblind.gondola.commands.SetwarpCommand;
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
		getCommand("tp").setExecutor(new TeleportCommand());
		getCommand("tpaccept").setExecutor(new TeleportAcceptCommand());
		getCommand("msg").setExecutor(new MessageCommand());
		getCommand("r").setExecutor(new MessageCommand());
		getCommand("home").setExecutor(new HomeCommand());
		getCommand("sethome").setExecutor(new SethomeCommand());
		getCommand("listhomes").setExecutor(new ListhomesCommand());
		getCommand("delhome").setExecutor(new DelhomeCommand());
		getCommand("warp").setExecutor(new WarpCommand());
		getCommand("setwarp").setExecutor(new SetwarpCommand());
		getCommand("delwarp").setExecutor(new DelwarpCommand());
		getCommand("setclanbanner").setExecutor(new SetbannerCommand());
		getCommand("createclan").setExecutor(new CreateClanCommand());
		getCommand("deleteclan").setExecutor(new DeleteClanCommand());
		getCommand("setclancolour").setExecutor(new SetClanColourCommand());
		getCommand("listclans").setExecutor(new ListClansCommand());
		getCommand("claninvite").setExecutor(new ClanInviteCommand());
		getCommand("acceptinvite").setExecutor(new InviteAcceptCommand());
		getCommand("clankick").setExecutor(new ClanKickCommand());
		getCommand("claninfo").setExecutor(new ClanInfoCommand());
	}
}
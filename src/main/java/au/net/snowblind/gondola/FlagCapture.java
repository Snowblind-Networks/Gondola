package au.net.snowblind.gondola;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class FlagCapture implements Runnable {
	//private final double MULTIPLIER = 0.001;
	private final double MULTIPLIER = 0.001;

	private String attackingClan, defendingClan;
	private double progress;
	private Set<Player> attackingPlayers, defendingPlayers;
	private Location flag;
	private BukkitTask task;
	private BossBar bossBar;
	
	public FlagCapture(String defendingClan, String attackingClan, Location flag) {
		this.defendingClan = defendingClan;
		this.attackingClan = attackingClan;
		this.attackingPlayers = Gondola.clans.getAllOnlinePlayers(attackingClan);
		this.defendingPlayers = Gondola.clans.getAllOnlinePlayers(defendingClan);
		this.progress = 1.00;
		this.flag = flag;
		
		bossBar = Gondola.plugin.getServer().createBossBar(
				Gondola.clans.getColor(attackingClan) + Gondola.clans.getName(attackingClan)
						+ ChatColor.RESET + " is capturing the flag of "
						+ Gondola.clans.getColor(defendingClan) + Gondola.clans.getName(defendingClan),
				BarColor.RED,
				BarStyle.SOLID);
		
		for (Player p : attackingPlayers)
			bossBar.addPlayer(p);
		for (Player p : defendingPlayers)
			bossBar.addPlayer(p);
	}
	
	public void setTask(BukkitTask task) {
		this.task = task;
	}
	
	public String getAttackingClan() {
		return this.attackingClan;
	}
	
	public String getDefendingClan() {
		return this.defendingClan;
	}
	
	public void addAttackingPlayer(Player p) {
		attackingPlayers.add(p);
		bossBar.addPlayer(p);
	}
	
	public void delAttackingPlayer(Player p) {
		attackingPlayers.remove(p);
		bossBar.removePlayer(p);
	}
	
	public void addDefendingPlayer(Player p) {
		defendingPlayers.add(p);
		bossBar.addPlayer(p);
	}
	
	public void delDefendingPlayer(Player p) {
		defendingPlayers.remove(p);
		bossBar.removePlayer(p);
	}

	
	private void end() {
		bossBar.removeAll();
		Gondola.captures.remove(defendingClan);
		task.cancel();
	}
	
	public void run() {		
		int attacking = 0, defending = 0;
		for (Player p : attackingPlayers) {
			if (flag.distance(p.getLocation()) < 5.0)
				attacking++;
		}
		
		for (Player p : defendingPlayers) {
			if (flag.distance(p.getLocation()) < 5.0)
				defending++;
		}
		
		// If nobody is attacking, then defense is 2x faster
		if (attacking == 0) defending *= 2;
		
		progress -= (attacking - defending) * MULTIPLIER;
		
		if (progress >= 1.0000000) {
			end();
		}
		if (progress <= 0.00000000) {
			end();
			
			long points = Math.round(Gondola.clans.getPoints(defendingClan) * 0.1);
			points = Gondola.clans.stealPoints(attackingClan, defendingClan, (int) points);
			
			Gondola.plugin.getServer().broadcastMessage(
					Gondola.clans.getColor(attackingClan) + Gondola.clans.getName(attackingClan)
					+ ChatColor.RESET + ChatColor.BOLD + " just captured "
					+ Gondola.clans.getColor(defendingClan) + Gondola.clans.getName(defendingClan)
					+ ChatColor.RESET + ChatColor.BOLD+ "'s flag and stole " + points + " points.");
		}
		
		bossBar.setProgress(Math.max(Math.min(progress, 1.000000000d), 0.00000000d));
	}
}

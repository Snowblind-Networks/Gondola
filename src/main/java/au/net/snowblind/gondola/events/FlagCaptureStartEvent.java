package au.net.snowblind.gondola.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlagCaptureStartEvent extends Event implements Cancellable {
	private String defendingClan, attackingClan;
	private boolean isCancelled;
	
	private static final HandlerList handlers = new HandlerList();
	
	
	@Override
	public boolean isCancelled() {
	    return this.isCancelled;
	}
	 
	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}
	
	
	public FlagCaptureStartEvent(String defendingClan, String attackingClan, Block flag) {
		this.defendingClan = defendingClan;
		this.attackingClan = attackingClan;
		this.isCancelled = false;
	}
	
	public String getDefendingClan() {
		return this.defendingClan;
	}
	
	public String getAttackingClan() {
		return this.attackingClan;
	}
	
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	
}

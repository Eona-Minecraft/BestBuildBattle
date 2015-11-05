package de.nuss9940.bbb;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Event implements Listener {
	
	public Event() {
		Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onTp(PlayerTeleportEvent e) {
		if (Game.running && !e.getCause().equals(TeleportCause.SPECTATE)) {
			if (e.getFrom().getWorld().getName().equals(Main.wname)) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (Game.spectate) {
			if (e.getBlock().getWorld().getName().equals(Main.wname)) e.setCancelled(true);
		} else if (Game.running) {
			if (e.getBlock().getWorld().getName().equals(Main.wname)) {
				if () {
					
				}
				
				
				
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (Game.spectate && e.getBlock().getWorld().getName().equals(Main.wname)) e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (Game.spectate && e.getItem().getWorld().getName().equals(Main.wname)) e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (Game.spectate && e.getPlayer().getWorld().getName().equals(Main.wname)) e.setCancelled(true);
	}
}

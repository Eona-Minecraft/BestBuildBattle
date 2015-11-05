package de.nuss9940.bbb;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Event implements Listener {
	
	public Event() {
		Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
	}
	
	@EventHandler
	public void onTp(PlayerTeleportEvent e) {
		if (Game.running) {
			if (e.getCause().equals(TeleportCause.SPECTATE)) {
				//TODO
			} else {
				if (e.getFrom().getWorld().getName().equals(Main.wname)) {
					e.setCancelled(true);
				}
			}
		}
		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (Game.spectate) {
			if (e.getBlock().getWorld().getName().equals(Main.wname)) e.setCancelled(true);
		} else if (Game.running) {
			if (e.getBlock().getWorld().getName().equals(Main.wname)) {
				if (!Game.arenas.get(e.getPlayer().getName()).inRegion(e.getBlock().getLocation())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			switch (e.getItem().getItemMeta().getDisplayName()) {
			case "&4GRAUENHAFT!!":
				Game.ranking.getScore(Game.showp).setScore(Game.ranking.getScore(Game.showp).getScore() + 1);
				break;
			case "&6*kotz*":
				Game.ranking.getScore(Game.showp).setScore(Game.ranking.getScore(Game.showp).getScore() + 2);
				break;
			case "&eok!":
				Game.ranking.getScore(Game.showp).setScore(Game.ranking.getScore(Game.showp).getScore() + 3);
				break;
			case "&aschön!":
				Game.ranking.getScore(Game.showp).setScore(Game.ranking.getScore(Game.showp).getScore() + 4);
				break;
			case "&2Wow!":
				Game.ranking.getScore(Game.showp).setScore(Game.ranking.getScore(Game.showp).getScore() + 5);
				break;
			default:
				break;
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
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (Game.players.contains(e.getPlayer().getName())) {
			Game.leave(e.getPlayer());
		}
	}
}

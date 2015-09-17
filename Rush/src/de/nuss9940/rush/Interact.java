package de.nuss9940.rush;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
//import me.dori99xd.OneEightLib.ActionBar;
import org.bukkit.inventory.ItemStack;

import de.nuss9940.lobby.Spawn;

public class Interact implements Listener {
	
	
	public Rush plugin;
	public Interact(Rush plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			
			if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				
				Sign sign = (Sign)e.getClickedBlock().getState();
				Player p = e.getPlayer();
				
				
				//join
				if (sign.getLine(1).endsWith("Rush")) {
					
					Rush.players.add(p.getDisplayName());
					p.setOp(false);
					if (Rush.tnt.size() < Rush.melone.size()) {
						Rush.tnt.add(p.getDisplayName());
						p.getInventory().setHelmet(new ItemStack(Material.TNT));
						p.updateInventory();
					} else {
						Rush.melone.add(p.getDisplayName());
						p.getInventory().setHelmet(new ItemStack(Material.MELON_BLOCK));
					}
					p.teleport(new Location(Rush.world, 0.5, 92, 0.5, 90, 0));
					//ActionBar.set(p, "&6Herzlichen &aGlückwunsch! &6Du hast den &a" + difficulty + " &6Jump and Run geschafft!");
					p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
					Handler.startCount();
				}
				
				//leave
				if (sign.getLine(2).endsWith("verlassen")) {
					
					if (e.getClickedBlock().getWorld() == Rush.world) {
						
						Rush.players.remove(p.getDisplayName());
						
						p.teleport(Spawn.spawn(p));
						//ActionBar.set(p, "&6Herzlichen &aGlückwunsch! &6Du hast den &a" + difficulty + " &6Jump and Run geschafft!");
						p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
						
					}
				}
			} else
			
			if (e.getClickedBlock().getType() == Material.TNT) {
				
				Player p = e.getPlayer();
				Rush.melone.remove(p.getDisplayName());
				Rush.tnt.add(p.getDisplayName());
				p.getInventory().setHelmet(new ItemStack(Material.TNT));
				p.updateInventory();
				
			} else
			
			if (e.getClickedBlock().getType() == Material.MELON_BLOCK) {
				
				Player p = e.getPlayer();
				Rush.tnt.remove(p.getDisplayName());
				Rush.melone.add(p.getDisplayName());
				p.getInventory().setHelmet(new ItemStack(Material.MELON_BLOCK));
				p.updateInventory();
				
			}
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if (e.getCurrentItem() == new ItemStack(Material.MELON_BLOCK) ||
				e.getCurrentItem() == new ItemStack(Material.TNT)) {
			
			e.setCancelled(true);
			
		}
		
	}
	
}

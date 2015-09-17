package de.nuss9940.rush;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class Handler implements Listener {
	
	
	public static Rush plugin;
	public Handler(Rush plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		Handler.plugin = plugin;
	}
	
	
	//Var
	static boolean setup = false;
	static int count = 20;
	static int countdown = 0;
	static int needed = 1;
	static int deaths = 0;
	static String first = "-";
	static String second = "-";
	static String third = "-";
	
	public static void startCount() {
		
		
		countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if (Rush.players.size() > needed && !setup) {
					
					if (count == 20 || count == 10 || count == 5 || count == 4 ||
											count == 3 || count == 2 || count == 1) {
						
						for (String pname : Rush.players) {
							
							Player p = Bukkit.getPlayer(pname);
							p.sendMessage("Start in "+ count + " Seconds");
							p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 10, 1);
							
						}
						
					}
					
					for (String pname : Rush.players) {
						Bukkit.getPlayer(pname).setLevel(count);
					}
					
					if (count == 0 && !setup) {
						
						startGame();
						Rush.running = true;
						count = 20;
						Bukkit.getScheduler().cancelTask(countdown);
						
					} else {
						count--;
					}
					
					
					
				} else {
					for (String pname : Rush.players) {
						Bukkit.getPlayer(pname).sendMessage(ChatColor.GREEN+""+(needed+1 - Rush.players.size())+
								ChatColor.RED+" more player needed!");
					}
					Bukkit.getScheduler().cancelTask(countdown);
				}
				
			}
		}, 0, 20);
		
	}
	
	
	protected static void startGame() {
		
		
		deaths = Rush.players.size();
		Rush.world.getBlockAt(-1, 71, -61).setType(Material.BEACON);
		Rush.world.getBlockAt(1, 71, 61).setType(Material.BEACON);
		Rush.hastnt = null;
		Rush.hasmelone = null;
		
		for (String pname : Rush.players) {
			//Spawn
			Player p = Bukkit.getPlayer(pname);
			p.setLevel(0);
			PlayerInventory inv = p.getInventory();
			inv.clear();
			//inv.setItem(39, null);
			inv.setItem(38, null);
			inv.setItem(37, null);
			inv.setItem(36, null);
			p.updateInventory();
			p.sendMessage("Start!");
			p.playSound(p.getLocation(), Sound.CAT_MEOW, 10, 1);
			Random rnd = new Random();
			if (Rush.tnt.contains(pname)) {
				p.teleport(new Location(Rush.world, rnd.nextInt(8)-3, 74, rnd.nextInt(12)+55, 180, 0));
			}
			if (Rush.melone.contains(pname)) {
				p.teleport(new Location(Rush.world, 0.5, 72, -50, 0, 0));
			}
			
		}
		
	}


	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
		if (Rush.players.remove(e.getPlayer().getDisplayName())) {
			
			if (Rush.running) {
				testend(e.getPlayer());
			} else if (!setup) {
				startCount();
			}
			
		}
		
	}
	
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (Rush.ingameplayers.contains(p.getDisplayName())) {
			
			//testend(p);
			
		}
		if (p.getWorld() == Rush.world) {
			p.spigot().respawn();
			PlayerInventory inv = p.getInventory();
			inv.clear();
			//inv.setItem(39, null);
			inv.setItem(38, null);
			inv.setItem(37, null);
			inv.setItem(36, null);
			p.updateInventory();
			Random rnd = new Random();
			Rush.world.dropItemNaturally(p.getLocation(), new ItemStack(Material.GOLD_INGOT));
			if (Rush.tnt.contains(p.getDisplayName()) && tnthasbed) {
				p.teleport(new Location(Rush.world, rnd.nextInt(8)-3, 74, rnd.nextInt(12)+55, 180, 0));
			} else {
				p.teleport(new Location(Rush.world, 0.5, 92, 0.5, 90 , 0));
			}
			if (Rush.melone.contains(p.getDisplayName()) && melonehasbed) {
				p.teleport(new Location(Rush.world, 0.5, 72, -50, 0, 0));
			} else {
				p.teleport(new Location(Rush.world, 0.5, 92, 0.5, 90 , 0));
			}
			
//			inv = p.getInventory();
//			inv.clear();
//			//inv.setItem(39, null);
//			inv.setItem(38, null);
//			inv.setItem(37, null);
//			inv.setItem(36, null);
//			p.updateInventory();
		}
		
	}
	
	
	private void testend(Player p) {
		
		String name = p.getDisplayName();
		if (Rush.ingameplayers.remove(name)) {
			
			if (Rush.tnt.remove(name) && p.equals(Rush.hasmelone)) {
				//end?
				Bukkit.broadcastMessage("Rush beendet");
			}
			if (Rush.melone.remove(name) && p.equals(Rush.hastnt)) {
				//end?
				Bukkit.broadcastMessage("Rush beendet");
			}
			
		}
		
	}


	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if (!Rush.ingame && Rush.players.contains(p.getDisplayName())) {
				e.setCancelled(true);
			}
		}
		
	}
	
	
	
}

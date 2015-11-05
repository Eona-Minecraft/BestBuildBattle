package de.nuss9940.bbb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import me.clip.actionannouncer.ActionAnnouncer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class Game {
	
	static boolean running = false;
	static boolean spectate = false;
	static HashSet<String> players = new HashSet<String>();
	static String theme;
	static List<String> nextthemes;
	static int timeInSec;
	static HashMap<String, SimpleRegion> arenas = new HashMap<String, SimpleRegion>();
	
	public static boolean testStart() {
		
		if (!running && players.size() > Main.minps) {
			start();
			return true;
		} else {
			return false;
		}
	}

	private static void start() {
		
		running = true;
		
		if (nextthemes.isEmpty()) {
			nextthemes = Main.allthemes;
		}
		theme = nextthemes.remove(new Random().nextInt(nextthemes.size()));
		
		ItemStack paper = getPaper();
		
		for (String pname : players) {
			Player p = Bukkit.getPlayer(pname);
			
			PlayerInventory inv = p.getInventory();
			inv.clear();
			inv.setItem(39, null);
			inv.setItem(38, null);
			inv.setItem(37, null);
			inv.setItem(36, null);
			for (PotionEffect effect : p.getActivePotionEffects()) {
		        p.removePotionEffect(effect.getType());
			}
			
			p.setTotalExperience(0);
			inv.setItemInHand(paper);
			ActionAnnouncer.sendAnnouncement(p, "&aStart!");
			p.teleport(getArena(), TeleportCause.SPECTATE);
		}
		timeInSec = Main.time * 60;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				updatePlayer();
				if (timeInSec < 1) {
					Bukkit.getScheduler().cancelTasks(Main.plugin);
					spectate = true;
					for (String pname : players) {
						Player p = Bukkit.getPlayer(pname);
						p.setLevel(0);
						ActionAnnouncer.sendAnnouncement(p, "&l&4Bauzeit zuende!");
						PlayerInventory inv = p.getInventory();
						inv.clear();
						inv.setItem(39, null);
						inv.setItem(38, null);
						inv.setItem(37, null);
						inv.setItem(36, null);
						for (PotionEffect effect : p.getActivePotionEffects()) {
					        p.removePotionEffect(effect.getType());
						}
					}
					
				} else {
					timeInSec--;
				}
			}

			private void updatePlayer() {
				
				if (timeInSec % 60 == 0) {
					for (String pname : players) {
						Player p = Bukkit.getPlayer(pname);
						p.setLevel(timeInSec);
						ActionAnnouncer.sendAnnouncement(p, "&aNoch &l&4" + timeInSec + "&r&4 sek.");
					}
				} else {
					for (String pname : players) {
						Bukkit.getPlayer(pname).setLevel(timeInSec);
					}
				}
			}
		}, 60, 20);
	}
	
	private static void stop() {
		running = false;
	}

	private static ItemStack getPaper(){
		ItemStack s = new ItemStack(Material.PAPER);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(theme);
		s.setItemMeta(m);
		return s;
	}
	
	int usedArenas = 0;
	
	private static Location getArena() {
		//TODO createme
		return null;
	}
	
}

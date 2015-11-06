package de.nuss9940.bbb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.clip.actionannouncer.ActionAnnouncer;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Objective;

public class Game {
	
	static boolean running = false;
	static boolean spectate = false;
	static ArrayList<String> players = new ArrayList<String>();
	static String theme;
	static List<String> nextthemes;
	static int timeInSec;
	static int arenasleft;
	static HashMap<String, BuildArena> arenas = new HashMap<String, BuildArena>();
	static String showp;
	static Objective ranking;
	
	static Dye red = new Dye();
	static Dye orange = new Dye();
	static Dye yellow = new Dye();
	static Dye lime = new Dye();
	static Dye green = new Dye();
	
	static ItemStack verybad;
	static ItemStack bad;
	static ItemStack meh;
	static ItemStack good;
	static ItemStack verygood;
	
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
			if (Main.allthemes.isEmpty()) {
				theme = "Error";
			} else {
				nextthemes = Main.allthemes;
				theme = nextthemes.remove(new Random().nextInt(nextthemes.size()));
			}
		}
		
		ItemStack paper = new ItemStack(Material.PAPER);
		paper = name(paper, theme);
		int offset = 50;
		arenas.clear();
		
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
			
			//TODO anpassen
			arenas.put(pname, new BuildArena(offset + 15, 60, 0, offset, 10, 0, offset + 31, 80, 0));
			offset += 36;
			
			p.teleport(arenas.get(pname).getSpawn(), TeleportCause.SPECTATE);
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
					startVoting();
					
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
			
			private void startVoting() {
				
				timeInSec = 15;
				arenasleft = arenas.size();
				for (String pname : ranking.getScoreboard().getEntries()) {
					ranking.getScoreboard().resetScores(pname);
				}
				
				red.setColor(DyeColor.RED);
				orange.setColor(DyeColor.ORANGE);
				yellow.setColor(DyeColor.YELLOW);
				lime.setColor(DyeColor.LIME);
				green.setColor(DyeColor.GREEN);
				
				verybad = red.toItemStack();
				bad = orange.toItemStack();
				meh = yellow.toItemStack();
				good = lime.toItemStack();
				verygood = green.toItemStack();
				
				verybad = name(verybad, "&4GRAUENHAFT!!");
				bad = name(bad, "&6*kotz*");
				meh = name(meh, "&eok!");
				good = name(good, "&aschön!");
				verygood = name(verygood, "&2Wow!");
				
				for (String pname : players) {
					Player p = Bukkit.getPlayer(pname);
					p.setLevel(15);
					ActionAnnouncer.sendAnnouncement(p, "&aVoting beginnt");
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
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						if (timeInSec < 1) {
							if (arenasleft < 1) {
								Bukkit.getScheduler().cancelTasks(Main.plugin);
								stop();
							} else {
								timeInSec = 15;
								arenasleft--;
							}
						} else if (timeInSec == 15) {
							showp = players.get(arenasleft - 1);
							BuildArena thisa = arenas.get(showp);
							for (String pname : players) {
								Player p = Bukkit.getPlayer(pname);
								p.setLevel(timeInSec);
								PlayerInventory inv = p.getInventory();
								inv.clear();
								inv.addItem(verybad);
								inv.addItem(bad);
								inv.addItem(meh);
								inv.addItem(good);
								inv.addItem(verygood);
								p.teleport(thisa.getSpawn(), TeleportCause.SPECTATE);
							}
							timeInSec--;
						} else {
							for (String pname : players) {
								Bukkit.getPlayer(pname).setLevel(timeInSec);
							}
							timeInSec--;
						}
					}
				}, 60, 20);
			}
			
		}, 60, 20);
	}
	
	static void stop() {
		for (String pname : players) {
			Player p = Bukkit.getPlayer(pname);
			p.setLevel(0);
			ActionAnnouncer.sendAnnouncement(p, "&l&4Spiel zuende!");
			PlayerInventory inv = p.getInventory();
			inv.clear();
			inv.setItem(39, null);
			inv.setItem(38, null);
			inv.setItem(37, null);
			inv.setItem(36, null);
			for (PotionEffect effect : p.getActivePotionEffects()) {
				p.removePotionEffect(effect.getType());
			}
			p.setScoreboard(ranking.getScoreboard());
		}
		
		for (String pname : players) {
			arenas.remove(pname);
		}
		
		Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
			@Override
			public void run() {
				for (String pname : players) {
					Player p = Bukkit.getPlayer(pname);
					p.teleport(Main.lobby, TeleportCause.SPECTATE);
					p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				}
				spectate = false;
				running = false;
			}
		}, 100);
		
	}
	
	public static void leave(Player p) {
		players.remove(p.getName());
		if (running) {
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			arenas.remove(p.getName());
			if (players.size() < Main.minps) {
				Bukkit.getScheduler().cancelTasks(Main.plugin);
				stop();
			}
		}
	}
	
	private static ItemStack name(ItemStack s, String name) {
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(name);
		s.setItemMeta(m);
		return s;
	}
	
}

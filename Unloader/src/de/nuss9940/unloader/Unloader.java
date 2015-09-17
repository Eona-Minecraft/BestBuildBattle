package de.nuss9940.unloader;

import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;


public class Unloader extends JavaPlugin {
	
	
	//Varc
	String world = "Creative1";
	String message1 = ChatColor.RED + "Creative wird in " + ChatColor.GREEN;
	String message2 = ChatColor.RED + "Minuten geschlossen!";
	
	//Var
	//private static final Unloader instance = new Unloader();
	public static Unloader plugin;
	//public Unloader(Unloader plugin) {  Unloader.plugin = plugin;  }
	MultiverseCore mv = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("MultiverseCore");
	Logger log = Logger.getLogger("Minecraft");
	
	
	
	public void onDisable(){
		
		plugin = null;
		
	}
	
	
	public void onEnable(){
		
		
		//Var
		plugin = this;
		Calendar c = Calendar.getInstance();
		int hh = c.get(Calendar.HOUR_OF_DAY);
		int day = c.get(Calendar.DAY_OF_WEEK);
		int unloadtime;
		
		//Day
		if (day == Calendar.FRIDAY || day == Calendar.SATURDAY) {
			unloadtime = 23;
		} else {
			unloadtime = 22;
		}
		
		if (hh >= unloadtime || hh < 9) {
			
			//schedule load
			load(getTime());
			
		} else if (hh < unloadtime || hh >= 9) {
			
			//open now
			Bukkit.broadcastMessage(ChatColor.RED + "Die Creative Welt wird " + ChatColor.GREEN + "JETZT" + ChatColor.RED + " geöffnet!");
			
			mv = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
			
			if (mv != null) {
				
				mv.getCore().getMVWorldManager().loadWorld(world);
				
			} else {
				log.warning("[Unloader] Multiverse-Core not found!");
			}
			
			
			//schedule unload
			unload(getTime());
			
		}
		
	}
	
	
	public long getTime() {
		
		
		//Var
		Calendar c = Calendar.getInstance();
		
		int hh = c.get(Calendar.HOUR_OF_DAY);
		int mm = c.get(Calendar.MINUTE);
		int day = c.get(Calendar.DAY_OF_WEEK);
		int unloadtime;
		int whh = 0;
		int whhinmm = 0;
		
		log.info("[Unloader] System Time: " + hh + ":" + mm);
		
		//Day
		if (day == Calendar.FRIDAY || day == Calendar.SATURDAY) {
			
			unloadtime = 23;
			
		} else {
			
			unloadtime = 22;
			
		}
		
		
		//Calc
		if (hh >= unloadtime || hh < 9) {
			
			//unload
			if (hh > 9) {
				
				whh = 24 - hh + 9;
				
			} else {
				
				whh = 9 - hh;
				
			}
			
			
			whhinmm = whh * 60;
			
			log.info("[Unloader] Full hours until unload: " + whh + " - equals " + whhinmm + " Minutes!");
			log.info("[Unloader] Minutes to subtract: " + mm);
			
			
		} else if (hh < unloadtime || hh >= 9) {
			
			//load
			
			whh = unloadtime - hh;
			
			whhinmm = whh * 60;
			
			log.info("[Unloader] Full hours until load: " + whh + " - equals " + whhinmm + " Minutes!");
			log.info("[Unloader] Minutes to subtract: " + mm);
			
			
		} else {
			log.warning("[Unloader] Could not read system time!");
			log.warning("[Unloader] World will not be unloaded!");
			return -1;
		}
		
		
		return (whhinmm - mm)*60;
		
	}
	
	
	public void unload(long t) {
		
		if (t > 300) {
			t -= 300;
		}
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			
			@Override
			public void run() {
				
				Bukkit.broadcastMessage(ChatColor.RED + "Die Creative Welt wird in " + ChatColor.GREEN + "5 Minuten" + ChatColor.RED + " geschlossen!");
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						
						Bukkit.broadcastMessage(ChatColor.RED + "Die Creative Welt wird " + ChatColor.GREEN + "JETZT" + ChatColor.RED + " geschlossen!");
						
						mv = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

						if (mv != null) {
							
							mv.getCore().getMVWorldManager().unloadWorld(world, true);
						
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								
								@Override
								public void run() { load(getTime()); }}, 1800L);//1.5 min
						} else {
							log.warning("[Unloader] Multiverse-Core not found!");
						}
						
					}
				}, 6000L);//5 min
				
			}
		}, t*20);
		
	}
	
	
	
	public void load(long t) {
		
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			
			@Override
			public void run() {
				
				Bukkit.broadcastMessage(ChatColor.RED + "Die Creative Welt wird " + ChatColor.GREEN + "JETZT" + ChatColor.RED + " geöffnet!");
				mv = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
				
				if (mv != null) {
					
					mv.getCore().getMVWorldManager().loadWorld(world);
				
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						@Override
						public void run() { unload(getTime()); }}, 1800L);//1.5 min
				} else {
					log.warning("[Unloader] Multiverse-Core not found!");
				}
			}
		}, t*20);
		
	}
	
	
}
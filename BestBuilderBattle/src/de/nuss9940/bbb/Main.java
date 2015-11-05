package de.nuss9940.bbb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

public class Main extends JavaPlugin {

	FileConfiguration conf;
	
	//config
	static Location lobby;
	static String wname;
	static int minps = 3;
	static int time = 5;
	static List<String> allthemes;

	public static Plugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		loadConfig();
		Game.nextthemes = allthemes;
		new Event();
		Game.ranking = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("bbbscore");
		if (Game.ranking == null) {
			Game.ranking = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("bbbscore", "dummy");
			Game.ranking.setDisplayName("&b&lPunkte:");
			Game.ranking.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("bbb")
				|| command.getName().equalsIgnoreCase("bestbuilderbattle")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("join")) {

						Game.players.add(p.getName());
						p.teleport(lobby);
						return true;

					} else if (args[0].equalsIgnoreCase("leave")) {
						Game.leave(p);
						return true;
						
					} else if (args[0].equalsIgnoreCase("start")) {

						if (p.hasPermission("worldedit.*")) {
							if (!Game.testStart()) {
								p.sendMessage(ChatColor.RED + "Mindestens "
										+ minps
										+ " Spieler für Spielstart benötigt");
							}
							return true;
						} else {
							p.sendMessage(ChatColor.RED + "Nur für Admins!");
							return false;
						}
					} else if (args[0].equalsIgnoreCase("stop")) {

						if (p.hasPermission("worldedit.*")) {
							Bukkit.getScheduler().cancelTasks(Main.plugin);
							Game.stop();
							return true;
						} else {
							p.sendMessage(ChatColor.RED + "Nur für Admins!");
							return false;
						}
					} else if (args[0].equalsIgnoreCase("reloadconfig")) {

						if (p.hasPermission("worldedit.*")) {
							reloadConfig();
							return true;
						} else {
							p.sendMessage(ChatColor.RED + "Nur für Admins!");
							return false;
						}
					}
					return false;
				} else {
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Nur für Spieler!");
			}

		}
		return true;
	}

	public void loadConfig() {
		
		getConfig().addDefault("Locations.Lobby.World", "world");
		getConfig().addDefault("Locations.Lobby.x", "1111");
		getConfig().addDefault("Locations.Lobby.y", "101");
		getConfig().addDefault("Locations.Lobby.z", "-1111");
		getConfig().addDefault("Locations.Lobby.yaw", "180");
		
		getConfig().addDefault("Game.Min_Players", "3");
		getConfig().addDefault("Game.Build_Time", "5");
		ArrayList<String> examples = new ArrayList<String>();
		String[] ex = {"Bett","Melone"};
		for (String string : ex) {
			examples.add(string);
		}
		getConfig().addDefault("Game.Things_to_Build", examples);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		conf = getConfig();
		
		lobby = new Location(Bukkit.getWorld(conf.getString("Locations.Lobby.World")),
				conf.getDouble("Locations.Lobby.x"),
				conf.getDouble("Locations.Lobby.y"),
				conf.getDouble("Locations.Lobby.z"),
				(float) conf.getDouble("Locations.Lobby.yaw"), 0);
		
		wname = lobby.getWorld().getName();
		
		minps = conf.getInt("Game.Min_Players");
		if (minps < 2) {
			minps = 2;
			Logger.getLogger("Minecarft").warning("[BestBuilderBattle] CONFIGURATION FEHLERHAFT,"
					+ " \"Min_Players\" DARF MINNIMAL 2 SEIN!");
		}
		
		time = conf.getInt("Game.Build_Time");
		if (time < 1) {
			time = 1;
			Logger.getLogger("Minecarft").warning("[BestBuilderBattle] CONFIGURATION FEHLERHAFT,"
					+ " \"Build_Time\" DARF MINNIMAL 1 SEIN!");
		}
		
		allthemes = conf.getStringList("Game.Things_to_Build");
	}
	
}

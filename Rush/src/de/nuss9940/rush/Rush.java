package de.nuss9940.rush;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Rush extends JavaPlugin {
	
	//Var
	Logger log = Logger.getLogger("Minecraft");
	public static World world;
	
	//Conf
	ArrayList<Arena> as = new ArrayList<Arena>();
	

	public void onEnable() {
		
		//Config
		FileConfiguration conf = getConfig();
		conf.addDefault("World", "Rush");
		world = Bukkit.getWorld(conf.getString("Rush.World"));
		
		if (world == null) {
			
			log.warning("[Rush] WOLRD " + conf.getString("Rush.World") + " NOT FOUND! PLEASE EDIT THE CONFIG FILE!");
			world = Bukkit.getWorlds().get(0);
			
		} else {
			
			new Interact(this);
			new Handler(this);
			new BuildManager(this);
			
		}
		conf.addDefault("Arena.Example", new Arena("TestArena", new SimpleRegion(100, 100, -100, -100),
				new SimpleLocation(10, 10), 10, new SimpleLocation(-10, -10), 10));
		
		for (Object thisarenaname : conf.getConfigurationSection("Arena").getValues(false).values()) {
			
			if (thisarenaname instanceof String) {
				try {
					as.add((Arena) conf.get("Arena." + thisarenaname));
				} catch (Exception e) {
					log.warning("[Rush] THERE WAS AN ERROR LOADING ARENA "+ thisarenaname +"! PLEASE EDIT THE CONFIG FILE!");
				}
			} else {
				log.warning("[Rush] THERE WAS AN ERROR LOADING AN ARENA! PLEASE EDIT THE CONFIG FILE!");
			}
			
		}
		String names = "[Rush] Loaded arenas: ";
		for (Arena thisarena : as) {
			names.concat(thisarena.name);
			names.concat(", ");
		}
		log.info(names.substring(0, names.length() - 2) + " loaded!");
		
	}
}

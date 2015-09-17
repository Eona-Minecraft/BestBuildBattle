package de.nuss9940.rush;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BuildManager implements Listener {
	
	public Rush plugin;
	public BuildManager(Rush plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	
	//Var
	
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		Material m = b.getState().getType();
		if (b.getWorld() == Rush.world) {
			if (!e.getPlayer().isOp()) {
				if (m != Material.SANDSTONE && m != Material.LADDER && m != Material.ENDER_STONE
						&& m != Material.CHEST && m != Material.WEB) {
					
					e.setCancelled(true);
					
				}
			}
			if (m == Material.MOB_SPAWNER) {
				Player p = e.getPlayer();
				Location loc = b.getLocation();
				if (loc.getBlockZ() == 61) {
					if (Rush.tnt.contains(p.getDisplayName())) {
						e.setCancelled(true);
					} else {
						Rush.hastnt = p;
						Rush.world.getBlockAt(1, 71, 61).setType(Material.OBSIDIAN);
						Rush.world.spigot().strikeLightning(loc, false);
					}
				}
				if (loc.getBlockZ() == -61) {
					if (Rush.melone.contains(p.getDisplayName())) {
						e.setCancelled(true);
					} else {
						Rush.hasmelone = p;
						Rush.world.getBlockAt(-1, 71, -61).setType(Material.OBSIDIAN);
						Rush.world.spigot().strikeLightning(loc, false);
					}
				}
			}
		}
	}
}

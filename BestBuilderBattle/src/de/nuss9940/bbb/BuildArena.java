package de.nuss9940.bbb;

import org.bukkit.Location;
import org.bukkit.Material;

public class BuildArena {
	
	private Location spawn;
	private int x1;
	private int y1;
	private int z1;
	private int x2;
	private int y2;
	private int z2;
	
	
	public BuildArena(int x, int y, int z, int x1, int y1, int z1, int x2, int y2, int z2) {
		
		this.spawn = new Location(Main.lobby.getWorld(), x, y, z);
		
		if (x1 < x2) {
			this.x1 = x1;
			this.x2 = x2;
		} else {
			this.x1 = x2;
			this.x2 = x1;
		}
		if (y1 < y2) {
			this.y1 = y1;
			this.y2 = y2;
		} else {
			this.y1 = y2;
			this.y2 = y1;
		}
		if (z1 < z2) {
			this.z1 = z1;
			this.z2 = z2;
		} else {
			this.z1 = z2;
			this.z2 = z1;
		}
	}
	
	public Location getSpawn() {return this.spawn;}
	public int getX1() {return this.x1;}
	public int getY1() {return this.y1;}
	public int getZ1() {return this.z1;}
	public int getX2() {return this.x2;}
	public int getY2() {return this.y2;}
	public int getZ2() {return this.z2;}
	
	public boolean inRegion(Location loc) {
		
		if (loc.getX() >= x1 && loc.getX() <= x2 && 
			loc.getY() >= y1 && loc.getY() <= y2 && 
			loc.getZ() >= z1 && loc.getZ() <= z2) {
			return true;
		} else {
			return false;
		}
	}
	
	public void clear() {
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					spawn.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}
	}
}

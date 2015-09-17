package de.nuss9940.rush;

import org.bukkit.Location;

public class SimpleRegion {
	
	private int x1;
	private int z1;
	private int x2;
	private int z2;
	
	
	public SimpleRegion(int x1, int z1, int x2, int z2) {
		
		if (x1 < x2) {
			this.x1 = x1;
			this.x2 = x2;
		} else {
			this.x1 = x2;
			this.x2 = x1;
		}
		if (z1 < z2) {
			this.z1 = z1;
			this.z2 = z2;
		} else {
			this.z1 = z2;
			this.z2 = z1;
		}
		
	}
	
	public int getX1() {
		return this.x1;
	}
	public int getZ1() {
		return this.z1;
	}
	public int getX2() {
		return this.x2;
	}
	public int getZ2() {
		return this.z2;
	}
	public boolean inRegion(Location loc) {
		
		if (loc.getX() > x1 && loc.getX() < x2 && loc.getZ() > z1 && loc.getZ() < z2) {
			return true;
		} else {
			return false;
		}
	}
}

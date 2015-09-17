package de.nuss9940.rush;

import java.util.HashSet;

import org.bukkit.util.Vector;

public class Arena {
	
	String name;
	SimpleRegion arena;
	private HashSet<SimpleLocation> redspawn;
	private HashSet<SimpleLocation> greenspawn;
	
	
	public Arena(String name, SimpleRegion arena, SimpleLocation redspawner, int rred,
			SimpleLocation greenspawner, int rgreen) {
		
		this.name = name;
		this.arena = arena;
		redspawn = getCycle(redspawner, rred);
		greenspawn = getCycle(greenspawner, rgreen);
		
	}
	
	public HashSet<SimpleLocation> getRedspawn() {
		return redspawn;
	}
	public HashSet<SimpleLocation> getGreenspawn() {
		return greenspawn;
	}
	
	private HashSet<SimpleLocation> getCycle(SimpleLocation redspawner, int r) {
		
		Vector middlevec = new Vector(redspawner.getX(), 0, redspawner.getZ());
		
		HashSet<SimpleLocation> points = new HashSet<SimpleLocation>();
		
		for (int x = 0; x < r; x++) {
			
			for (int z = 0; z < r; z++) {
				
				Vector thisvec = new Vector(x, 0, z);
				if (thisvec.distanceSquared(middlevec) <= r * r) {
					
					//add
					points.add(new SimpleLocation( x,  z));
					points.add(new SimpleLocation(-x, -z));
					if (x != 0 && z != 0) {
						points.add(new SimpleLocation(-x,  z));
						points.add(new SimpleLocation( x, -z));
					}
				}
			}
		}
		
		return points;
		
	}
	
}

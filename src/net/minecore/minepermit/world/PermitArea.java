/**
 * 
 */
package net.minecore.minepermit.world;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Numenorean95
 *
 */
public abstract class PermitArea {
	
	/**
	 * Tests if this PermitArea contains the given point
	 * @param l Location to test
	 * @return True if it does, false otherwise.
	 */
	public abstract boolean contains(Location l);
	
	/**
	 * Gets the name of this PermitArea
	 * @return The name, or null if it doesnt have one.
	 */
	public abstract String getName();
	
	/**
	 * Gets the prices of all the required permits for this area.
	 * @return A PermitPriceList that as all prices defined.
	 */
	public abstract PermitPriceList getPrices();
	
	/**
	 * Gets the depth at which permits are relevant.
	 * @return A Y-Coord between 0 and the world height limit.
	 */
	public abstract int getEffectiveDepth();
	
	/**
	 * Gets the world that this area is contained in.
	 * @return A World
	 */
	public World getWorld();
	
	/**
	 * Checks if the given block must have a permit to be mined
	 * @param id block id of the material to be checked.
	 * @return True if a permit is required 
	 */
	public boolean requiresPermit(int id);
	
	

}

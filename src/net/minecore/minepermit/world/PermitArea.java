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
public interface PermitArea {
	
	/**
	 * Tests if this PermitArea contains the given point
	 * @param l Location to test
	 * @return True if it does, false otherwise.
	 */
	public boolean contains(Location l);
	
	/**
	 * Gets the name of this PermitArea
	 * @return The name, or null if it doesnt have one.
	 */
	public String getName();
	
	/**
	 * Gets the prices of all the required permits for this area.
	 * @return A PermitPriceList that as all prices defined.
	 */
	public PermitPriceList getPrices();
	
	/**
	 * Gets the depth at which permits are relevant.
	 * @return A Y-Coord between 0 and the world height limit.
	 */
	public int getEffectiveDepth();
	
	/**
	 * Gets the world that this area is contained in.
	 * @return A World
	 */
	public World getWorld();

}

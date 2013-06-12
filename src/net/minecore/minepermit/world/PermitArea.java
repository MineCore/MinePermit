/**
 * 
 */
package net.minecore.minepermit.world;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.TreeMap;

import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Numenorean95
 *
 */
public abstract class PermitArea {
	
	private Map<String, PermitArea> children;
	private PriceList pl;
	private int effective_depth;
	
	public PermitArea(String name, PriceList pl){
		
		if(pl == null)
			throw new InvalidParameterException("PriceList can not be null!");
		
		this.pl = pl;
		children = new TreeMap<String, PermitArea>();
	}

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
	 * Sets the name of this PermitArea
	 * @param name The name to set
	 */
	public abstract void setName(String name);
	
	/**
	 * Gets the prices of all the required permits for this area.
	 * @return A PermitPriceList that as all prices defined.
	 */
	public PriceList getPrices(){
		return pl;
	}
	
	/**
	 * Gets the depth at which permits are relevant.
	 * @return A Y-Coord between 0 and the world height limit.
	 */
	public int getEffectiveDepth(){
		return effective_depth;
	}
	
	public void setEffectiveDepth(int y){
		effective_depth = y;
	}
	
	/**
	 * Gets the world that this area is contained in.
	 * @return A World
	 */
	public abstract World getWorld();
	
	/**
	 * Checks if the given block must have a permit to be mined
	 * @param id block id of the material to be checked.
	 * @return True if a permit is required 
	 */
	public boolean requiresPermit(int id){
		return getPrices().getPrice(id) != -1;
	}
	
	/**
	 * Gets the price to buy this permit.
	 * @param id The id of the material to check
	 * @return The price, or -1 if a permit is not required
	 */
	public int getPrice(int id){
		return getPrices().getPrice(id);
	}
	
	/**
	 * Gets the lowest level PermitArea that contains this location. 
	 * Thus this could return a PermitArea that is contained entirely within this PermitArea, or one contained in a PermitArea that is contained
	 * in this PermitArea, and so on. This allows for infinite levels of PermitAreas, and so Inception.
	 * @param l The location to check
	 * @return A PermitArea, possibly this one, that contains the given point as per above. If it is not contained in this PermitArea returns null.
	 */
	public PermitArea getRelevantPermitArea(Location l){
		if(!contains(l))
			return null;
		
		for(String s : children.keySet()){
			PermitArea pa = children.get(s).getRelevantPermitArea(l);
			
			if(pa != null)
				return pa;
			
		}
		
		return this;
	}
	
	/**
	 * Adds the given PermitArea to this PermitArea as a child PermitArea. See concepts relating to Inception in getRelevantPermitArea(). 
	 * The given PermitArea must be completely contained in this PermitArea, and it must have a unique name.
	 * @param pa The PermitArea to add
	 * @return True if the PermitArea could be added, false otherwise
	 */
	public abstract boolean addPermitArea(PermitArea pa);
	
	/**
	 * Gets and removes the child PermitArea with this name.
	 * @param name Name of the PermitArea to remove.
	 * @return The PermitArea, or null if it is not contained in this PermitArea.
	 */
	public PermitArea removePermitArea(String name) {
		return children.remove(name);
	}
	
	/**
	 * Gets the child PermitArea with this name.
	 * @param name The name of the chimmmmmmld PermitArea to get.
	 * @return The PermitArea or null if there is no child PermitArea with the given name.
	 */
	public PermitArea getPermitArea(String name){
		return children.get(name);
	}
	
	/**
	 * Gets all the children contained in this PermitArea
	 * @return A potentially empty Map with the name of the area as the Key.
	 */
	public Map<String, PermitArea> getChildren(){
		return children;
	}
}

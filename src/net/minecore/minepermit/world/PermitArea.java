/**
 * 
 */
package net.minecore.minepermit.world;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.TreeMap;

import net.minecore.minepermit.price.Price;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Numenorean95
 * 
 */
public abstract class PermitArea {

	private final Map<String, ContainablePermitArea> children;
	private final PriceList pl;
	private int effectiveDepth;
	private String name;
	private boolean allowMiningUnspecifiedBlocks;

	public PermitArea(String name, PriceList pl) {

		if (pl == null)
			throw new InvalidParameterException("PriceList can not be null!");

		this.name = name;

		this.pl = pl;
		children = new TreeMap<String, ContainablePermitArea>();

		allowMiningUnspecifiedBlocks = false;
		effectiveDepth = 65;
	}

	/**
	 * Tests if this PermitArea contains the given point
	 * 
	 * @param l
	 *            Location to test
	 * @return True if it does, false otherwise.
	 */
	public abstract boolean contains(Location l);

	/**
	 * Checks if the given PermitArea is entirely contained in this PermitArea
	 * 
	 * @param pa
	 *            PermitArea to check
	 * @return True if it is entirely contained in this area, including if they
	 *         share any borders.
	 */
	public abstract boolean contains(ContainablePermitArea pa);

	/**
	 * Checks to see whether the given PermitArea is at all contained in this
	 * PermitArea
	 * 
	 * @param pa
	 *            The PermitArea to check
	 * @return True if it is contained,false otherwise
	 */
	public abstract boolean intersects(PermitArea pa);

	/**
	 * Gets the name of this PermitArea
	 * 
	 * @return The name, or null if it doesnt have one.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this PermitArea
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the prices of all the required permits for this area.
	 * 
	 * @return A PermitPriceList that as all prices defined.
	 */
	public PriceList getPrices() {
		return pl;
	}

	/**
	 * Gets the depth at which permits are relevant.
	 * 
	 * @return A Y-Coord between 0 and the world height limit.
	 */
	public int getEffectiveDepth() {
		return effectiveDepth;
	}

	/**
	 * Sets the effective depth of this PermitArea
	 * 
	 * @param y
	 *            The depth
	 */
	public void setEffectiveDepth(int y) {
		effectiveDepth = y;
	}

	/**
	 * Gets the world that this area is contained in.
	 * 
	 * @return A World
	 */
	public abstract World getWorld();

	/**
	 * Gets the price to buy this permit.
	 * 
	 * @param m
	 *            The material to check
	 * @return A Price, or null if it is not tracked
	 */
	public Price getPrice(Material m) {
		return getPrices().getPrice(m);
	}

	/**
	 * Gets whether this material requires a permit to be mined. Does NOT take
	 * into account allowMiningUnspecifiedBlocks
	 * 
	 * @param m
	 *            Material to check
	 * @return True if it requires a permit.
	 */
	public boolean requiresPermit(Material m) {
		return getPrice(m) != null;
	}

	/**
	 * Gets the lowest level PermitArea that contains this location. Thus this
	 * could return a PermitArea that is contained entirely within this
	 * PermitArea, or one contained in a PermitArea that is contained in this
	 * PermitArea, and so on. This allows for infinite levels of PermitAreas,
	 * and so Inception.
	 * 
	 * @param l
	 *            The location to check
	 * @return A PermitArea, possibly this one, that contains the given point as
	 *         per above. If it is not contained in this PermitArea returns
	 *         null.
	 */
	public PermitArea getRelevantPermitArea(Location l) {
		if (!contains(l))
			return null;

		for (String s : children.keySet()) {
			PermitArea pa = children.get(s).getRelevantPermitArea(l);

			if (pa != null)
				return pa;

		}

		return this;
	}

	/**
	 * Adds the given PermitArea to this PermitArea as a child PermitArea. See
	 * concepts relating to Inception in getRelevantPermitArea(). The given
	 * PermitArea must be completely contained in this PermitArea, and it must
	 * have a unique name.
	 * 
	 * @param pa
	 *            The PermitArea to add
	 * @return True if the PermitArea could be added, false otherwise
	 */
	public boolean addPermitArea(ContainablePermitArea pa) {
		if (pa == null)
			throw new InvalidParameterException("You cannot add a null permit area!");

		if (getChildPermitArea(pa.getName()) != null)
			return false;

		getChildren().put(pa.getName(), pa);

		pa.setParent(this);

		return true;
	}

	/**
	 * Gets and removes the child PermitArea with this name.
	 * 
	 * @param name
	 *            Name of the PermitArea to remove.
	 * @return The PermitArea, or null if it is not contained in this
	 *         PermitArea.
	 */
	public ContainablePermitArea removeChildPermitArea(ContainablePermitArea pa) {
		return children.remove(pa.getName());
	}

	/**
	 * Gets the child PermitArea with this name.
	 * 
	 * @param name
	 *            The name of the child PermitArea to get.
	 * @return The PermitArea or null if there is no child PermitArea with the
	 *         given name.
	 */
	public ContainablePermitArea getChildPermitArea(String name) {
		return children.get(name);
	}

	/**
	 * Gets all the children contained in this PermitArea
	 * 
	 * @return A potentially empty Map with the name of the area as the Key.
	 */
	public Map<String, ContainablePermitArea> getChildren() {
		return children;
	}

	/**
	 * Saves the data in this PermitArea to the given Configuration.
	 * 
	 * @param cs
	 *            The ConfigurationSection to save to.
	 */
	public abstract void saveToConfiguration(ConfigurationSection cs);

	/**
	 * Gets a string that can be used to directly get this PermitArea
	 * 
	 * @return A String
	 */
	public String getStringRepresentation() {
		return name;
	}

	/**
	 * Gets the Price of the UniversalPermit
	 * 
	 * @return A Price, or null if it isnt availible
	 */
	public Price getUniversalPermitPrice() {
		return pl.getUniversalPrice();
	}

	/**
	 * Sets whether blocks with no price can still be mined
	 * 
	 * @param flag
	 *            True or False
	 */
	public void setAllowMiningUnspecifiedBlocks(boolean flag) {
		allowMiningUnspecifiedBlocks = flag;
	}

	/**
	 * Gets whether blocks that have no price can be mined
	 * 
	 * @return True or False
	 */
	public boolean getAllowMiningUnspecifiedBlocks() {
		return allowMiningUnspecifiedBlocks;
	}

}

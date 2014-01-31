package net.minecore.minepermit.price;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public interface PriceList {

	/**
	 * Gets the price for the given material
	 * 
	 * @param m
	 *            Material of the object
	 * @return The price or null if there is no price
	 */
	public Price getPrice(Material m);

	/**
	 * Gets the price for the universal Permit
	 * 
	 * @return A Price, or null if a Universal Permit isn't available
	 */
	public Price getUniversalPrice();

	/**
	 * Sets the Universal Price
	 * 
	 * @param p
	 *            The price to set it to.
	 */
	public void setUniversalPrice(Price p);

	/**
	 * Gets all the prices contained in this PriceList
	 * 
	 * @return A map, potentially empty
	 */
	public Map<Material, Price> getPrices();

	/**
	 * Saves this PriceList in the given ConfigurationSection
	 */
	public void saveToConf(ConfigurationSection cs);

	/**
	 * Sets the cost for the given material
	 * 
	 * @param id
	 *            ID of the material to set the cost of.
	 * @param cost
	 *            Cost to set it to
	 */
	public void setPrice(Material m, Price p);

}

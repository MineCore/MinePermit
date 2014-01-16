package net.minecore.minepermit.price;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public interface PriceList {

	/**
	 * Gets the price for the given material
	 * 
	 * @param id
	 *            Id of the material to get the price of
	 * @return The price or
	 */
	public int getPrice(Material m);

	/**
	 * Gets all the prices contained in this PriceList
	 * 
	 * @return A map, potentially empty
	 */
	public Map<Material, Integer> getPrices();

	/**
	 * Saves this PriceList in the given ConfigurationSection
	 */
	public void saveToConf(ConfigurationSection cs);

	/**
	 * Sets the cost for the given material id
	 * 
	 * @param id
	 *            ID of the material to set the cost of.
	 * @param cost
	 *            Cost to set it to
	 */
	public void setPrice(Material m, int cost);

}

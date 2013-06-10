package net.minecore.minepermit.world;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public interface PriceList {

	/**
	 * Gets the price for the given material
	 * @param id Id of the material to get the price of
	 * @return The price or 
	 */
	public int getPrice(int id);
	
	/**
	 * Gets all the prices contained in this PriceList
	 * @return A map, potentially empty
	 */
	public Map<Integer, Integer> getPrices();
	
	/**
	 * Gets this PermitPriceList as a ConfigurationSection
	 * @return A ConfigurationSection that contains all the Data neccessary to reproduce this PermitPriceList
	 */
	public ConfigurationSection getConfRepresentation();

}

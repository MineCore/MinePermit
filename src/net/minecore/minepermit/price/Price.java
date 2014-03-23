package net.minecore.minepermit.price;

import java.util.TreeMap;

import net.minecore.minepermit.permits.PermitType;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * Used to hold data about the Price for something, specifically permits
 * 
 * @author The Numenorean
 * 
 */
public class Price {

	public TreeMap<PermitType, Integer> cost;
	public TreeMap<PermitType, Integer> amount;

	/**
	 * Creates a new, empty Price
	 */
	public Price() {
		cost = new TreeMap<PermitType, Integer>();
		amount = new TreeMap<PermitType, Integer>();
	}

	/**
	 * Affiliates the PermitType with the given cost and amount
	 * 
	 * @param type
	 *            The type to affiliate with
	 * @param cost
	 *            The Cost
	 * @param amount
	 *            The Amount, such as for number of blocks or time (in seconds)
	 */
	public void addType(PermitType type, int cost, int amount) {

		this.cost.put(type, cost);
		this.amount.put(type, amount);

	}

	/**
	 * Gets the amount of time or blocks that should be given when this is
	 * purchased
	 * 
	 * @param type
	 *            The PermitType
	 * @return An integer amount previously set
	 */
	public int getAmount(PermitType type) {
		return amount.get(type);
	}

	/**
	 * Gets the cost for the type of permit
	 * 
	 * @param type
	 *            The PermitType
	 * @return The cost
	 */
	public int getCost(PermitType type) {
		return cost.get(type);
	}

	/**
	 * Creates a new Price from the data containde in the ConfigurationSection
	 * 
	 * @param cs
	 *            The ConfigurationSection to read
	 * @return A new Price
	 * @throws InvalidConfigurationException
	 */
	public static Price readFromConfigurationSection(ConfigurationSection cs) throws InvalidConfigurationException {
		Price p = new Price();

		if (!cs.isConfigurationSection("types"))
			cs.createSection("types");

		ConfigurationSection types = cs.getConfigurationSection("types");

		for (String key : types.getKeys(false)) {

			if (types.isConfigurationSection(key)) {
				ConfigurationSection type = types.getConfigurationSection(key);
				p.addType(PermitType.valueOf(key), type.getInt("price"), type.getInt("amount"));
			}

		}

		return p;
	}

	@Override
	public String toString() {
		String s = "";

		for (PermitType pt : cost.keySet())
			s = s + cost.get(pt) + " for a " + pt.name() + " permit, giving " + amount.get(pt) + "\n";

		return s;
	}

	public boolean typeAvailible(PermitType type) {
		return cost.containsKey(type);
	}
}

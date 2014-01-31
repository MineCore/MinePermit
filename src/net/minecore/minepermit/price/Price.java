package net.minecore.minepermit.price;

import java.util.TreeMap;

import net.minecore.minepermit.permits.PermitType;

import org.bukkit.configuration.ConfigurationSection;

public class Price {

	public TreeMap<PermitType, Integer> cost;
	public TreeMap<PermitType, Integer> amount;

	public Price() {
		cost = new TreeMap<PermitType, Integer>();
		amount = new TreeMap<PermitType, Integer>();
	}

	public void addType(PermitType type, int cost, int amount) {

		this.cost.put(type, cost);
		this.amount.put(type, amount);

	}

	public int getAmount(PermitType type) {
		return amount.get(type);
	}

	public int getCost(PermitType type) {
		return cost.get(type);
	}

	public static Price readFromConfigurationSection(ConfigurationSection cs) {
		Price p = new Price();

		for (String key : cs.getKeys(false)) {

			if (cs.isConfigurationSection(key)) {
				ConfigurationSection type = cs.getConfigurationSection(key);
				p.addType(PermitType.valueOf(key), type.getInt("cost"), type.getInt("amount"));
			}

		}

		return p;
	}
}

package net.minecore.minepermit.price;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class InertPriceList implements PriceList {

	private final TreeMap<Material, Integer> prices;

	public InertPriceList() {
		prices = new TreeMap<Material, Integer>();
	}

	@Override
	public int getPrice(Material m) {
		Object o = prices.get(m);
		return (Integer) (o == null ? -1 : o);
	}

	@Override
	public Map<Material, Integer> getPrices() {
		return prices;
	}

	@Override
	public void saveToConf(ConfigurationSection cs) {

		for (Material m : prices.keySet())
			cs.set(m.name(), prices.get(m));
	}

	@Override
	public void setPrice(Material m, int cost) {
		prices.put(m, cost);
	}

}

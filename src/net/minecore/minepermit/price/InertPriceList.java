package net.minecore.minepermit.price;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

public class InertPriceList implements PriceList {

	private TreeMap<Integer, Integer> prices;

	public InertPriceList() {
		prices = new TreeMap<Integer, Integer>();
	}

	@Override
	public int getPrice(int id) {
		Object o = prices.get(id);
		return (int) (o == null ? -1 : o);
	}

	@Override
	public Map<Integer, Integer> getPrices() {
		return prices;
	}

	@Override
	public void saveToConf(ConfigurationSection cs) {
		
		for(int i : prices.keySet())
			cs.set(i + "", prices.get(i));
	}

	@Override
	public void setPrice(int id, int cost) {
		prices.put(id, cost);
	}

}

package net.minecore.minepermit.price;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class InertPriceList implements PriceList {

	private final TreeMap<Material, Price> prices;
	private Price universalPrice;

	public InertPriceList() {
		prices = new TreeMap<Material, Price>();
	}

	@Override
	public Price getPrice(Material m) {
		return prices.get(m);
	}

	@Override
	public Map<Material, Price> getPrices() {
		return prices;
	}

	@Override
	public void saveToConf(ConfigurationSection cs) {
		for (Material m : prices.keySet())
			cs.set(m.name(), prices.get(m));
	}

	@Override
	public void setPrice(Material m, Price price) {
		prices.put(m, price);
	}

	public static InertPriceList loadFromConfigurationSection(ConfigurationSection cs)
			throws InvalidConfigurationException {
		InertPriceList pl = new InertPriceList();

		for (String key : cs.getKeys(false)) {

			if (cs.isConfigurationSection(key)) {

				try {
					ConfigurationSection material = cs.getConfigurationSection(key);

					if (key.equalsIgnoreCase("UNIVERSAL"))
						pl.setUniversalPrice(Price.readFromConfigurationSection(material));
					else
						pl.setPrice(Material.matchMaterial(key),
								Price.readFromConfigurationSection(material));
				} catch (Exception e) {
					throw new InvalidConfigurationException(e.getMessage());
				}
			}
		}

		return pl;
	}

	@Override
	public Price getUniversalPrice() {
		return universalPrice;
	}

	@Override
	public void setUniversalPrice(Price p) {
		universalPrice = p;
	}

}

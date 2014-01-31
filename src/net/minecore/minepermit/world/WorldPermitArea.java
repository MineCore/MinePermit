package net.minecore.minepermit.world;

import net.minecore.minepermit.price.InertPriceList;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class WorldPermitArea extends PermitArea {

	private final World w;

	public WorldPermitArea(World w, PriceList pl) {

		super(w.getName(), pl);

		this.w = w;
	}

	@Override
	public boolean contains(Location l) {
		return l.getWorld().getName().equals(w.getName());
	}

	@Override
	public boolean contains(PermitArea pa) {
		return pa.getWorld().equals(w);
	}

	@Override
	public boolean intersects(PermitArea pa) {
		return pa.getWorld().equals(w);
	}

	@Override
	public String getName() {
		return w.getName();
	}

	@Override
	public World getWorld() {
		return w;
	}

	@Override
	public void setName(String name) {
		// Can't change name.
	}

	@Override
	public boolean addPermitArea(ContainablePermitArea pa) {

		if (!pa.getWorld().equals(w))
			return false;

		for (ContainablePermitArea ch : this.getChildren().values())
			if (ch.intersects(pa)) {

				if (pa.contains(ch)) {
					pa.addPermitArea(ch);
					this.removeChildPermitArea(ch);
					return super.addPermitArea(pa);
				}

				return false;

			} else if (pa.intersects(ch)) {

				if (ch.contains(pa)) {
					return ch.addPermitArea(pa);
				}

				return false;
			}

		return super.addPermitArea(pa);
	}

	/**
	 * Creates a new WorldPermitArea based on the given ConfigurationSection
	 * 
	 * @param cs
	 *            ConfigurationSection with the settings for a WorldPermitArea
	 * @return A new WorldPermitArea
	 */
	public static WorldPermitArea loadPermitArea(World w, ConfigurationSection cs) {

		PriceList pl = InertPriceList.loadFromConfigurationSection(cs
				.getConfigurationSection("prices"));

		WorldPermitArea area = new WorldPermitArea(w, pl);

		area.setAllowMiningUnspecifiedBlocks(cs.getBoolean("allowMiningUnspecifiedBlocks"));
		area.setEffectiveDepth(cs.getInt("effectiveDepth"));

		ConfigurationSection children = cs.getConfigurationSection("children");

		if (children != null) {

			for (String s : children.getKeys(false))
				if (cs.isConfigurationSection(s))
					try {
						area.addPermitArea(LocationPermitArea.loadPermitArea(s, w,
								cs.getConfigurationSection(s)));
					} catch (InvalidConfigurationException e) {
						// TODO: This could be better
						e.printStackTrace();
					}

		}

		return area;
	}

	@Override
	public void saveToConfiguration(ConfigurationSection cs) {

		cs.set("allowMiningUnspecifiedBlocks", getAllowMiningUnspecifiedBlocks());
		cs.set("effectiveDepth", getEffectiveDepth());

		cs.set("prices", null);
		ConfigurationSection prices = cs.createSection("prices");

		getPrices().saveToConf(prices);

		cs.set("children", null);
		ConfigurationSection children = cs.createSection("children");

		for (String s : this.getChildren().keySet())
			this.getChildPermitArea(s).saveToConfiguration(children.createSection(s));

	}

}

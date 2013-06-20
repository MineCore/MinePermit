package net.minecore.minepermit.world;

import net.minecore.minepermit.price.InertPriceList;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class WorldPermitArea extends PermitArea {

	private World w;

	public WorldPermitArea(World w, PriceList pl) {

		super(w.getName(), pl);

		this.w = w;
	}

	@Override
	public boolean contains(Location l) {
		return l.getWorld().getName().equals(w.getName());
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

		// TODO: Check contains in this but not children.

		return super.addPermitArea(pa);
	}

	/**
	 * Creates a new WorldPermitArea based on the given ConfigurationSection
	 * 
	 * @param cs ConfigurationSection with the settings for a WorldPermitArea
	 * @return A new WorldPermitArea
	 */
	public static WorldPermitArea loadPermitArea(World w,
			ConfigurationSection cs) {
		PriceList pl = new InertPriceList();

		ConfigurationSection prices = cs.getConfigurationSection("prices");

		if (prices != null)

			for (String s : prices.getKeys(false))
				if (prices.isInt(s)) {
					try {
						pl.setPrice(Integer.parseInt(s), prices.getInt(s));
					} catch (NumberFormatException e) {
						
					}
				}

		WorldPermitArea area = new WorldPermitArea(w, pl);

		ConfigurationSection children = cs.getConfigurationSection("children");

		if (children != null) {

			for (String s : children.getKeys(false))
				if (cs.isConfigurationSection(s))
					try {
						area.addPermitArea(LocationPermitArea.loadPermitArea(s,
								w, cs.getConfigurationSection(s)));
					} catch (InvalidConfigurationException e) {
						//TODO: This could be better
						e.printStackTrace();
					}

		}

		return area;
	}

}

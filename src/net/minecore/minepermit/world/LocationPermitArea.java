package net.minecore.minepermit.world;

import java.security.InvalidParameterException;

import net.minecore.minepermit.MinePermit;
import net.minecore.minepermit.price.InertPriceList;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class LocationPermitArea extends ContainablePermitArea {

	private final Location l1, l2;

	public LocationPermitArea(Location l1, Location l2, String name, PriceList pl) {

		super(name, pl);

		if (!l1.getWorld().equals(l2.getWorld()))
			throw new InvalidParameterException("Locations must be in same world!");

		this.l1 = l1;
		this.l2 = l2;
	}

	@Override
	public boolean contains(Location l) {

		int higherX = Math.max(l1.getBlockX(), l2.getBlockX());
		int lowerX = Math.min(l1.getBlockX(), l2.getBlockX());

		int higherZ = Math.max(l1.getBlockZ(), l2.getBlockZ());
		int lowerZ = Math.min(l1.getBlockZ(), l2.getBlockZ());

		if (l.getBlockX() >= lowerX && l.getBlockX() <= higherX && l.getBlockZ() >= lowerZ
				&& l.getBlockZ() <= higherZ)
			return true;

		return false;
	}

	@Override
	public boolean intersects(PermitArea pa) {

		int higherX = Math.max(l1.getBlockX(), l2.getBlockX());
		int lowerX = Math.min(l1.getBlockX(), l2.getBlockX());

		int higherZ = Math.max(l1.getBlockZ(), l2.getBlockZ());
		int lowerZ = Math.min(l1.getBlockZ(), l2.getBlockZ());

		Location l1 = new Location(getWorld(), higherX, 0, higherZ);
		Location l2 = new Location(getWorld(), higherX, 0, lowerZ);
		Location l3 = new Location(getWorld(), lowerX, 0, higherZ);
		Location l4 = new Location(getWorld(), lowerX, 0, lowerZ);

		if (pa.contains(l1) || pa.contains(l2) || pa.contains(l3) || pa.contains(l4))
			return true;

		return false;
	}

	@Override
	public World getWorld() {
		return l1.getWorld();
	}

	@Override
	public boolean addPermitArea(ContainablePermitArea pa) {

		for (PermitArea ch : this.getChildren().values())
			if (ch.intersects(pa))
				return false;

		return super.addPermitArea(pa);
	}

	public Location getLocation1() {
		return l1;
	}

	public Location getLocation2() {
		return l2;
	}

	public static LocationPermitArea loadPermitArea(String name, World w, ConfigurationSection cs)
			throws InvalidConfigurationException {

		if (!cs.isConfigurationSection("prices"))
			throw new InvalidConfigurationException("No prices section!");

		PriceList pl = InertPriceList.loadFromConfigurationSection(cs
				.getConfigurationSection("prices"));

		if (!cs.contains("x1") || !cs.contains("z1") || !cs.contains("x2") || !cs.contains("z2"))
			throw new InvalidConfigurationException("Missing coordinates!");

		Location l1 = new Location(w, cs.getInt("x1"), 0, cs.getInt("z1"));
		Location l2 = new Location(w, cs.getInt("x2"), 0, cs.getInt("z2"));

		LocationPermitArea area = new LocationPermitArea(l1, l2, name, pl);

		area.setAllowMiningUnspecifiedBlocks(cs.getBoolean("allowMiningUnspecifiedBlocks"));
		area.setEffectiveDepth(cs.getInt("effectiveDepth"));

		ConfigurationSection children = cs.getConfigurationSection("children");

		if (children != null) {

			for (String s : children.getKeys(false))
				if (children.isConfigurationSection(s)) {
					ConfigurationSection child = children.getConfigurationSection(s);
					String childRep = child.getCurrentPath().replaceAll(".children", "");
					try {
						MinePermit.log.info("Loading PermitArea " + childRep);
						area.addPermitArea(LocationPermitArea.loadPermitArea(s, w, child));
						MinePermit.log.info("Finished loading PermitArea " + childRep);
					} catch (InvalidConfigurationException e) {
						MinePermit.log.severe("Error loading PermitArea " + childRep);
						e.printStackTrace();
					}
				}

		}

		MinePermit.log.info("Finished loading PermitArea " + name);

		return area;
	}

	@Override
	public void saveToConfiguration(ConfigurationSection cs) {
		cs.set("x1", l1.getBlockX());
		cs.set("z1", l1.getBlockZ());
		cs.set("x2", l2.getBlockX());
		cs.set("z2", l2.getBlockZ());

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

	@Override
	public boolean contains(ContainablePermitArea pa) {

		if (pa instanceof LocationPermitArea) {

			LocationPermitArea contained = (LocationPermitArea) pa;

			int higherX = Math.max(contained.l1.getBlockX(), contained.l2.getBlockX());
			int lowerX = Math.min(contained.l1.getBlockX(), contained.l2.getBlockX());

			int higherZ = Math.max(contained.l1.getBlockZ(), contained.l2.getBlockZ());
			int lowerZ = Math.min(contained.l1.getBlockZ(), contained.l2.getBlockZ());

			Location l1 = new Location(getWorld(), higherX, 0, higherZ);
			Location l2 = new Location(getWorld(), higherX, 0, lowerZ);
			Location l3 = new Location(getWorld(), lowerX, 0, higherZ);
			Location l4 = new Location(getWorld(), lowerX, 0, lowerZ);

			if (contains(l1) && contains(l2) && contains(l3) && contains(l4))
				return true;
		}

		return false;
	}

}

package net.minecore.minepermit.world;

import java.security.InvalidParameterException;

import net.minecore.minepermit.price.InertPriceList;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class LocationPermitArea extends ContainablePermitArea {
	
	private Location l1, l2;

	public LocationPermitArea(Location l1, Location l2, String name, PriceList pl) {
		
		super(name, pl);
		
		if(!l1.getWorld().equals(l2.getWorld()))
			throw new InvalidParameterException("Locations must be in same world!");
		
		this.l1 = l1;
		this.l2 = l2;
	}

	@Override
	public boolean contains(Location l) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public World getWorld() {
		return l1.getWorld();
	}

	@Override
	public boolean addPermitArea(ContainablePermitArea pa) {
		
		
		
		//TODO: Check contains in this but not children.
		
		return super.addPermitArea(pa);
	}
	
	public Location getLocation1(){
		return l1;
	}
	
	public Location getLocation2(){
		return l2;
	}
	
	public static LocationPermitArea loadPermitArea(String name, World w, ConfigurationSection cs) throws InvalidConfigurationException {
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
		
		if(!cs.contains("x1") || !cs.contains("z1") || !cs.contains("x2") || !cs.contains("z2"))
			throw new InvalidConfigurationException("Missing coordinates!");
		
		Location l1 = new Location(w, cs.getInt("x1"), 0, cs.getInt("z1"));
		Location l2 = new Location(w, cs.getInt("x2"), 0, cs.getInt("z2"));
		
		LocationPermitArea area = new LocationPermitArea(l1, l2, name, pl);

		ConfigurationSection children = cs.getConfigurationSection("children");

		if (children != null) {

			for (String s : children.getKeys(false))
				if (cs.isConfigurationSection(s))
					area.addPermitArea(LocationPermitArea.loadPermitArea(s,
							w, cs.getConfigurationSection(s)));

		}

		return area;
	}

}

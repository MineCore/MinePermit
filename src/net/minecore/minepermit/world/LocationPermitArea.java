package net.minecore.minepermit.world;

import java.security.InvalidParameterException;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationPermitArea extends PermitArea {
	
	private String name;
	private Location l1, l2;

	public LocationPermitArea(Location l1, Location l2, String name, PriceList pl) {
		
		super(name, pl);
		
		if(!l1.getWorld().equals(l2.getWorld()))
			throw new InvalidParameterException("Locations must be in same world!");
		
		this.name = name;
		this.l1 = l1;
		this.l2 = l2;
	}

	@Override
	public boolean contains(Location l) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public World getWorld() {
		return l1.getWorld();
	}

	@Override
	public boolean addPermitArea(PermitArea pa) {
		
		if(pa == null)
			throw new InvalidParameterException("You cannot add a null permit area!");
		
		if(pa instanceof WorldPermitArea)
			return false;
		
		if(getPermitArea(pa.getName()) != null)
			return false;
		
		//TODO: Check contains in this but not children.
		
		getChildren().put(pa.getName(), pa);
		return true;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}
	
	public Location getLocation1(){
		return l1;
	}
	
	public Location getLocation2(){
		return l2;
	}

}

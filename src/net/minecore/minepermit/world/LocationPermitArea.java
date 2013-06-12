package net.minecore.minepermit.world;

import java.security.InvalidParameterException;
import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationPermitArea extends PermitArea {
	
	private String name;

	public LocationPermitArea(String name, PriceList pl) {
		
		super(name, pl);
		
		this.name = name;
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
		// TODO Auto-generated method stub
		return null;
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

}

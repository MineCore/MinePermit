package net.minecore.minepermit.world;

import java.security.InvalidParameterException;
import java.util.Map;

import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;

public class WorldPermitArea extends PermitArea {

	private World w;
	private PriceList pl;
	private Map<String, PermitArea> children;

	public WorldPermitArea(World w, PriceList pl) {
		
		if(w == null)
			throw new InvalidParameterException("World must not be null!");
		
		if(pl == null)
			throw new InvalidParameterException("PriceList can not be null!");
		
		this.w = w;
		this.pl = pl;
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
	public PriceList getPrices() {
		return pl;
	}

	@Override
	public World getWorld() {
		return w;
	}
	
	@Override
	public PermitArea getRelevantPermitArea(Location l) {
		if(!contains(l))
			return null;
		
		for(String s : children.keySet()){
			PermitArea pa = children.get(s).getRelevantPermitArea(l);
			
			if(pa != null)
				return pa;
			
		}
		
		return this;
	}

	@Override
	public boolean addPermitArea(PermitArea pa) {
		
		if(pa == null)
			throw new InvalidParameterException("You cannot add a null permit area!");
		
		if(getPermitArea(pa.getName()) != null)
			return false;
		
		if(pa instanceof WorldPermitArea)
			return false;
		
		//TODO: Check contains in this but not children.
		
		children.put(pa.getName(), pa);
		return true;
	}

	@Override
	public PermitArea removePermitArea(String name) {
		return children.remove(name);
	}

	@Override
	public PermitArea getPermitArea(String name) {
		return children.get(name);
	}

}

package net.minecore.minepermit.world;

import net.minecore.minepermit.price.PriceList;

import org.bukkit.Location;
import org.bukkit.World;

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
		//Can't change name.
	}

	@Override
	public boolean addPermitArea(ContainablePermitArea pa) {
		
		if(!pa.getWorld().equals(w))
			return false;
		
		//TODO: Check contains in this but not children.
		
		return super.addPermitArea(pa);
	}

}

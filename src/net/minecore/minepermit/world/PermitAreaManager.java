package net.minecore.minepermit.world;

import java.util.TreeMap;

import net.minecore.minepermit.MinePermit;

public class PermitAreaManager {
	
	private MinePermit mp;
	private TreeMap<String, PermitArea> areas;

	public PermitAreaManager(MinePermit mp){
		this.mp = mp;
		
		areas = new TreeMap<String,PermitArea>();
	}
	
	public boolean addPermitArea(PermitArea pa){
		
		if(pa instanceof WorldPermitArea){
			if(areas.containsKey(pa.getWorld()))
				return false;
			
			areas.put(pa.getWorld().getName(), pa);
			
			return true;
		}
		
		return true;
		
	}

}

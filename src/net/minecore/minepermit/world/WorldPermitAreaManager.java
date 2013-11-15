package net.minecore.minepermit.world;

import java.util.TreeMap;

import net.minecore.minepermit.MinePermit;
import net.minecore.minepermit.price.InertPriceList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldPermitAreaManager {
	
	private MinePermit mp;
	private TreeMap<String, WorldPermitArea> areas;

	public WorldPermitAreaManager(MinePermit mp){
		this.mp = mp;
		
		areas = new TreeMap<String, WorldPermitArea>();
	}
	
	public boolean addPermitArea(PermitArea pa){
		
		if(pa instanceof WorldPermitArea){
			if(areas.containsKey(pa.getWorld()))
				return false;
			
			areas.put(pa.getWorld().getName(), (WorldPermitArea) pa);
			
			return true;
		}
		
		return false;
		
	}
	
	public WorldPermitArea getWorldPermitArea(World world){
		WorldPermitArea wpa = areas.get(world);
		if (wpa == null){
			wpa = new WorldPermitArea(world, new InertPriceList());
			areas.put(world.getName(), wpa);
		}
		
		return wpa;
	}
	
	public WorldPermitArea getWorldPermitArea(String world){
		return getWorldPermitArea(Bukkit.getWorld(world));
	}
	
	public PermitArea getRelevantPermitArea(Location l){
		return getWorldPermitArea(l.getWorld()).getRelevantPermitArea(l);
	}

}

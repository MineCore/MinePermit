package net.minecore.minepermit.miner;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.world.PermitArea;

public class PermitHolder {
	
	private Map<Integer, Permit> permits;

	public PermitHolder(PermitArea pa) {
		permits = new TreeMap<Integer, Permit>();
	}
	
	public boolean addPermit(Permit p){
		
		refreshPermit(p.getBlockID());
		
		if(permits.containsKey(p.getBlockID()))
			return false;
		
		permits.put(p.getBlockID(), p);
		
		return true;
	}

	private void refreshPermit(int blockID) {
		Permit p = permits.get(blockID);
		if(p == null)
			return;
		
		if(!p.canStillBreakBlocks())
			permits.remove(blockID);
		
	}

	public boolean hasPermit(int id) {
		refreshPermit(id);
		return permits.containsKey(id);
	}

	public void removePermit(int blockID) {
		if(hasPermit(blockID)){
			permits.remove(blockID);
		}
		
	}

	public Permit getPermit(int blockID) {
		refreshPermit(blockID);
		return permits.get(blockID);
	}

	public void saveTo(ConfigurationSection cs) {
		for(String path : cs.getKeys(false))
			cs.set(path, null);
		
		for(Permit p : permits.values()){
			//TODO:
		}
			
	}

}

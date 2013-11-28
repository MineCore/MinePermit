package net.minecore.minepermit.miner;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.permits.UniversalPermit;
import net.minecore.minepermit.world.PermitArea;

public class PermitHolder {
	
	private Map<Integer, Permit> permits;
	private PermitArea pa;
	private UniversalPermit up;

	public PermitHolder(PermitArea pa) {
		this.pa = pa;
		permits = new TreeMap<Integer, Permit>();
	}

	public boolean hasUniversalPermit() {
		checkUniversalPermit();
		
		return up != null;
	}
	
	private void checkUniversalPermit(){
		if(!up.canStillBreakBlocks())
			up = null;
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

	public boolean removePermit(int blockID) {
		if(hasPermit(blockID)){
			permits.remove(blockID);
			return true;
		}
		return false;
	}

	public Permit getPermit(int blockID) {
		refreshPermit(blockID);
		return permits.get(blockID);
	}

	public void saveTo(ConfigurationSection cs) {
		for(String path : cs.getKeys(false))
			cs.set(path, null);
		
		for(Permit p : permits.values()){
			p.save(cs.createSection("" + p.getBlockID()));
		}
			
	}

	public boolean addUniversalPermit(UniversalPermit p) {
		checkUniversalPermit();
		if(hasUniversalPermit())
			return false;
		up = p;
		return true;
	}

}

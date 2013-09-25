package net.minecore.minepermit.miner;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import net.minecore.Miner;
import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.permits.UniversalPermit;
import net.minecore.minepermit.world.PermitArea;

public class PermitMiner {
	
	private Miner miner;
	private Map<String, PermitHolder> permits;
	private UniversalPermit up;
	

	public PermitMiner(Miner miner){
		this.miner = miner;
		permits = new TreeMap<String, PermitHolder>();
	}
	
	public String getPlayer(){
		return miner.getPlayerName();
	}
	
	public boolean hasPermit(int blockID){
		if(permits.containsKey(blockID) && checkTime(blockID) || hasUniversalPermit())
			return true;
		
		return false;
	}
	
	public boolean addPermit(PermitArea pa, Permit p){
		
		if(p instanceof UniversalPermit)
			return addUniversalPermit((UniversalPermit) p);
		
		PermitHolder ph = permits.get(pa.getStringRepresentation());
		
		return ph.addPermit(p);
		
	}
	
	private boolean addUniversalPermit(UniversalPermit p) {
		checkUniversalPermit();
		if(hasUniversalPermit())
			return false;
		up = p;
		return true;
	}

	public long getRemainingTime(int blockID){
		if(!hasPermit(blockID))
			return 0;
		return (permits.get(blockID) - System.currentTimeMillis()) / 60000L;
	}
	
	public Map<Integer, Long> getPermits(){
		return permits;
	}
	
	public void removePermit(int blockID){
		permits.remove(blockID);
	}
	
	public boolean checkTime(int id){
		if(permits.containsKey(id)){
			
			if(permits.get(id) - System.currentTimeMillis() <= 0){
				removePermit(id);
				return false;
			}
			
			return true;
		}
		return false;
	}

	public boolean hasUniversalPermit() {
		checkUniversalPermit();
		
		return up != null;
	}
	
	private void checkUniversalPermit(){
		if(!up.canStillBreakBlocks())
			up = null;
	}

	public void save() {
		ConfigurationSection cs = miner.getConfigurationSection("permits");
		
	}

}

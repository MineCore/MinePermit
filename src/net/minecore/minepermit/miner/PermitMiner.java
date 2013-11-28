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
	

	public PermitMiner(Miner miner){
		this.miner = miner;
		permits = new TreeMap<String, PermitHolder>();
	}
	
	public String getPlayer(){
		return miner.getPlayerName();
	}
	
	public boolean hasPermit(PermitArea pa, int blockID){		
		return getPermit(pa, blockID) != null;
	}
	
	public boolean addPermit(PermitArea pa, Permit p){
		
		if(p instanceof UniversalPermit)
			return addUniversalPermit(pa, (UniversalPermit) p);
		
		return getPermitHolder(pa).addPermit(p);
		
	}
	
	private boolean addUniversalPermit(PermitArea pa, UniversalPermit p) {
		return getPermitHolder(pa).addUniversalPermit(p);
	}
	
	public Map<String, PermitHolder> getPermits(){
		return permits;
	}
	
	public boolean removePermit(PermitArea pa, int blockID){
		return getPermitHolder(pa).removePermit(blockID);
	}
	
	public Permit getPermit(PermitArea pa, int blockID){
		return getPermitHolder(pa).getPermit(blockID);
	}
	
	public PermitHolder getPermitHolder(PermitArea pa){
		PermitHolder ph = permits.get(pa.getStringRepresentation());
		
		if(ph == null)
			permits.put(pa.getStringRepresentation(), ph = new PermitHolder(pa));
		
		return ph;
	}

	public void save() {
		ConfigurationSection cs = miner.getConfigurationSection("permits");
		for(String key : permits.keySet()){
			String tmp = key.replace('.', '-');
			ConfigurationSection child = cs.getConfigurationSection(tmp);
			if(child == null)
				child = cs.createSection(tmp);
			permits.get(key).saveTo(child);
		}
	}

}

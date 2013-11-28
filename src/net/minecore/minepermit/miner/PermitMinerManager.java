package net.minecore.minepermit.miner;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.entity.Player;

import net.minecore.minepermit.MinePermit;

public class PermitMinerManager {
	
	MinePermit mp;
	private Map<String, PermitMiner> miners;
	
	public PermitMinerManager(MinePermit mn)
	{
		mp = mn;
		miners = new TreeMap<String, PermitMiner>();
	}

	public PermitMiner getPermitMiner(String playerName) {

		PermitMiner m = miners.get(playerName);

		if (m == null)
			return loadMiner(playerName);

		return m;
	}
	
	public PermitMiner getPermitMiner(Player p) {
		return getPermitMiner(p.getName());
	}
	
	private PermitMiner loadMiner(String playerName) {
		
		PermitMiner pm = new PermitMiner(mp.getMineCore().getMiner(playerName));
		miners.put(playerName, pm);
		
		return pm;
	}
	
	public void savePlayerData() {
		
		for(PermitMiner pm : miners.values()){
			pm.save();
			mp.log.info("Data for " + mp.getName() + " saved");
		}
		
	}


}

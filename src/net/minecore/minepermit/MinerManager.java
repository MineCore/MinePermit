package net.minecore.minepermit;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MinerManager {
	
	MinePermit mp;
	Config conf;
	public MinerManager(MinePermit mn,Config c)
	{
		conf = c;
		mp = mn;
		miners = new TreeMap<String, Miner>();
	}

	
	private Map<String, Miner> miners;

	public Miner getMiner(String playerName) {

		Miner m = miners.get(playerName);

		if (m == null) {
			return addNewMiner(playerName);
		}

		return m;

	}
	
	public Miner getMiner(Player p) {
		return getMiner(p.getName());
		
	}

	public Miner addNewMiner(String playerName) {
		return addNewMiner(Bukkit.getPlayerExact(playerName));

	}

	public Miner addNewMiner(Player player) {
		return addMiner(new Miner(player.getName()));
	}

	public Miner addMiner(Miner player) {
		miners.put(player.getPlayer(), player);
		return player;
	}
	
	public void savePlayers() {
		
		for(int y = 0; y < miners.size(); y++){
			Miner m = miners.get(miners.keySet().toArray()[y]);
			mp.log.info("Saving conf for " + m);
			conf.savePlayerConf(m);
			mp.log.info("Conf for " + m + " saved");
		}
		
	}


}

package net.minecore.minepermit;

import java.io.IOException;
import java.util.logging.Logger;

import net.minecore.Metrics;
import net.minecore.MineCore;
import net.minecore.minepermit.miner.PermitMinerManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	private PermitMinerManager mm;
	
	public Logger log;
	private FileConfiguration conf;
	
	@Override
	public void onLoad()
	{
		log = getLogger();
		mm = new PermitMinerManager(this);
		
		conf = this.getConfig();
		
	}

	@Override
	public void onDisable() {

		log.info("Saving players...");
		mm.savePlayerData();
		log.info("Players saved!");
		log.info("MinePermit Disabled");

	}

	@Override
	public void onEnable() {

		//Register the blockListener
		this.getServer().getPluginManager().registerEvents(new BlockListener(mm), this);
		
		//Set the command executer
		getCommand("permit").setExecutor(new CommandInterpreter(this,mm));
		
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		log.info("MinePermit Enabled!");

	}
	
	public MineCore getMineCore() {
		return (MineCore)getServer().getPluginManager().getPlugin("MineCore");
	}

}

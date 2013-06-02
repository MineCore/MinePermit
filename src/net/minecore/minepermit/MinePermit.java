package net.minecore.minepermit;

import java.io.IOException;
import java.util.logging.Logger;

//import net.milkbowl.vault.economy.Economy;
//import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	private MinerManager mm;
	private Config c;
	
	public Logger log = Logger.getLogger("minecraft");
	//public static Economy econ;
	FileConfiguration conf;
	//public static Permission perm;
	@Override
	public void onLoad()
	{
		mm = new MinerManager(this, c);
	}

	@Override
	public void onDisable() {

		log.info("[MinePermit] Saving players...");
		mm.savePlayers();
		log.info("[MinePermit] Players saved!");
		log.info("MinePermit Disabled");

	}

	@Override
	public void onEnable() {

		//Register the blockListener
		this.getServer().getPluginManager()
				.registerEvents(new BlockListener(mm), this);
		
		//load settings from conf

		//Load economy plugins
		
		//Load permissions plugins
		//Set the command executer
		getCommand("permit").setExecutor(new CommandInterpreter(this,mm));

		log.info("MinePermit Enabled");

	}

}

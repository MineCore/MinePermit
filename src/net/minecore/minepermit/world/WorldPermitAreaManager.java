package net.minecore.minepermit.world;

import java.util.TreeMap;

import net.minecore.minepermit.price.InertPriceList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class WorldPermitAreaManager {

	private final TreeMap<String, WorldPermitArea> areas;

	public WorldPermitAreaManager() {

		areas = new TreeMap<String, WorldPermitArea>();
	}

	public boolean addPermitArea(PermitArea pa) {

		if (pa instanceof WorldPermitArea) {
			if (areas.containsKey(pa.getWorld()))
				return false;

			areas.put(pa.getWorld().getName(), (WorldPermitArea) pa);

			return true;
		}

		return false;

	}

	public WorldPermitArea getWorldPermitArea(World world) {
		WorldPermitArea wpa = areas.get(world);
		if (wpa == null) {
			wpa = new WorldPermitArea(world, new InertPriceList());
			areas.put(world.getName(), wpa);
		}

		return wpa;
	}

	public WorldPermitArea getWorldPermitArea(String world) {
		return getWorldPermitArea(Bukkit.getWorld(world));
	}

	public PermitArea getRelevantPermitArea(Location l) {
		return getWorldPermitArea(l.getWorld()).getRelevantPermitArea(l);
	}

	public static WorldPermitAreaManager loadFromConfigurationSection(ConfigurationSection root) {

		WorldPermitAreaManager am = new WorldPermitAreaManager();

		for (String worldKey : root.getKeys(false)) {

			ConfigurationSection worldCS = root.getConfigurationSection(worldKey);

			WorldPermitArea worldPA = new WorldPermitArea(Bukkit.getWorld(worldKey),
					InertPriceList.loadFromConfigurationSection(worldCS
							.getConfigurationSection("prices")));

			worldPA.setEffectiveDepth(worldCS.getInt("effectiveDepth"));
			worldPA.setAllowMiningUnspecifiedBlocks(worldCS
					.getBoolean("allowMiningUnspecifiedBlocks"));

		}

		return am;
	}
}

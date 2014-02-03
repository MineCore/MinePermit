package net.minecore.minepermit.miner;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.minecore.minepermit.permits.Permit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class PermitHolder {

	private final Map<Material, Permit> permits;
	private Permit universalPermit;

	public PermitHolder() {
		permits = new TreeMap<Material, Permit>();
	}

	public boolean hasUniversalPermit() {
		checkUniversalPermit();

		return universalPermit != null;
	}

	private void checkUniversalPermit() {
		if (!universalPermit.canStillBreakBlocks())
			universalPermit = null;
	}

	public boolean addPermit(Permit p) {

		refreshPermit(p.getMaterial());

		if (permits.containsKey(p.getMaterial()))
			return false;

		permits.put(p.getMaterial(), p);

		return true;
	}

	private void refreshPermit(Material material) {
		Permit p = permits.get(material);

		if (p != null && !p.canStillBreakBlocks())
			permits.remove(material);

	}

	public boolean hasPermit(Material m) {
		refreshPermit(m);
		return permits.containsKey(m);
	}

	public boolean removePermit(Material m) {
		return permits.remove(m) != null;
	}

	public Permit getPermit(Material m) {
		refreshPermit(m);
		return permits.get(m);
	}

	public void saveTo(ConfigurationSection cs) {
		for (String path : cs.getKeys(false))
			cs.set(path, null);

		for (Permit p : permits.values()) {
			p.save(cs.createSection(p.getMaterial().name()));
		}

	}

	public boolean addUniversalPermit(Permit p) {
		checkUniversalPermit();
		if (hasUniversalPermit())
			return false;
		universalPermit = p;
		return true;
	}

	public Permit getUniversalPermit() {
		return universalPermit;
	}

	public Collection<Permit> getPermits() {
		return permits.values();
	}

	public static PermitHolder createPermitHolderFromConfigurationSection(ConfigurationSection cs) {
		PermitHolder ph = new PermitHolder();

		return ph;
	}

}

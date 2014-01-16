package net.minecore.minepermit.miner;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.permits.UniversalPermit;
import net.minecore.minepermit.world.PermitArea;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class PermitHolder {

	private final Map<Material, Permit> permits;
	private final PermitArea pa;
	private UniversalPermit up;

	public PermitHolder(PermitArea pa) {
		this.pa = pa;
		permits = new TreeMap<Material, Permit>();
	}

	public boolean hasUniversalPermit() {
		checkUniversalPermit();

		return up != null;
	}

	private void checkUniversalPermit() {
		if (!up.canStillBreakBlocks())
			up = null;
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
		if (p == null)
			return;

		if (!p.canStillBreakBlocks())
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

	public boolean addUniversalPermit(UniversalPermit p) {
		checkUniversalPermit();
		if (hasUniversalPermit())
			return false;
		up = p;
		return true;
	}

	/**
	 * @return the pa
	 */
	public PermitArea getPermitArea() {
		return pa;
	}

	public UniversalPermit getUniversalPermit() {
		return up;
	}

	public Collection<Permit> getPermits() {
		return permits.values();
	}

}

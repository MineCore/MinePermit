package net.minecore.minepermit.permits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class TimedPermit extends Permit {

	private long end_time;

	/**
	 * Creates a new TimedPermit with time seconds remaining from this point in
	 * time
	 * 
	 * @param blockID
	 * @param time
	 */
	public TimedPermit(Material m, long time) {
		super(PermitType.TIMED, m);

		end_time = System.currentTimeMillis() + time * 1000L;
	}

	public TimedPermit(ConfigurationSection perm) {
		this(Material.matchMaterial(perm.getString("material")), perm.getInt("time"));
	}

	public long getRemainingTime() {
		long remaining = end_time - System.currentTimeMillis();
		if (remaining <= 0) {
			end_time = 0;
			return 0;
		}

		return remaining / 1000L;
	}

	@Override
	public void save(ConfigurationSection cs) {
		cs.set("type", getPermitType());
		cs.set("material", getMaterial());
		cs.set("time", getRemainingTime());
	}

	@Override
	public boolean canStillBreakBlocks() {
		return getRemainingTime() > 0;
	}

	@Override
	public String toString() {
		return "TimedPermit with " + getRemainingTime() + " seconds remaining";
	}

	@Override
	public boolean breakBlock(Block block) {
		return canStillBreakBlocks();
	}

}

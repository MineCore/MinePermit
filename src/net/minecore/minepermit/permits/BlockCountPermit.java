package net.minecore.minepermit.permits;

import java.security.InvalidParameterException;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class BlockCountPermit extends Permit {

	private int count;

	public BlockCountPermit(Material m, int count) {
		super(PermitType.COUNTED, m);
		this.count = count;
	}

	public void setRemainingBlocks(int count) {

		if (count < 0)
			throw new InvalidParameterException("Count cannot be less than 0!");

		this.count = count;
	}

	public int getRemainingBlocks() {
		return count;
	}

	public boolean mineBlock() {
		if (count == 0)
			return false;

		count--;
		return true;
	}

	@Override
	public void save(ConfigurationSection cs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canStillBreakBlocks() {
		return count > 0;
	}

	@Override
	public String toString() {
		return "BlockCountPermit with " + count + " blocks remaining";
	}

}

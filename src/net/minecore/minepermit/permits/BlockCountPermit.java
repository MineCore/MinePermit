package net.minecore.minepermit.permits;

import java.security.InvalidParameterException;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class BlockCountPermit extends Permit {

	private int count;

	public BlockCountPermit(Material m, int count) {
		super(PermitType.COUNTED, m);
		this.count = count;
	}

	public BlockCountPermit(ConfigurationSection perm) {
		this(Material.matchMaterial(perm.getString("material")), perm.getInt("amt"));
	}

	public void setRemainingBlocks(int count) {

		if (count < 0)
			throw new InvalidParameterException("Count cannot be less than 0!");

		this.count = count;
	}

	public int getRemainingBlocks() {
		return count;
	}

	@Override
	public void save(ConfigurationSection cs) {
		cs.set("type", getPermitType());
		cs.set("material", getMaterial());
		cs.set("amt", getRemainingBlocks());

	}

	@Override
	public boolean canStillBreakBlocks() {
		return count > 0;
	}

	@Override
	public String toString() {
		return "BlockCountPermit with " + count + " blocks remaining";
	}

	@Override
	public boolean breakBlock(Block block) {
		if (count == 0)
			return false;

		count--;
		return true;
	}

}

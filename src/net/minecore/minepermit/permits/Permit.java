/**
 * 
 */
package net.minecore.minepermit.permits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author The Numenorean
 * 
 */
public abstract class Permit {

	private final PermitType type;
	private Material material;

	public Permit(PermitType type, Material m) {
		this.material = m;
		this.type = type;
	}

	public Material getMaterial() {
		return material;
	}

	public void setBlock(Material m) {
		this.material = m;
	}

	public PermitType getPermitType() {
		return type;
	}

	/**
	 * Saves the Permit to the given ConfigurationSection
	 * 
	 * @param cs
	 *            A configuration section
	 */
	public abstract void save(ConfigurationSection cs);

	/**
	 * Gets whether the Permit is still valid and can let the player mine the
	 * block type
	 * 
	 * @return True if it is still valid.
	 */
	public abstract boolean canStillBreakBlocks();

	/**
	 * Atempts to break a block. If the permit cannot break the block, this will
	 * return false.
	 * 
	 * @param block
	 *            The block to mine
	 * @return True if the player should be allowed to mine the block.
	 */
	public abstract boolean breakBlock(Block block);

}

/**
 * 
 */
package net.minecore.minepermit.permits;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author The Numenorean
 *
 */
public abstract class Permit {
	
	private int blockID;

	public Permit(int blockID){
		this.blockID = blockID;
	}

	public int getBlockID() {
		return blockID;
	}

	public void setBlockID(int blockID) {
		this.blockID = blockID;
	}
	
	public abstract void save(ConfigurationSection cs);

	public abstract boolean canStillBreakBlocks();
	
}

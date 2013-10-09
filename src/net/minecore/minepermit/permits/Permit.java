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
	private PermitType type;

	public Permit(PermitType type, int blockID){
		this.blockID = blockID;
		this.type = type;
	}

	public int getBlockID() {
		return blockID;
	}

	public void setBlockID(int blockID) {
		this.blockID = blockID;
	}
	
	public PermitType getPermitType(){
		return type;
	}
	
	public abstract void save(ConfigurationSection cs);

	public abstract boolean canStillBreakBlocks();
	
}

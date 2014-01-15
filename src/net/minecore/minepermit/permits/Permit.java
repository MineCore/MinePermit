/**
 * 
 */
package net.minecore.minepermit.permits;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author The Numenorean
 *
 */
public abstract class Permit {
	
	private PermitType type;
	private Material material;

	public Permit(PermitType type, Material m){
		this.material = m;
		this.type = type;
	}

	public Material getBlock() {
		return material;
	}

	public void setBlock(Material m) {
		this.material = m;
	}
	
	public PermitType getPermitType(){
		return type;
	}
	
	public abstract void save(ConfigurationSection cs);

	public abstract boolean canStillBreakBlocks();
	
}

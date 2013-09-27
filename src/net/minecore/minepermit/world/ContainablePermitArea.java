package net.minecore.minepermit.world;

import net.minecore.minepermit.price.PriceList;

public abstract class ContainablePermitArea extends PermitArea {

	private PermitArea parent;

	public ContainablePermitArea(String name, PriceList pl) {
		super(name, pl);
	}
	
	public void setParent(PermitArea pa){
		parent = pa;
	}
	
	public PermitArea getParent(){
		return parent;
	}
	
	@Override
	public String getStringRepresentation(){
		if(parent == null)
			return getName();
		
		return parent.getStringRepresentation() + '.' + getName();
	}
}

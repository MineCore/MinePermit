package net.minecore.minepermit.permits;

public class UniversalBlockCountPermit extends BlockCountPermit implements UniversalPermit {

	public UniversalBlockCountPermit(int count) {
		super(-1, count);
	}

}

package net.minecore.minepermit.permits;

public class UniversalTimedPermit extends TimedPermit implements UniversalPermit {

	public UniversalTimedPermit(long time) {
		super(null, time);
	}

}

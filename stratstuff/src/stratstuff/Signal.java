package stratstuff;

public abstract class Signal extends MicroObject {

	public Signal(WorldPoint source) {

	}
	
	@Override
	public void update()
	{
		checkForReceivers();
	}

	protected abstract void checkForReceivers();

}

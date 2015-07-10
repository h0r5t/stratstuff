package stratstuff;

public abstract class Signal extends MicroObject {
	
	protected String message;

	public Signal(WorldPoint source, String message) {

	}
	
	public String getMessage()
	{
		return message;
	}
	
	@Override
	public void update()
	{
	}

	protected abstract void checkForReceivers();

}

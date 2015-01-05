package stratstuff;

public class SimpleTimer {

	private long time;

	public SimpleTimer() {

	}

	public void start() {
		time = System.currentTimeMillis();
	}

	public long stop() {
		return System.currentTimeMillis() - time;
	}
}

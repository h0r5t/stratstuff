package stratstuff;

public class IdleTask extends Task {
	// this task is used by the frontend for whatever action requires idling for
	// some time

	private double idleMillis;
	private TaskManager mgr;

	public IdleTask(TaskManager manager, double idleMillis) {
		this.idleMillis = idleMillis;
		this.mgr = manager;
	}

	@Override
	public void update() {
		idleMillis -= GameSettings.TICK_MILLIS;
		if (idleMillis <= 0) {
			mgr.addToDelete(this);
		}
	}

}

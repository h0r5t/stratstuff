package stratstuff;

public class MoveTask extends Task {

	private MovingObject object;
	private MovePath path;
	private TaskManager taskManager;

	public MoveTask(World world, TaskManager taskManager, MovingObject o,
			WorldPoint target) {
		this.object = o;
		this.taskManager = taskManager;
		path = new MovePath(this, world, o.getPosition(), target);
		object.updateRotation(path.seeNext());
	}

	@Override
	public void update() {
		if (object.hasTurned()) {
			WorldPoint next = path.getNext();
			if (next != null) {
				object.moveTo(next);
				object.updateRotation(path.seeNext());
			} else {
				taskManager.addToDelete(this);
			}
		}
	}

}

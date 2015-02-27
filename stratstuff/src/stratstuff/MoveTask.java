package stratstuff;

public class MoveTask extends Task {

	private MovingObject object;
	private MovePath path;
	private Core main;

	public MoveTask(Core main, MovingObject o, WorldPoint target) {
		this.object = o;
		this.main = main;
		path = new MovePath(this, main.getTaskManager(), main.getWorld(),
				o.getPosition(), target);
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
				main.getTaskManager().addToDelete(this);
			}
		}
	}

}

package stratstuff;

public class MoveTask extends Task {

	private MovingObject object;
	private MovePath path;
	private Main main;

	public MoveTask(Main main, MovingObject o, WorldPoint target) {
		this.object = o;
		this.main = main;
		path = new MovePath(main.getTaskManager(), main.getWorld(),
				o.getPosition(), target);
	}

	@Override
	public void update() {
		WorldPoint next = path.getNext();
		if (next != null) {
			main.getWorld().moveObjectTo(object, next);
		} else {
			main.getTaskManager().addToDelete(this);
		}
	}

}

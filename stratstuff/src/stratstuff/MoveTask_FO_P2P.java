package stratstuff;

public class MoveTask_FO_P2P extends Task {

	private Galaxy galaxy;
	private FloatingObject object;
	private SpacePosition targetPos;
	private TaskManager taskManager;
	private double vectorX, vectorY;
	private boolean finished = false;
	private DynamicTexture dynamicTexture;

	public MoveTask_FO_P2P(Galaxy galaxy, TaskManager taskManager,
			FloatingObject object, SpacePosition targetPos) {
		this.object = object;
		this.galaxy = galaxy;
		this.targetPos = targetPos;
		this.taskManager = taskManager;

		double[] vector = object.getPosition().calculateVectorTo(
				targetPos.getMacX(), targetPos.getMacY(), 10);
		vectorX = vector[0];
		vectorY = vector[1];

		if (object instanceof Starship) {
			dynamicTexture = ((Starship) object).getDynamicTexture();
			dynamicTexture.turnDirectionVectorSpace(object.getPosition(),
					targetPos);
		}
	}

	@Override
	public void update() {
		dynamicTexture.update();
		if (dynamicTexture != null && !dynamicTexture.hasTurned()) {
			return;
		}
		if (finished)
			return;
		followVector();
		checkIfReachedTarget();
	}

	private void checkIfReachedTarget() {
		if (object.getPosition().isNearTo(targetPos, 10)) {
			finished = true;
			taskManager.addToDelete(this);
		}
	}

	private void followVector() {
		boolean stillInBounds = object.getPosition().moveBy(vectorX, vectorY,
				galaxy, object);
		if (!stillInBounds) {
			finished = true;
			taskManager.addToDelete(this);
		}

	}

}

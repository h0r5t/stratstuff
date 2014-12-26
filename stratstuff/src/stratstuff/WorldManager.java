package stratstuff;

public class WorldManager implements Updatable {

	private World world;

	public WorldManager(World world) {
		this.world = world;
	}

	// this class will be able to only update certain edges in the world
	// graph
	// as this will massively increase performance!
	// Thus, all changes in the world graph have to happen through the World
	// class (method reportWorldPointChange()).

	public void changeGround(WorldPoint p, int groundID) {
		p.setGround(groundID);
		updateEdges(p);
	}

	public void attachElement(WorldPoint p, int elementID) {
		p.attachElement(elementID);
		updateEdges(p);
	}

	public void setOnlyElement(WorldPoint p, int elementID) {
		// TODO
		updateEdges(p);
	}

	// update all edges around (4 for non-diagonal movement, 8 for diagonal
	// movement)
	// This method is called at the initial world creation !AND! when a node
	// changes
	private void updateEdges(WorldPoint current) {
		// // from current
		// int z = current.getZ();
		// int x = current.getX();
		// int y = current.getY();
		// WorldPoint left = world.getWP(x, y, z);
		// WorldPoint right = world.getWP(x, y, z);
		// WorldPoint up = world.getWP(x, y, z);
		// WorldPoint down = world.getWP(x, y, z);
		//
		// GraphEdgeInfo a = new GraphEdgeInfo();
		//
		// if (x > 0) {
		// if (left.collides() == false) {
		// a.setLeft(new GraphEdge(current, left));
		// }
		// }
		//
		// if (x < GameSettings.WORLD_WIDTH) {
		// if (right.collides() == false) {
		// a.setRight(new GraphEdge(current, right));
		// }
		// }
		//
		// if (y > 0) {
		// if (up.collides() == false) {
		// a.setUp(new GraphEdge(current, up));
		// }
		// }
		//
		// if (y < GameSettings.WORLD_HEIGHT) {
		// if (down.collides() == false) {
		// a.setDown(new GraphEdge(current, down));
		// }
		// }
		//
		// current.outgoingEdges = a;
		//
		// // tocurrent
	}

	// Do this only after initial setup of all WorldPoints
	public void initialCreationOfEdges() {
		int z = 0;
		for (int x = 0; x < GameSettings.WORLD_WIDTH; x++) {
			for (int y = 0; y < GameSettings.WORLD_HEIGHT; y++) {
				WorldPoint current = world.getWP(x, y, z);

				GraphEdgeInfo a = new GraphEdgeInfo(current);

				if (current.collides()) {
					world.applyEdgeArray(a);
				} else {
					if (x > 0) {
						WorldPoint left = world.getWP(x - 1, y, z);
						if (left.collides() == false) {
							a.setLeft(true);
						}
					}

					if (x < GameSettings.WORLD_WIDTH - 1) {
						WorldPoint right = world.getWP(x + 1, y, z);
						if (right.collides() == false) {
							a.setRight(true);
						}
					}

					if (y > 0) {
						WorldPoint up = world.getWP(x, y - 1, z);
						if (up.collides() == false) {
							a.setUp(true);
						}
					}

					if (y < GameSettings.WORLD_HEIGHT - 1) {
						WorldPoint down = world.getWP(x, y + 1, z);
						if (down.collides() == false) {
							a.setDown(true);
						}
					}

					world.applyEdgeArray(a);
				}

			}
		}

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}

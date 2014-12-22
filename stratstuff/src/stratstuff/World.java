package stratstuff;

import pathfinder.Graph;

public class World extends Graph implements Updatable {

	private boolean worldChanged = true;

	// this boolean is used to be able to only update certain edges in the world
	// graph
	// as this will massively increase performance!
	// Thus, all changes in the world graph have to happen through the World
	// class (method updateEdge()).

	public World() {

	}

	public void addWorldPoint(WorldPoint p) {
		addNode(p);
		updateEdges(p);
	}

	private void updateEdges(WorldPoint p) {
		// update all edges around (4 for non-diagonal movement, 8 for diagonal
		// movement)
		// This method is called at the initial world creation !AND! when a node
		// changes
	}

	@Override
	public void update() {

	}

}

package stratstuff;

import java.util.LinkedList;

import pathfinder.GraphNode;
import pathfinder.GraphSearch_Astar;

public class MovePath {

	private WorldPoint from;
	private WorldPoint to;
	private World world;
	private TaskManager mgr;
	private LinkedList<GraphNode> route;
	private int index = 0;

	public MovePath(TaskManager mgr, World w, WorldPoint from, WorldPoint to) {
		this.from = from;
		this.to = to;
		this.world = w;
		this.mgr = mgr;
		calculatePath();
	}

	private void calculatePath() {
		GraphSearch_Astar search = new GraphSearch_Astar(world);
		route = search.search(world.getID(from), world.getID(to));
	}

	public WorldPoint getNext() {
		if (index < route.size()) {
			GraphNode next = route.get(index);
			WorldPoint p = world.getWP((int) next.x(), (int) next.y(),
					(int) next.z());

			index++;
			return p;
		}

		return null;

	}
}

package stratstuff;

import java.util.LinkedList;

import pathfinder.GraphNode;
import pathfinder.GraphSearch_Astar;

public class MovePath {

	private WorldPoint from;
	private WorldPoint to;
	private World world;
	private TaskManager mgr;
	private Task myTask;
	private LinkedList<GraphNode> route;
	private int index = 0;

	public MovePath(Task myTask, TaskManager mgr, World w, WorldPoint from,
			WorldPoint to) {
		this.from = from;
		this.to = to;
		this.world = w;
		this.mgr = mgr;
		this.myTask = myTask;
		calculatePath();
	}

	public void recalculatePath(WorldPoint from) {
		this.from = from;
		calculatePath();
	}

	private void calculatePath() {
		GraphSearch_Astar search = new GraphSearch_Astar(world);
		route = search.search(world.getID(from), world.getID(to));
	}

	public WorldPoint getNext() {
		if (route == null) {
			// no route possible, this path will be deleted by its task
			myTask.setDeletionMessage("false");
			return null;
		} else {
			myTask.setDeletionMessage("true");
		}
		if (index < route.size()) {
			GraphNode next = route.get(index);
			WorldPoint p = world.getWP((int) next.x(), (int) next.y(),
					(int) next.z());

			// this changed since the calculation
			if (p.collides()) {
				GraphNode current = route.get(index - 1);
				WorldPoint c = world.getWP((int) current.x(),
						(int) current.y(), (int) current.z());
				recalculatePath(c);
				index = 0;
				return c;
			}

			index++;
			return p;
		}

		return null;

	}
}

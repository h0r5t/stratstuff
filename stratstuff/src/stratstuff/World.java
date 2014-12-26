package stratstuff;

import pathfinder.Graph;
import pathfinder.GraphEdge;

public class World extends Graph {

	private Integer[][][] worldPointArray;

	public World() {
		worldPointArray = new Integer[GameSettings.WORLD_DEPTH][GameSettings.WORLD_WIDTH][GameSettings.WORLD_HEIGHT];
	}

	public void addWorldPoint(WorldPoint p) {
		addNode(p);
		worldPointArray[p.getZ()][p.getX()][p.getY()] = p.id();
	}

	public WorldPoint getWP(int x, int y, int z) {
		return (WorldPoint) getNode(worldPointArray[z][x][y]);
	}

	public void applyEdgeArray(GraphEdgeInfo a) {
		WorldPoint out = a.getOutgoing();
		int x = out.getX();
		int y = out.getY();
		int z = out.getZ();

		if (out.myEdges.canLeft() && x > 0) {
			removeEdge(worldPointArray[z][x][y], worldPointArray[z][x - 1][y]);
		}

		if (out.myEdges.canRight() && x < GameSettings.WORLD_WIDTH) {
			removeEdge(worldPointArray[z][x][y], worldPointArray[z][x + 1][y]);
		}

		if (out.myEdges.canUp() && y > 0) {
			removeEdge(worldPointArray[z][x][y], worldPointArray[z][x][y - 1]);
		}

		if (out.myEdges.canDown() && y < GameSettings.WORLD_HEIGHT) {
			removeEdge(worldPointArray[z][x][y], worldPointArray[z][x][y + 1]);
		}

		if (a.canLeft() && x > 0) {
			addEdge(worldPointArray[z][x][y], worldPointArray[z][x - 1][y], 0);
		}

		if (a.canRight() && x + 1 < GameSettings.WORLD_WIDTH) {
			addEdge(worldPointArray[z][x][y], worldPointArray[z][x + 1][y], 0);
		}

		if (a.canDown() && y + 1 < GameSettings.WORLD_HEIGHT) {
			addEdge(worldPointArray[z][x][y], worldPointArray[z][x][y + 1], 0);
		}

		if (a.canUp() && y > 0) {
			addEdge(worldPointArray[z][x][y], worldPointArray[z][x][y - 1], 0);
		}

		out.myEdges = a;
	}

	public void debugPrintEdges(int x, int y, int z) {
		System.out.println("Edges for " + x + "," + y + "," + z);
		for (GraphEdge edge : this.getEdgeArray(worldPointArray[z][x][y])) {
			System.out.println("x:" + edge.to().x() + " y:" + edge.to().y());
		}
		System.out.println("     ----------");
	}
}

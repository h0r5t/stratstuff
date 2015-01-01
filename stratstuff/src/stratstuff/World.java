package stratstuff;

import java.util.HashMap;

import pathfinder.Graph;
import pathfinder.GraphEdge;

public class World extends Graph {

	private Integer[][][] worldPointArray;
	private HashMap<MovingObject, WorldPoint> objectMap;
	private Core main;

	public World(Core main) {
		worldPointArray = new Integer[GameSettings.WORLD_DEPTH][GameSettings.WORLD_WIDTH][GameSettings.WORLD_HEIGHT];
		objectMap = new HashMap<MovingObject, WorldPoint>();
		this.main = main;
	}

	public void addWorldPoint(WorldPoint p) {
		addNode(p);
		worldPointArray[p.getZ()][p.getX()][p.getY()] = p.id();
	}

	public WorldPoint getWP(int x, int y, int z) {
		return (WorldPoint) getNode(worldPointArray[z][x][y]);
	}

	public int getID(WorldPoint p) {
		return worldPointArray[p.getZ()][p.getX()][p.getY()];
	}

	public HashMap<MovingObject, WorldPoint> getObjectMap() {
		return objectMap;
	}

	private void addZEdgeInBothDirections(WorldPoint a, WorldPoint b) {
		addZEdge(a, b);
		addZEdge(b, a);
	}

	private void addZEdge(WorldPoint from, WorldPoint to) {
		int xfrom = from.getX();
		int yfrom = from.getY();
		int zfrom = from.getZ();
		int xto = to.getX();
		int yto = to.getY();
		int zto = to.getZ();

		addEdge(worldPointArray[zfrom][xfrom][yfrom],
				worldPointArray[zto][xto][yto], 0);
	}

	private void removeZEdgeInBothDirections(WorldPoint a, WorldPoint b) {
		removeZEdge(a, b);
		removeZEdge(b, a);
	}

	private void removeZEdge(WorldPoint from, WorldPoint to) {
		int xfrom = from.getX();
		int yfrom = from.getY();
		int zfrom = from.getZ();
		int xto = to.getX();
		int yto = to.getY();
		int zto = to.getZ();

		removeEdge(worldPointArray[zfrom][xfrom][yfrom],
				worldPointArray[zto][xto][yto]);
	}

	public void moveObjectTo(MovingObject o, WorldPoint p) {
		o.getPosition().removeObjectAttachment(o);
		p.attachMovingObject(o);
		objectMap.put(o, p);
	}

	public void spawnObject(MovingObject o, WorldPoint p) {
		objectMap.put(o, p);
		p.attachMovingObject(o);
		main.getUnitManager().addUnit(o);
	}

	public WorldPoint getObjectPosition(MovingObject o) {
		return objectMap.get(o);
	}

	private void applyEdgeInfo(GraphEdgeInfo a) {
		WorldPoint p = a.getOutgoing();
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();

		if (p.getMyEdges().canLeft() == false && x > 0) {
			if (a.canLeft()) {
				addEdge(worldPointArray[z][x][y], worldPointArray[z][x - 1][y],
						0);
			}

		}

		else if (p.getMyEdges().canLeft() && x > 0) {
			if (a.canLeft() == false) {
				removeEdge(worldPointArray[z][x][y],
						worldPointArray[z][x - 1][y]);
			}
		}

		if (p.getMyEdges().canRight() == false && x < GameSettings.WORLD_WIDTH) {
			if (a.canRight()) {
				addEdge(worldPointArray[z][x][y], worldPointArray[z][x + 1][y],
						0);
			}
		}

		else if (p.getMyEdges().canRight() && x < GameSettings.WORLD_WIDTH) {
			if (a.canRight() == false) {
				removeEdge(worldPointArray[z][x][y],
						worldPointArray[z][x + 1][y]);
			}
		}

		if (p.getMyEdges().canDown() == false && y < GameSettings.WORLD_HEIGHT) {
			if (a.canDown()) {
				addEdge(worldPointArray[z][x][y], worldPointArray[z][x][y + 1],
						0);
			}
		}

		else if (p.getMyEdges().canDown() && y < GameSettings.WORLD_HEIGHT) {
			if (a.canDown() == false) {
				removeEdge(worldPointArray[z][x][y],
						worldPointArray[z][x][y + 1]);
			}
		}

		if (p.getMyEdges().canUp() == false && y > 0) {
			if (a.canUp()) {
				addEdge(worldPointArray[z][x][y], worldPointArray[z][x][y - 1],
						0);
			}
		}

		else if (p.getMyEdges().canUp() && y > 0) {
			if (a.canUp() == false) {
				removeEdge(worldPointArray[z][x][y],
						worldPointArray[z][x][y - 1]);
			}
		}

		p.setMyEdges(a);
	}

	public String getEdgesString(int x, int y, int z) {
		String out = "";
		out += ("Edges for " + x + "," + y + "," + z + " :\n");
		for (GraphEdge edge : this.getEdgeArray(worldPointArray[z][x][y])) {
			if (edge.to().x() > x) {
				out += "right";
			}
			if (edge.to().x() < x) {
				out += "left";
			}
			if (edge.to().y() > y) {
				out += "down";
			}
			if (edge.to().y() < y) {
				out += "up";
			}
			out += ("x:" + edge.to().x() + " y:" + edge.to().y());
			out += "\n";
		}
		return out;
	}

	public void changeGround(WorldPoint p, int groundID) {
		boolean collided = p.collides();
		p.setGround(groundID);
		if (p.collides() && collided == false) {
			updateEdges(p, true);
		}
		if (p.collides() == false && collided) {
			updateEdges(p, false);
		}
		main.tellFrontend(FrontendMessaging.groundUpdate(groundID, p.getX(),
				p.getY(), p.getZ()));
	}

	@SuppressWarnings("deprecation")
	public void attachElement(boolean initial, WorldPoint p, int elementID) {
		boolean collided = p.collides();
		p.attachElement(elementID);

		if (initial == false) {
			if (p.collides() && collided == false) {
				updateEdges(p, true);
			}
			if (p.collides() == false && collided) {
				updateEdges(p, false);
			}
			if (Element.isLadderDown(elementID)) {
				addZEdgeInBothDirections(p,
						getWP(p.getX(), p.getY(), p.getZ() + 1));
				getWP(p.getX(), p.getY(), p.getZ() + 1).attachElement(
						Element.getByName("ladderup"));
			}
			if (Element.isLadderUp(elementID)) {
				addZEdgeInBothDirections(p,
						getWP(p.getX(), p.getY(), p.getZ() - 1));
				getWP(p.getX(), p.getY(), p.getZ() - 1).attachElement(
						Element.getByName("ladderdown"));
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void removeElementFromWP(WorldPoint p, int elementID) {
		boolean collided = p.collides();
		p.removeAttachedElement(elementID);
		if (p.collides() && collided == false) {
			updateEdges(p, true);
		}
		if (p.collides() == false && collided) {
			updateEdges(p, false);
		}
		if (Element.isLadderDown(elementID)) {
			removeZEdgeInBothDirections(p,
					getWP(p.getX(), p.getY(), p.getZ() + 1));
			getWP(p.getX(), p.getY(), p.getZ() + 1).removeAttachedElement(
					Element.getByName("ladderup"));
		}
		if (Element.isLadderUp(elementID)) {
			removeZEdgeInBothDirections(p,
					getWP(p.getX(), p.getY(), p.getZ() - 1));
			getWP(p.getX(), p.getY(), p.getZ() - 1).removeAttachedElement(
					Element.getByName("ladderdown"));
		}
	}

	public void setOnlyElement(WorldPoint p, int elementID) {
		// TODO
		// if (p.collides() && collided == false) {
		// updateEdges(p, true);
		// }
		// if (p.collides() == false && collided) {
		// updateEdges(p, false);
		// }
	}

	private void updateEdges(WorldPoint p, boolean nowCollides) {
		// update edges from p, then to p
		// if nowcollides, need to remove edges to and from
		// if !nowcollides, need to add edges to and from

		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();

		if (nowCollides) {
			// removeEdges
			WorldPoint current = p;
			WorldPoint left = getWP(x - 1, y, z);
			WorldPoint right = getWP(x + 1, y, z);
			WorldPoint down = getWP(x, y + 1, z);
			WorldPoint up = getWP(x, y - 1, z);
			GraphEdgeInfo a = new GraphEdgeInfo(current);
			applyEdgeInfo(a);

			a = left.getMyEdges().getCopy();
			a.setRight(false);
			applyEdgeInfo(a);

			a = right.getMyEdges().getCopy();
			a.setLeft(false);
			applyEdgeInfo(a);

			a = up.getMyEdges().getCopy();
			a.setDown(false);
			applyEdgeInfo(a);

			a = down.getMyEdges().getCopy();
			a.setUp(false);
			applyEdgeInfo(a);

		} else {
			// addEdges
			WorldPoint current = p;
			WorldPoint left = getWP(x - 1, y, z);
			WorldPoint right = getWP(x + 1, y, z);
			WorldPoint down = getWP(x, y + 1, z);
			WorldPoint up = getWP(x, y - 1, z);
			GraphEdgeInfo a = new GraphEdgeInfo(current);

			if (left.collides() == false) {
				a.setLeft(true);
			}
			if (right.collides() == false) {
				a.setRight(true);
			}
			if (down.collides() == false) {
				a.setDown(true);
			}
			if (up.collides() == false) {
				a.setUp(true);
			}
			applyEdgeInfo(a);

			// add edges to current

			a = left.getMyEdges().getCopy();
			if (left.collides() == false) {
				a.setRight(true);
			}
			applyEdgeInfo(a);

			a = right.getMyEdges().getCopy();
			if (right.collides() == false) {
				a.setLeft(true);
			}
			applyEdgeInfo(a);

			a = down.getMyEdges().getCopy();
			if (down.collides() == false) {
				a.setUp(true);
			}
			applyEdgeInfo(a);

			a = up.getMyEdges().getCopy();
			if (up.collides() == false) {
				a.setDown(true);
			}
			applyEdgeInfo(a);
		}
	}

	public void initialCreationOfEdges() {
		for (int z = 0; z < GameSettings.WORLD_DEPTH; z++) {
			for (int x = 0; x < GameSettings.WORLD_WIDTH; x++) {
				for (int y = 0; y < GameSettings.WORLD_HEIGHT; y++) {
					WorldPoint current = getWP(x, y, z);

					GraphEdgeInfo a = new GraphEdgeInfo(current);

					if (current.collides()) {
						applyEdgeInfo(a);
					} else {
						if (x > 0) {
							WorldPoint left = getWP(x - 1, y, z);
							if (left.collides() == false) {
								a.setLeft(true);
							}
						}

						if (x < GameSettings.WORLD_WIDTH - 1) {
							WorldPoint right = getWP(x + 1, y, z);
							if (right.collides() == false) {
								a.setRight(true);
							}
						}

						if (y > 0) {
							WorldPoint up = getWP(x, y - 1, z);
							if (up.collides() == false) {
								a.setUp(true);
							}
						}

						if (y < GameSettings.WORLD_HEIGHT - 1) {
							WorldPoint down = getWP(x, y + 1, z);
							if (down.collides() == false) {
								a.setDown(true);
							}
						}

						applyEdgeInfo(a);
					}

				}
			}
		}

	}
}

package stratstuff;

import java.awt.Image;
import java.util.HashMap;

import pathfinder.Graph;
import pathfinder.GraphEdge;

public class World extends Graph implements Saveable, Updatable {

	private Integer[][][] worldPointArray;
	private HashMap<MovingObject, WorldPoint> objectMap;
	private Core main;
	private String myName;
	private LightManager myLightManager;
	private WorldCamera myGameCamera;
	private WorldCursor myGameCursor;

	private int width;
	private int height;
	private int depth;

	public World(Core main, String name, int width, int height, int depth) {
		worldPointArray = new Integer[depth][width][height];
		this.width = width;
		this.height = height;
		this.depth = depth;
		objectMap = new HashMap<MovingObject, WorldPoint>();
		this.main = main;
		this.myName = name;
		myLightManager = new LightManager(this);
	}

	public WorldCamera getGameCamera() {
		return myGameCamera;
	}

	public void setGameCamera(WorldCamera myGameCamera) {
		this.myGameCamera = myGameCamera;
	}

	public WorldCursor getGameCursor() {
		return myGameCursor;
	}

	public void setGameCursor(WorldCursor myGameCursor) {
		this.myGameCursor = myGameCursor;
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

	public void addMicroObject(MicroObject object) {
		main.getObjectManager().addMicroObject(object);
	}

	public void removeMicroObject(MicroObject o) {
		main.getObjectManager().removeMicroObject(o);
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

	public Unit getUnitByID(int uniqueID) {
		return main.getUnitManager().getUnit(uniqueID);
	}

	public Unit getUnitByObjectID(int objUID) {
		return main.getUnitManager().getUnitByObjectID(objUID);
	}

	public void addItem(Item item) {
		main.getItemManager().addItem(item);
	}

	public void addUnit(Unit unit) {
		main.getUnitManager().addUnit(unit);
	}

	public void moveObjectTo(MovingObject o, WorldPoint p) {
		WorldPoint old = o.getPosition();
		myLightManager.unregisterLightSource(old);
		o.getPosition().removeObjectAttachment(o);
		p.attachMovingObject(o);
		objectMap.put(o, p);
		myLightManager.registerLightSource(p);
	}

	public void spawnObject(MovingObject o, WorldPoint p) {
		myLightManager.registerLightSource(p);
		objectMap.put(o, p);
		p.attachMovingObject(o);
		main.getObjectManager().addUnit(o);
	}

	public void removeObjectFromWorld(MovingObject o) {
		WorldPoint point = objectMap.get(o);
		point.removeObjectAttachment(o);
		objectMap.remove(o);
		main.getObjectManager().removeObject(o.getUniqueID());
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

		if (p.getMyEdges().canRight() == false && x < width) {
			if (a.canRight()) {
				addEdge(worldPointArray[z][x][y], worldPointArray[z][x + 1][y],
						0);
			}
		}

		else if (p.getMyEdges().canRight() && x < width) {
			if (a.canRight() == false) {
				removeEdge(worldPointArray[z][x][y],
						worldPointArray[z][x + 1][y]);
			}
		}

		if (p.getMyEdges().canDown() == false && y < height) {
			if (a.canDown()) {
				addEdge(worldPointArray[z][x][y], worldPointArray[z][x][y + 1],
						0);
			}
		}

		else if (p.getMyEdges().canDown() && y < height) {
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
		Core.tellFrontend(FrontendMessaging.groundUpdate(groundID, p.getX(),
				p.getY(), p.getZ()));
	}

	@SuppressWarnings("deprecation")
	public void setElementForWP(boolean initial, WorldPoint p, int elementID) {
		// initial should only be true when world is being loaded

		boolean collided = p.collides();
		boolean wasLightSource = p.isLightSource();
		p.setElement(elementID);

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
				getWP(p.getX(), p.getY(), p.getZ() + 1).setElement(
						Element.getByName("ladderup"));
			}
			if (Element.isLadderUp(elementID)) {
				addZEdgeInBothDirections(p,
						getWP(p.getX(), p.getY(), p.getZ() - 1));
				getWP(p.getX(), p.getY(), p.getZ() - 1).setElement(
						Element.getByName("ladderdown"));
			}
			if (!wasLightSource && p.isLightSource()) {
				myLightManager
						.registerLightSource(p.getX(), p.getY(), p.getZ());

			} else if (wasLightSource && !p.isLightSource()) {
				myLightManager.unregisterLightSource(p.getX(), p.getY(),
						p.getZ());
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void removeElementFromWP(WorldPoint p) {
		int elementID = p.getAttachedElement();
		boolean collided = p.collides();
		p.removeElement();
		if (p.collides() && collided == false) {
			updateEdges(p, true);
		}
		if (p.collides() == false && collided) {
			updateEdges(p, false);
		}
		if (Element.isLadderDown(elementID)) {
			removeZEdgeInBothDirections(p,
					getWP(p.getX(), p.getY(), p.getZ() + 1));
			getWP(p.getX(), p.getY(), p.getZ() + 1).removeElement();// ladderup
		}
		if (Element.isLadderUp(elementID)) {
			removeZEdgeInBothDirections(p,
					getWP(p.getX(), p.getY(), p.getZ() - 1));
			getWP(p.getX(), p.getY(), p.getZ() - 1).removeElement(); // ladderdown
		}
		dropItem(p, elementID);
	}

	public void dropItem(WorldPoint p, int destroyedElementID) {
		int droppedItemType = Element.getDroppedItemType(destroyedElementID);
		MovingObject object = new MovingObject(
				Item.getLinkedObjectType(droppedItemType), this,
				UniqueIDFactory.getID());
		spawnObject(object, p);
		Item droppedItem = new Item(this, UniqueIDFactory.getID(),
				droppedItemType, object.getUniqueID(), -1, "dropped");
		addItem(droppedItem);
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
			WorldPoint up = null, down = null, left = null, right = null;
			if (x - 1 > 0) {
				left = getWP(x - 1, y, z);
			}
			if (x + 1 < width) {
				right = getWP(x + 1, y, z);
			}
			if (y + 1 < height) {
				down = getWP(x, y + 1, z);
			}
			if (y - 1 > 0) {
				up = getWP(x, y - 1, z);
			}
			GraphEdgeInfo a = new GraphEdgeInfo(current);
			applyEdgeInfo(a);

			if (left != null) {
				a = left.getMyEdges().getCopy();
				a.setRight(false);
			}
			applyEdgeInfo(a);

			if (right != null) {
				a = right.getMyEdges().getCopy();
				a.setLeft(false);
			}
			applyEdgeInfo(a);

			if (up != null) {
				a = up.getMyEdges().getCopy();
				a.setDown(false);
			}
			applyEdgeInfo(a);

			if (down != null) {
				a = down.getMyEdges().getCopy();
				a.setUp(false);
			}
			applyEdgeInfo(a);

		} else {
			// addEdges
			WorldPoint current = p;
			WorldPoint up = null, down = null, left = null, right = null;
			if (x - 1 > 0) {
				left = getWP(x - 1, y, z);
			}
			if (x + 1 < width) {
				right = getWP(x + 1, y, z);
			}
			if (y + 1 < height) {
				down = getWP(x, y + 1, z);
			}
			if (y - 1 > 0) {
				up = getWP(x, y - 1, z);
			}
			GraphEdgeInfo a = new GraphEdgeInfo(current);

			if (left != null && left.collides() == false) {
				a.setLeft(true);
			}
			if (right != null && right.collides() == false) {
				a.setRight(true);
			}
			if (down != null && down.collides() == false) {
				a.setDown(true);
			}
			if (up != null && up.collides() == false) {
				a.setUp(true);
			}
			applyEdgeInfo(a);

			// add edges to current

			if (left != null) {
				a = left.getMyEdges().getCopy();
				if (left.collides() == false) {
					a.setRight(true);
				}

			}
			applyEdgeInfo(a);

			if (right != null) {
				a = right.getMyEdges().getCopy();
				if (right.collides() == false) {
					a.setLeft(true);
				}

			}
			applyEdgeInfo(a);

			if (down != null) {
				a = down.getMyEdges().getCopy();
				if (down.collides() == false) {
					a.setUp(true);
				}

			}
			applyEdgeInfo(a);

			if (up != null) {
				a = up.getMyEdges().getCopy();
				if (up.collides() == false) {
					a.setDown(true);
				}
			}
			applyEdgeInfo(a);
		}
	}

	public void initialCreationOfEdges() {
		for (int z = 0; z < depth; z++) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
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

						if (x < width - 1) {
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

						if (y < height - 1) {
							WorldPoint down = getWP(x, y + 1, z);
							if (down.collides() == false) {
								a.setDown(true);
							}
						}

						applyEdgeInfo(a);
					}

					int elementID = current.getAttachedElement();

					if (elementID != -1) {
						if (z < depth - 1) {
							if (Element.isLadderDown(elementID)) {
								addZEdge(current, getWP(x, y, z + 1));
							}
						}

						if (z > 0) {
							if (Element.isLadderUp(elementID)) {
								addZEdge(current, getWP(x, y, z - 1));
							}
						}
					}

				}
			}
		}

	}

	public MovingObject getObjectByUID(int objID) {
		if (objID == -1) {
			return null;
		}
		return main.getObjectManager().getObject(objID);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}

	@Override
	public String save() {
		return null;
	}

	@Override
	public Saveable load(String fromString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
	}

	public String getName() {
		return myName;
	}

	public Image getShadowImage(WorldPoint wp) {
		return myLightManager.getShadowImage(wp);
	}

	public LightManager getLightManager() {
		return myLightManager;
	}
}

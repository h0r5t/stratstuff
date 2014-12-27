package stratstuff;

import java.awt.Graphics2D;
import java.util.ArrayList;

import pathfinder.GraphNode;

public class WorldPoint extends GraphNode implements Drawable {

	private int attachedGround;
	private ArrayList<Integer> attachedElements;
	private ArrayList<MovingObject> attachedMovingObjects;
	private GraphEdgeInfo myEdges = new GraphEdgeInfo(this);

	public WorldPoint(int x, int y, int z, int ground) {
		super(UniqueIDFactory.getID(), x, y, z);
		attachedGround = ground;
		attachedElements = new ArrayList<Integer>();
		attachedMovingObjects = new ArrayList<MovingObject>();
	}

	public void attachElement(int elementID) {
		attachedElements.add(elementID);
	}

	public void attachMovingObject(MovingObject o) {
		attachedMovingObjects.add(o);
	}

	public void setGround(int groundID) {
		attachedGround = groundID;
	}

	@Override
	public void draw(Graphics2D g, int x, int y) {
		Ground.draw(g, attachedGround, x, y);

		for (Integer i : attachedElements) {
			Element.draw(g, i, x, y);
		}

		for (MovingObject o : attachedMovingObjects) {
			o.draw(g, x, y);
		}
	}

	public boolean collides() {
		if (Ground.collides(attachedGround)) {
			return true;
		}

		for (Integer element : attachedElements) {
			if (Element.collides(element)) {
				return true;
			}
		}

		for (MovingObject object : attachedMovingObjects) {
			if (object.collides()) {
				return true;
			}
		}
		return false;
	}

	public int getX() {
		return (int) x();
	}

	public int getZ() {
		return (int) z();
	}

	public int getY() {
		return (int) y();
	}

	public int getGround() {
		return attachedGround;
	}

	public ArrayList<Integer> getAttachedElements() {
		return attachedElements;
	}

	public ArrayList<MovingObject> getAttachedMovingObjects() {
		return attachedMovingObjects;
	}

	public GraphEdgeInfo getMyEdges() {
		return myEdges;
	}

	public void setMyEdges(GraphEdgeInfo myEdges) {
		this.myEdges = myEdges;
	}

}

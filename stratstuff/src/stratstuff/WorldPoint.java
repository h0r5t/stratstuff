package stratstuff;

import java.awt.Graphics2D;
import java.util.ArrayList;

import pathfinder.GraphNode;

public class WorldPoint extends GraphNode implements Drawable {

	private int attachedGround;
	private int attachedElement;
	private ArrayList<MovingObject> attachedMovingObjects;
	private GraphEdgeInfo myEdges = new GraphEdgeInfo(this);

	public WorldPoint(int x, int y, int z, int ground) {
		super(UniqueIDFactory.getID(), x, y, z);
		attachedGround = ground;
		attachedElement = -1;
		attachedMovingObjects = new ArrayList<MovingObject>();
	}

	// use method addElement() in World (bridges get registered there)
	@Deprecated
	public void setElement(int elementID) {
		attachedElement = elementID;
	}

	// same here
	@Deprecated
	public void removeElement() {
		attachedElement = -1;
	}

	public void attachMovingObject(MovingObject o) {
		attachedMovingObjects.add(o);
	}

	public void removeObjectAttachment(MovingObject o) {
		attachedMovingObjects.remove(o);
	}

	public void setGround(int groundID) {
		attachedGround = groundID;
	}

	@Override
	public void draw(Graphics2D g, int x, int y) {
		Ground.draw(g, attachedGround, x, y);

		if (attachedElement != -1) {
			Element.draw(g, attachedElement, x, y);
		}

		for (MovingObject o : attachedMovingObjects) {
			o.draw(g, x, y);
		}
	}

	public boolean collides() {
		if (Ground.collides(attachedGround)) {
			return true;
		}

		if (attachedElement != -1) {
			if (Element.collides(attachedElement)) {
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

	public int getAttachedElement() {
		return attachedElement;
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

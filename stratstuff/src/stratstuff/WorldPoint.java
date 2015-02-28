package stratstuff;

import java.awt.Graphics2D;
import java.util.ArrayList;

import pathfinder.GraphNode;

public class WorldPoint extends GraphNode implements Drawable {

	private int attachedGround;
	private int attachedElement;
	private ArrayList<MovingObject> attachedMovingObjects;
	private ArrayList<MicroObject> attachedMicroObjects;
	private GraphEdgeInfo myEdges = new GraphEdgeInfo(this);
	private int textureID;

	public WorldPoint(int x, int y, int z, int ground) {
		super(UniqueIDFactory.getID(), x, y, z);
		attachedGround = ground;
		attachedElement = -1;
		attachedMovingObjects = new ArrayList<MovingObject>();
		attachedMicroObjects = new ArrayList<MicroObject>();
		this.textureID = (int) (Math.random() * GameSettings.TEXTURE_AMOUNT);
	}

	// use method addElement() in World (bridges get registered there)
	@Deprecated
	public void setElement(int elementID) {
		attachedElement = elementID;
		Core.tellFrontend(FrontendMessaging.elementUpdate(elementID, getX(),
				getY(), getZ()));
	}

	// same here
	@Deprecated
	public void removeElement() {
		attachedElement = -1;
		Core.tellFrontend(FrontendMessaging.elementUpdate(-1, getX(), getY(),
				getZ()));
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
		// Ground.draw(g, attachedGround, x, y);
		Ground.draw(g, attachedGround, textureID, x, y);

		if (attachedElement != -1) {
			Element.draw(g, attachedElement, x, y);
		}

		for (MovingObject o : attachedMovingObjects) {
			o.draw(g, x, y);
		}
	}

	public void drawMicroObjects(Graphics2D g, int x, int y) {
		for (MicroObject o : attachedMicroObjects) {
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

	public boolean isLightSource() {
		return Element.isLightSource(attachedElement);
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

	public void removeMicroObject(MicroObject o) {
		attachedMicroObjects.remove(o);
	}

	public void addMicroObject(MicroObject o) {
		attachedMicroObjects.add(o);
	}

	public GraphEdgeInfo getMyEdges() {
		return myEdges;
	}

	public void setMyEdges(GraphEdgeInfo myEdges) {
		this.myEdges = myEdges;
	}

}

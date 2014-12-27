package stratstuff;

public class GraphEdgeInfo {
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private WorldPoint outgoing;

	public GraphEdgeInfo(WorldPoint outgoing) {
		this.outgoing = outgoing;
	}

	public WorldPoint getOutgoing() {
		return outgoing;
	}

	public GraphEdgeInfo getCopy() {
		GraphEdgeInfo i = new GraphEdgeInfo(outgoing);
		i.setDown(down);
		i.setRight(right);
		i.setLeft(left);
		i.setUp(up);
		return i;
	}

	public boolean canLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean canRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean canUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean canDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
}

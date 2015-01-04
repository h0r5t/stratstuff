package stratstuff;

public abstract class Task implements Updatable {

	private int myID;

	public void setID(int id) {
		myID = id;
	}

	public int getID() {
		return myID;
	}

}

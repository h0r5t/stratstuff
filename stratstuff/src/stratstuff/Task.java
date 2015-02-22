package stratstuff;

public abstract class Task implements Updatable {

	private int myID;
	private String deletionMessage; // will be transferred to Frontend

	public void setID(int id) {
		myID = id;
	}

	public int getID() {
		return myID;
	}

	public void setDeletionMessage(String msg) {
		deletionMessage = msg;
	}

	public String getDeletionMessage() {
		return deletionMessage;
	}

}

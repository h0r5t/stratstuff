package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager implements Updatable {

	private Core main;
	private ArrayList<Task> runningTasks;
	private ArrayList<Task> openTasks;
	private ArrayList<Task> deletableTasks;
	private HashMap<Integer, Task> allTasksByID;
	private int currentIdIndex = 0;

	public TaskManager(Core main) {
		this.main = main;
		allTasksByID = new HashMap<Integer, Task>();
		runningTasks = new ArrayList<Task>();
		openTasks = new ArrayList<Task>();
		deletableTasks = new ArrayList<Task>();
	}

	public void addOpenTask(Task t) {
		openTasks.add(t);
		allTasksByID.put(currentIdIndex, t);
		currentIdIndex++;
	}

	public void runTask(Task t) {
		runningTasks.add(t);
		if (!openTasks.remove(t)) {
			allTasksByID.put(currentIdIndex, t);
			currentIdIndex++;
		}
	}

	public void addToDelete(Task t) {
		deletableTasks.add(t);
	}

	public ArrayList<Task> getRunningTasks() {
		return runningTasks;
	}

	public ArrayList<Task> getOpenTasks() {
		return openTasks;
	}

	@Override
	public void update() {
		for (Task t : deletableTasks) {
			main.getFrontendAdapter().taskEnded(t.getID());
			runningTasks.remove(t);
			// so frontend gets the message
		}
		deletableTasks.clear();
		for (Task t : runningTasks) {
			t.update();
		}
	}

}

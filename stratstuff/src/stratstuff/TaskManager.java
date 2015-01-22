package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager implements Updatable {

	private Core main;
	private ArrayList<Task> runningTasks;
	private ArrayList<Task> openTasks;
	private ArrayList<Task> deletableTasks;
	private HashMap<Integer, Task> allTasksByID;

	public TaskManager(Core main) {
		this.main = main;
		allTasksByID = new HashMap<Integer, Task>();
		runningTasks = new ArrayList<Task>();
		openTasks = new ArrayList<Task>();
		deletableTasks = new ArrayList<Task>();
	}

	public void runTask(Task t, int id) {
		runningTasks.add(t);
		allTasksByID.put(id, t);
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
			for (Integer i : allTasksByID.keySet()) {
				if (allTasksByID.get(i).equals(t)) {
					main.getFrontendAdapter().taskEnded(i);
				}
			}
			runningTasks.remove(t);
		}
		deletableTasks.clear();
		for (Task t : runningTasks) {
			t.update();
		}
	}

}

package stratstuff;

import java.util.ArrayList;

public class TaskManager implements Updatable {

	private Core main;
	private ArrayList<Task> runningTasks;
	private ArrayList<Task> openTasks;
	private ArrayList<Task> deletableTasks;

	public TaskManager(Core main) {
		this.main = main;
		runningTasks = new ArrayList<Task>();
		openTasks = new ArrayList<Task>();
		deletableTasks = new ArrayList<Task>();
	}

	public void addOpenTask(Task t) {
		openTasks.add(t);
	}

	public void runTask(Task t) {
		runningTasks.add(t);
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
			runningTasks.remove(t);
		}
		deletableTasks.clear();
		for (Task t : runningTasks) {
			t.update();
		}
	}

}

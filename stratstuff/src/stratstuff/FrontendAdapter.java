package stratstuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FrontendAdapter {

	private IPCServer ipcServer;
	private IPCClient ipcClient;

	private Core main;

	private ArrayList<String> newCommands;
	private ArrayList<String> queueToSend;

	private boolean frontendIsFinished = false;

	// Events
	private HashMap<Integer, Integer> taskEndedTaskIdEventId;

	public FrontendAdapter(Core main) {
		this.main = main;
		ipcServer = new IPCServer(this);
		ipcClient = new IPCClient();
		newCommands = new ArrayList<String>();
		queueToSend = new ArrayList<String>();
		taskEndedTaskIdEventId = new HashMap<Integer, Integer>();
	}

	public void start() {
		new Thread(ipcServer).start();
	}

	private void send(String message) {
		ipcClient.send(message, 32001);
	}

	public void sendQueue() {
		while (!queueToSend.isEmpty()) {
			send(queueToSend.remove(0) + "\n");
		}
	}

	public void messagesReceived(ArrayList<String> list) {
		for (String s : list) {
			newCommands.add(s);
		}
	}

	public void addToQueue(String message) {
		queueToSend.add(message);
	}

	public void startPythonFrontend() {
		String adapterStarterLocation = FileSystem.ADAPTER_STARTER_LOCATION;
		String argument = " " + FileSystem.STRATSTUFF_DIR;
		String[] command = { "gnome-terminal", "--command",
				adapterStarterLocation + argument };
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		queueToSend.clear();
	}

	public void taskEnded(int taskID) {
		if (taskEndedTaskIdEventId.keySet().contains(taskID)) {
			addToQueue(FrontendMessaging.eventOccurred(taskEndedTaskIdEventId
					.get(taskID)));
			taskEndedTaskIdEventId.remove(taskID);
		}
	}

	public void frontendIsFinished() {
		frontendIsFinished = true;
	}

	// look at all commands and execute them
	public void waitForFrontendFIN() {
		if (frontendIsFinished) {
			parseCommands();
		}
		sendQueue();
	}

	private void parseCommands() {
		while (!newCommands.isEmpty()) {
			String command = newCommands.remove(0);
			String name = command.split(" ")[0];

			if (name.equals("FIN")) {
				main.unlock();
				frontendIsFinished = false;
				return;
			}

			else if (name.equals("event")) {
				registerEvent(command.split(" "));
			}

			else if (name.equals("paintObj")) {
				setPaintObject(command.split(" "));
			}

			else {
				main.getConsole().commandEntered(true, command);
			}
		}
	}

	private void setPaintObject(String[] command) {
		boolean bool = Boolean.parseBoolean(command[2]);
		int objID = Integer.parseInt(command[1]);

		main.getWorld().getObjectByID(objID).setPaintBool(bool);
	}

	private void registerEvent(String[] commands) {
		if (Integer.parseInt(commands[1]) == 0) {
			int eventID = Integer.parseInt(commands[2]);
			int taskID = Integer.parseInt(commands[3]);
			taskEndedTaskIdEventId.put(taskID, eventID);
		}
	}

	public void sendStartMessage() {
		send("START");
	}
}

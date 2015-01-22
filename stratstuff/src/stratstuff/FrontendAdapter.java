package stratstuff;

import java.io.IOException;
import java.util.ArrayList;

public class FrontendAdapter {

	private IPCServer ipcServer;
	private IPCClient ipcClient;

	private Core main;

	private ArrayList<String> newCommands;
	private ArrayList<String> queueToSend;

	private boolean frontendIsFinished = false;

	public FrontendAdapter(Core main) {
		this.main = main;
		ipcServer = new IPCServer(this);
		ipcClient = new IPCClient();
		newCommands = new ArrayList<String>();
		queueToSend = new ArrayList<String>();
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
		addToQueue(FrontendMessaging.eventOccurred(taskID));
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

			else if (name.equals("paintObj")) {
				setPaintObject(command.split(" "));
			}

			else if (name.equals("idletask")) {
				int ID = Integer.parseInt(command.split(" ")[2]);
				double millis = Double.parseDouble(command.split(" ")[1]);
				IdleTask task = new IdleTask(main.getTaskManager(), millis);
				main.getTaskManager().runTask(task, ID);
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

	public void sendStartMessage() {
		send("START");
	}
}

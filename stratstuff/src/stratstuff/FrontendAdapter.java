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
		String OS = System.getProperty("os.name").toLowerCase();
		String adapterStarterLocation = FileSystem.ADAPTER_STARTER_LOCATION;
		String adapterLocation = System.getProperty("user.dir") + "\\python\\";
		String argument = " " + FileSystem.STRATSTUFF_DIR;
		if (isWindows(OS)) {
			String[] command = { "cmd", "/k", "start", "",
					adapterLocation + "start_adapter.bat" };

			try {
				Process p = Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (isUnix(OS))

		{
			String[] command = { "gnome-terminal", "--command",
					adapterStarterLocation + "start_adapter.sh" + argument };
			try {
				Process p = Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		queueToSend.clear();
	}

	private static boolean isWindows(String OS) {

		return (OS.indexOf("win") >= 0);

	}

	private static boolean isUnix(String OS) {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public void taskEnded(int taskID) {
		String msg = main.getTaskManager().getTaskWithID(taskID)
				.getDeletionMessage();
		if (msg != null) {
			addToQueue(FrontendMessaging.eventOccurred(taskID, msg));
		} else {
			addToQueue(FrontendMessaging.eventOccurred(taskID));
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

			else if (name.equals("paintObj")) {
				setPaintObject(command.split(" "));
			}

			else if (name.equals("idletask")) {
				int ID = Integer.parseInt(command.split(" ")[2]);
				double millis = Double.parseDouble(command.split(" ")[1]);
				IdleTask task = new IdleTask(main.getTaskManager(), millis);
				main.getTaskManager().runTask(task, ID);
			}

			else if (name.equals("che")) {
				int elementID = Integer.parseInt(command.split(" ")[1]);
				int x = Integer.parseInt(command.split(" ")[2]);
				int y = Integer.parseInt(command.split(" ")[3]);
				int z = Integer.parseInt(command.split(" ")[4]);
				WorldPoint wp = main.getWorld().getWP(x, y, z);
				main.getWorld().setElementForWP(false, wp, elementID);
			}

			else if (name.equals("remObj")) {
				int uniqueID = Integer.parseInt(command.split(" ")[1]);
				MovingObject o = main.getObjectManager().getObject(uniqueID);
				main.getWorld().removeObjectFromWorld(o);
			}

			else if (name.equals("dispInfo")) {
				String infoString = command.split(" ")[1];
				main.getCanvas().setInfoScreen(
						new InfoScreen(infoString, 100, 100));
			}

			else if (name.equals("getScope")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int eventID = Integer.parseInt(command.split(" ")[2]);
				Unit unit = main.getWorld().getUnitByObjectID(objUID);
				String frontendMsg = FrontendMessaging
						.eventOccurred(eventID, unit.getVisionScope(main)
								.getParsedFrontendDataString());
				addToQueue(frontendMsg);
			}

			else if (name.equals("turn")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int x = Integer.parseInt(command.split(" ")[2]);
				int y = Integer.parseInt(command.split(" ")[3]);
				int z = Integer.parseInt(command.split(" ")[4]);
				int eventID = Integer.parseInt(command.split(" ")[5]);
				WorldPoint wp = main.getWorld().getWP(x, y, z);
				main.getWorld().getObjectByUID(objUID)
						.turnToFaceWorldPoint(eventID, wp);
			}

			else if (name.equals("fire")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				main.getWorld().getUnitByObjectID(objUID).fireBullet(main);
			}

			else if (name.equals("mine")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int x = Integer.parseInt(command.split(" ")[2]);
				int y = Integer.parseInt(command.split(" ")[3]);
				int z = Integer.parseInt(command.split(" ")[4]);
				int eventID = Integer.parseInt(command.split(" ")[5]);
				WorldPoint wp = main.getWorld().getWP(x, y, z);
				main.getWorld().getUnitByObjectID(objUID)
						.mineWorldPoint(main, eventID, wp);
			}

			else if (name.equals("radialsignal")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				String message = command.split(" ")[2];
				WorldPoint wp = main.getWorld().getObjectByUID(objUID)
						.getPosition();
				RadialSignal signal = new RadialSignal(main, wp, message);
				main.getWorld().addMicroObject(signal);
			}

			else {
				main.getConsole().commandEntered(true, command);
			}
		}
	}

	private void setPaintObject(String[] command) {
		boolean bool = Boolean.parseBoolean(command[2]);
		int objID = Integer.parseInt(command[1]);

		main.getWorld().getObjectByUID(objID).setPaintBool(bool);
	}

	public void sendStartMessage() {
		send("START");
	}

	public void sendShutDownMessage() {
		send("SHUTDOWN");
	}
}

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

		} else if (isUnix(OS)) {
			// change this to be set in cfg file or smth, todo
			// gnome-terminal, konsole
			String[] command = { "gnome-terminal", "-e",
					adapterStarterLocation + "start_adapter.sh" + argument };
			try {
				Process p = Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				String[] command2 = { "konsole", "-e",
						adapterStarterLocation + "start_adapter.sh" + argument };
				try {
					Process p = Runtime.getRuntime().exec(command2);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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

			else if (name.equals("dispInfo")) {
				String infoString = command.split(" ")[1];
				main.getCanvas().setInfoScreen(
						new InfoScreen(infoString, 100, 100));
			}

			else if (name.equals("getScope")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int eventID = Integer.parseInt(command.split(" ")[2]);
				Unit unit = main.getUnitManager().getUnitByObjectID(objUID);
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
				World world = main.getObjectManager().getObject(objUID)
						.getWorld();
				WorldPoint wp = world.getWP(x, y, z);
				world.getObjectByUID(objUID).turnToFaceWorldPoint(eventID, wp);
			}

			else if (name.equals("fire")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				World world = main.getObjectManager().getObject(objUID)
						.getWorld();
				world.getUnitByObjectID(objUID).fireBullet(world);
			}

			else if (name.equals("mine")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int x = Integer.parseInt(command.split(" ")[2]);
				int y = Integer.parseInt(command.split(" ")[3]);
				int z = Integer.parseInt(command.split(" ")[4]);
				int eventID = Integer.parseInt(command.split(" ")[5]);
				World world = main.getObjectManager().getObject(objUID)
						.getWorld();
				WorldPoint wp = world.getWP(x, y, z);
				world.getUnitByObjectID(objUID).mineWorldPoint(world, eventID,
						wp);
			}

			else if (name.equals("radialsignal")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				String message = command.split(" ")[2];
				World world = main.getObjectManager().getObject(objUID)
						.getWorld();
				WorldPoint wp = world.getObjectByUID(objUID).getPosition();
				RadialSignal signal = new RadialSignal(world, wp,
						world.getObjectByUID(objUID), message);
				world.addMicroObject(signal);
			}

			else if (name.equals("pickupitem")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int linkedObjUID = Integer.parseInt(command.split(" ")[2]);
				Unit unit = main.getObjectManager().getObject(objUID)
						.getWorld().getUnitByObjectID(objUID);
				if (main.getObjectManager().getObject(linkedObjUID).canPickUp()) {
					Item item = main.getItemManager().getItemByObjUID(
							linkedObjUID);
					item.pickedUpBy(unit);
				}
			}

			else if (name.equals("move")) {
				int objUID = Integer.parseInt(command.split(" ")[1]);
				int x = Integer.parseInt(command.split(" ")[2]);
				int y = Integer.parseInt(command.split(" ")[3]);
				int z = Integer.parseInt(command.split(" ")[4]);
				int eventID = Integer.parseInt(command.split(" ")[5]);

				World world = main.getObjectManager().getObject(objUID)
						.getWorld();

				MovingObject o = main.getObjectManager().getObject(objUID);
				MoveTask task = new MoveTask(world, main.getTaskManager(), main
						.getObjectManager().getObject(objUID), world.getWP(x,
						y, z));
				main.getTaskManager().runTask(task, eventID);
			}
		}
	}

	private void setPaintObject(String[] command) {
		boolean bool = Boolean.parseBoolean(command[2]);
		int objID = Integer.parseInt(command[1]);

		World world = main.getObjectManager().getObject(objID).getWorld();

		world.getObjectByUID(objID).setPaintBool(bool);
	}

	public void sendStartMessage() {
		send("START");
	}

	public void sendShutDownMessage() {
		send("SHUTDOWN");
	}
}

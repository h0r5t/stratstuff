package stratstuff;

import java.io.IOException;
import java.util.ArrayList;

public class FrontendAdapter implements Updatable {

	private IPCServer ipcServer;
	private IPCClient ipcClient;

	private Core main;

	private ArrayList<String> newCommands;
	private ArrayList<String> queueToSend;

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

	public void messageReceived(ArrayList<String> list) {
		for (String s : list) {
			newCommands.add(s);
		}
	}

	public void addToQueue(String message) {
		queueToSend.add(message);
	}

	public void startPythonFrontend() {
		String adapterStarterLocation = FileSystem.ADAPTER_STARTER_LOCATION;
		String[] command = { "gnome-terminal", "--command", adapterStarterLocation };
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// look at all commands and execute them
	@Override
	public void update() {
		while (!newCommands.isEmpty()) {
			String command = newCommands.remove(0);

			main.getConsole().commandEntered(true, command);
		}
		sendQueue();
	}
}

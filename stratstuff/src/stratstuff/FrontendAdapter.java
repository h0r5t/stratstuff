package stratstuff;

import java.io.IOException;

public class FrontendAdapter {

	private IPCServer ipcServer;
	private IPCClient ipcClient;

	public FrontendAdapter() {
		ipcServer = new IPCServer();
		ipcClient = new IPCClient();
	}

	private void start() {
		new Thread(ipcServer).start();
	}

	private void send(String message) {
		ipcClient.send(message, 32001);
	}

	public static void main(String[] args) throws IOException {
		FrontendAdapter adapter = new FrontendAdapter();
		adapter.start();

		adapter.send("g\ns\nd");
	}

}

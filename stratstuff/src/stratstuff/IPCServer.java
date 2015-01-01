package stratstuff;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class IPCServer implements Runnable {

	private DatagramSocket socket;
	private FrontendAdapter adapter;

	public IPCServer(FrontendAdapter adapter) {
		this.adapter = adapter;
		try {
			socket = new DatagramSocket(32000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() {
		DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] data = packet.getData();

		String dataString = new String(data);
		ArrayList<String> list = new ArrayList<String>();

		for (String line : dataString.split("\n")) {
			list.add(line);
		}
		// remove last element since it contains nothing
		list.remove(list.size() - 1);

		adapter.messageReceived(list);
	}

	@Override
	public void run() {
		while (true) {
			listen();
		}

	}

}

package stratstuff;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class IPCServer implements Runnable {

	private DatagramSocket socket;

	public IPCServer() {
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

		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		int len = packet.getLength();
		byte[] data = packet.getData();

		System.out.printf("Anfrage von %s vom Port %d mit der Länge %d:%n%s%n",
				address, port, len, new String(data, 0, len));
	}

	@Override
	public void run() {
		while (true) {
			listen();
		}

	}

}

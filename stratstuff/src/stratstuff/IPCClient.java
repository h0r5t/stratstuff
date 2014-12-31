package stratstuff;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class IPCClient {

	DatagramSocket socket;

	public IPCClient() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void send(String s, int port) {
		System.out.println("sending");
		try {
			byte[] raw = s.getBytes();

			DatagramPacket packet = new DatagramPacket(raw, raw.length,
					InetAddress.getByName("localhost"), port);

			socket.send(packet);

			System.out.println("sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
}

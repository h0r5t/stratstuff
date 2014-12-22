package stratstuff;

public class StratStuff implements Runnable {

	public StratStuff() {
	}

	@Override
	public void run() {
		while (true) {

		}
	}

	public static void main(String[] args) {
		new Thread(new StratStuff()).start();
	}
}

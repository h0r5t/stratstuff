package stratstuff;

public class StratStuff implements Runnable {

	public StratStuff() {
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("hi");
		}
	}

	public static void main(String[] args) {
		new Thread(new StratStuff()).start();
	}
}

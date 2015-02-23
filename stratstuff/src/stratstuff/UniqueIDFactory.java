package stratstuff;

import java.util.concurrent.atomic.AtomicInteger;

public class UniqueIDFactory {

	private static AtomicInteger idMaker = new AtomicInteger();

	public static int getID() {
		return idMaker.incrementAndGet();
	}

	public static void increment() {
		idMaker.incrementAndGet();
	}
}

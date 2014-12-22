package stratstuff;

import java.util.concurrent.atomic.AtomicInteger;

public class UniqueIDFactory {
	
	private static AtomicInteger idMaker;

	public static int getID()
	{
		return idMaker.incrementAndGet();
	}

}

package stratstuff;

import java.io.File;
import java.io.FilenameFilter;

public class SectorFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith("sector"))
			return true;
		return false;
	}

}

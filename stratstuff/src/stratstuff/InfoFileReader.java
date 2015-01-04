package stratstuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class InfoFileReader {

	public static HashMap<String, LoadedInfo> readFile(String fileLocation) {
		File file = new File(fileLocation);
		Scanner scanner;
		HashMap<String, LoadedInfo> infos = new HashMap<String, LoadedInfo>();

		try {
			scanner = new Scanner(file);

			String line;
			LoadedInfo currentInfo = null;
			String currentInfoName = null;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (line.endsWith("{")) {
					currentInfoName = line.split(":")[0].trim();
					currentInfo = new LoadedInfo();
					continue;
				} else if (line.trim().equals("}")) {
					infos.put(currentInfoName, currentInfo);
					continue;
				}
				currentInfo.parse(line);
			}

			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return infos;
	}
}

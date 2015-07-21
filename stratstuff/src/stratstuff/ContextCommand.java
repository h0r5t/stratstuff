package stratstuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ContextCommand {

	// the design this contextcommand is assigned too
	private RobotDesign design;

	// the name of the method in the python script of the robot design
	private String methodName;

	public ContextCommand(RobotDesign design, String methodName) {
		this.design = design;
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	public static ArrayList<ContextCommand> scanDesignFile(RobotDesign design) {
		ArrayList<ContextCommand> list = new ArrayList<ContextCommand>();
		String line, methodName = "";

		try {
			Scanner scanner = new Scanner(design.getFile());

			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				if (line.contains("#ContextCommand")) {
					line = scanner.nextLine().trim();
					methodName = line.split(" ")[1].split("\\(")[0].trim();
					ContextCommand command = new ContextCommand(design, methodName);
					list.add(command);
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;

	}

}
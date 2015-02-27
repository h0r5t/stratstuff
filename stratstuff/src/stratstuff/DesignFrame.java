package stratstuff;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class DesignFrame implements Updatable {

	private Core core;
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel filesPanel, designsPanel;
	private JList<String> filesJList1, filesJList2;
	private DefaultListModel<String> modelFiles, modelDesigns;
	private DefaultComboBoxModel<String> modelDesigns2, modelUnits;
	private JButton shiftFilesButton1;
	private JButton shiftFilesButton2;
	private JComboBox<String> unitsComboBox, designsComboBox;
	private JButton applyButton, refreshButton;

	private ArrayList<Design> loadedDesigns;

	public DesignFrame(Core core) {
		this.core = core;
		initFrame();
		initTabbedPane();
		initMenuBar();
		initPanels();
		tabbedPane.add("Files", filesPanel);
		tabbedPane.add("Designs", designsPanel);
	}

	private void initTabbedPane() {
		tabbedPane = new JTabbedPane();
		frame.add(tabbedPane);
	}

	private void initPanels() {
		designsPanel = new JPanel();
		designsPanel.setLayout(null);
		designsPanel.setBackground(Color.LIGHT_GRAY);

		applyButton = new JButton("Apply Changes");
		applyButton.setBounds(150, 150, 200, 50);
		applyButton.addActionListener(new MyActionListener());
		designsPanel.add(applyButton);

		filesPanel = new JPanel();
		filesPanel.setLayout(null);
		filesPanel.setBackground(Color.LIGHT_GRAY);
		loadDesigns();
		makeUnitComboBox();
		makeDesignComboBox();

		loadAndAddAvailableFiles();

		refreshButton = new JButton("Refresh");
		refreshButton.setBounds(280, 250, 75, 50);
		refreshButton.setBackground(Color.DARK_GRAY);
		refreshButton.setForeground(Color.WHITE);
		refreshButton.addActionListener(new MyActionListener());
		filesPanel.add(refreshButton);

		shiftFilesButton1 = new JButton(">>");
		shiftFilesButton1.setBounds(280, 50, 75, 50);
		shiftFilesButton1.setBackground(Color.DARK_GRAY);
		shiftFilesButton1.setForeground(Color.WHITE);
		shiftFilesButton1.addActionListener(new MyActionListener());
		filesPanel.add(shiftFilesButton1);

		shiftFilesButton2 = new JButton("<<");
		shiftFilesButton2.setBounds(280, 150, 75, 50);
		shiftFilesButton2.setBackground(Color.DARK_GRAY);
		shiftFilesButton2.setForeground(Color.WHITE);
		shiftFilesButton2.addActionListener(new MyActionListener());
		filesPanel.add(shiftFilesButton2);
	}

	private void makeDesignComboBox() {
		modelDesigns2 = new DefaultComboBoxModel<String>();
		modelDesigns2.addElement("-");
		for (Design d : loadedDesigns) {
			modelDesigns2.addElement(d.getName());
		}

		designsComboBox = new JComboBox<String>(modelDesigns2);
		designsComboBox.setBounds(310, 30, 200, 30);
		designsComboBox.setBackground(Color.DARK_GRAY);
		designsComboBox.setForeground(Color.WHITE);
		designsPanel.add(designsComboBox);
	}

	public void makeUnitComboBox() {
		modelUnits = new DefaultComboBoxModel<String>();
		if (core.getUnitManager() == null)
			return;
		for (Unit u : core.getUnitManager().getUnitList()) {
			modelUnits.addElement("" + u.getUniqueID());
		}
		unitsComboBox = new JComboBox<String>(modelUnits);
		unitsComboBox.setBounds(10, 30, 200, 30);
		unitsComboBox.setBackground(Color.DARK_GRAY);
		unitsComboBox.setForeground(Color.WHITE);
		unitsComboBox.addActionListener(new ChangeListener());
		designsPanel.add(unitsComboBox);

		String unit = (String) unitsComboBox.getSelectedItem();
		Design actualDesign = core.getUnitManager()
				.getUnit(Integer.parseInt(unit)).getDesign();
		if (actualDesign == null) {
			designsComboBox.setSelectedItem("-");
		} else {
			designsComboBox.setSelectedItem(actualDesign.getName());
		}
	}

	private void initMenuBar() {

	}

	private void initFrame() {
		frame = new JFrame("designs");
		frame.setVisible(false);
		frame.setLocation(100, 100);
		frame.setSize(700, 350);
		frame.addKeyListener(new MyKeyListener());
		frame.addWindowListener(new MyWindowAdapter());
	}

	private void loadAndAddAvailableFiles() {
		modelFiles = new DefaultListModel<String>();
		File dir = new File(FileSystem.CUSTOM_DESIGNS_DIR);
		for (File f : dir.listFiles(new PythonSourceFileFilter())) {
			modelFiles.addElement(f.getName().split("\\.")[0]);
		}

		// remove all designs that are already loaded
		for (Design d : loadedDesigns) {
			modelFiles.removeElement(d.getName());
		}

		filesJList1 = new JList<String>(modelFiles);
		filesJList1.setBounds(10, 10, 230, 230);
		filesJList1.setBackground(Color.DARK_GRAY);
		filesJList1.setForeground(Color.WHITE);
		filesPanel.add(filesJList1);
	}

	private void updateFiles() {
		modelFiles.removeAllElements();
		File dir = new File(FileSystem.CUSTOM_DESIGNS_DIR);
		for (File f : dir.listFiles(new PythonSourceFileFilter())) {
			modelFiles.addElement(f.getName().split("\\.")[0]);
		}

		// remove all designs that are already loaded
		for (Design d : loadedDesigns) {
			modelFiles.removeElement(d.getName());
		}
	}

	private void loadDesigns() {
		loadedDesigns = new ArrayList<Design>();
		try {
			modelDesigns = new DefaultListModel<String>();
			Scanner scanner = new Scanner(new File(FileSystem.GAME_DIR
					+ "/designs/robots.txt"));

			while (scanner.hasNextLine()) {
				String[] split = scanner.nextLine().split(" ");
				String name = split[0];
				File path = new File(split[1]);
				loadedDesigns.add(new Design(name, path));
				modelDesigns.addElement(name);
			}

			scanner.close();

			filesJList2 = new JList<String>(modelDesigns);
			filesJList2.setBounds(400, 10, 230, 230);
			filesJList2.setBackground(Color.DARK_GRAY);
			filesJList2.setForeground(Color.WHITE);
			filesPanel.add(filesJList2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteDesign(String designName) {
		Design d = null;
		for (Design design : loadedDesigns) {
			if (design.getName().equals(designName)) {
				d = design;
			}
		}

		loadedDesigns.remove(d);
	}

	public void save() {
		try {
			PrintWriter writer = new PrintWriter(new File(FileSystem.GAME_DIR
					+ "/designs/robots.txt"));
			for (Design d : loadedDesigns) {
				writer.append(d.getName() + " " + d.getPath() + "\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}

	@Override
	public void update() {

	}

	public Design getDesignByName(String designName) {
		for (Design d : loadedDesigns) {
			if (d.getName().equals(designName)) {
				return d;
			}
		}

		return null;
	}

	private class MyWindowAdapter extends WindowAdapter {
		private Core main;

		public MyWindowAdapter() {
		}

		public void windowClosing(WindowEvent windowEvent) {
			hide();
		}
	}

	private class MyKeyListener extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F12) {
				hide();
			}
		}
	}

	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == shiftFilesButton1) {
				String designName = filesJList1.getSelectedValue();
				if (designName == null)
					return;
				modelDesigns.addElement(designName);
				modelFiles.removeElement(designName);
				loadedDesigns.add(new Design(designName, new File(
						FileSystem.CUSTOM_DESIGNS_DIR + designName + ".py")));
				makeDesignComboBox();
				makeUnitComboBox();
			} else if (e.getSource() == shiftFilesButton2) {
				String designName = filesJList2.getSelectedValue();
				if (designName == null)
					return;
				modelDesigns.removeElement(designName);
				modelFiles.addElement(designName);
				deleteDesign(designName);
				makeDesignComboBox();
				makeUnitComboBox();
			} else if (e.getSource() == applyButton) {
				String unitID = (String) unitsComboBox.getSelectedItem();
				String design = (String) designsComboBox.getSelectedItem();
				Unit unit = core.getUnitManager().getUnit(
						Integer.parseInt(unitID));
				if (design.equals("-")) {
					if (unit.getDesign() != null) {
						Core.tellFrontend(FrontendMessaging
								.removeRobotDesign(unit.getMovingObjUID()));
					}
					unit.setDesign(null);
				} else {
					Design d = getDesignByName(design);
					if (unit.getDesign() == null) {
						Core.tellFrontend(FrontendMessaging
								.startRobotWithDesign(unit.getMovingObjUID(),
										d.getName()));
					} else if (!unit.getDesign().getName().equals(d.getName())) {
						Core.tellFrontend(FrontendMessaging
								.removeRobotDesign(unit.getMovingObjUID()));
						Core.tellFrontend(FrontendMessaging
								.startRobotWithDesign(unit.getMovingObjUID(),
										d.getName()));
					}
					unit.setDesign(d);
				}
			} else if (e.getSource() == refreshButton) {
				updateFiles();
			}
		}
	}

	private class ChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String unit = (String) unitsComboBox.getSelectedItem();
			Design actualDesign = core.getUnitManager()
					.getUnit(Integer.parseInt(unit)).getDesign();
			if (actualDesign == null) {
				designsComboBox.setSelectedItem("-");
			} else {
				designsComboBox.setSelectedItem(actualDesign.getName());
			}
		}
	}

	private class PythonSourceFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			if (name.split("\\.").length > 0 && !name.startsWith("__init__"))
				if (name.split("\\.")[1].equals("py"))
					return true;
			return false;
		}

	}
}

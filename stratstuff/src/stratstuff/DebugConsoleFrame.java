package stratstuff;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class DebugConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private String currentText = "";
	private String lastCommand = "";
	private DebugConsole console;

	public DebugConsoleFrame(DebugConsole console) {
		super("debug_console");

		this.console = console;

		setSize(GameSettings.DEBUG_FRAME_WIDTH, GameSettings.DEBUG_FRAME_HEIGHT);
		setLocation(GameSettings.DEBUG_FRAME_XPOS,
				GameSettings.DEBUG_FRAME_YPOS);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setAutoscrolls(true);
		textArea.setCaretColor(Color.WHITE);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
		textArea.addKeyListener(new KL());
		add(textArea);
	}

	public void setInitialCaret() {
		textArea.setCaretPosition(1);
	}

	public void print(String s) {
		currentText += s;
		textArea.append(s);
	}

	private void callback() {
		String newInput = textArea.getText().replace(currentText, "");
		currentText = textArea.getText();
		lastCommand = newInput.trim();
		console.commandEntered(false, newInput.trim());
	}

	public void resetText() {
		textArea.setText("");
		textArea.repaint();
		currentText = "";
	}

	private void loadLastCommand() {
		print(lastCommand);
	}

	private class KL implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				callback();
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				loadLastCommand();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	}

	public void setCaretToLast() {
		textArea.setCaretPosition(currentText.length());
	}

}

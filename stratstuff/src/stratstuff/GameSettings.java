package stratstuff;

import java.awt.Toolkit;

public class GameSettings {

	public static final boolean GENERATE_NEW_WORLD = false;

	public static final long TICK_MILLIS = 50;
	public static final long INITIAL_SLEEP_TIME = 2000;

	public static final int TILE_SIZE = 20;

	public static int GAME_FRAME_WIDTH = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();
	public static int GAME_FRAME_HEIGHT = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight();
	public static final int GAME_FRAME_XPOS = 0;
	public static final int GAME_FRAME_YPOS = 0;

	public static final int MENU_WIDTH = 200;
	public static final int MENU_X = GAME_FRAME_WIDTH - MENU_WIDTH + 35;
	public static final int MENU_HEIGHT = GAME_FRAME_HEIGHT;

	public static final int DEBUG_FRAME_WIDTH = 250;
	public static final int DEBUG_FRAME_HEIGHT = 700;
	public static final int DEBUG_FRAME_XPOS = GAME_FRAME_WIDTH + 20;
	public static final int DEBUG_FRAME_YPOS = 10;

	public static final int WORLD_WIDTH = 120;
	public static final int WORLD_HEIGHT = 120;
	public static final int WORLD_DEPTH = 10;
}

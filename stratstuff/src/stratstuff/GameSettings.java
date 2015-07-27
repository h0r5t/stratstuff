package stratstuff;

import java.awt.Toolkit;

public class GameSettings {

	public static final boolean GENERATE_NEW_WORLD = false;
	public static final boolean KEEP_STUFF = true;

	public static final boolean GENERATE_NEW_GALAXY = true;

	public static final long TICK_MILLIS = 50;
	public static final long INITIAL_SLEEP_TIME = 2000;

	public static final int TILE_SIZE = 20;

	public static int GAME_FRAME_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int GAME_FRAME_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int GAME_FRAME_XPOS = 0;
	public static final int GAME_FRAME_YPOS = 0;

	public static final int MENU_WIDTH = 200;
	public static final int MENU_X = GAME_FRAME_WIDTH - MENU_WIDTH;
	public static final int MENU_HEIGHT = GAME_FRAME_HEIGHT;

	public static final int COLOR_VARIATION = 5;
	public static final int TEXTURE_AMOUNT = 30;

	public static final int BULLET_SPEED = 50;

	public static final int LIGHT_LEVEL_SURFACE = 4;
	public static final int LIGHT_LEVEL_UNDERGROUND = 4;
}

package myHTTPProxy;

import java.awt.Color;

public interface GUIConstants {
	static final int DEFAULT_HEIGHT = 480;
	static final int DEFAULT_WIDTH = 480;
	static final String DEFAULT_LOOK_N_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	static final String DESCRIPTION = "<html>A simple HTTP Java Proxy Server based on Swing Application Framework, sockets and threads.</html>";
	static final Color D_BLUE = Color.decode("0xF3F7FB");
	static final Color X_BLUE = Color.decode("0x3C5B93");
	static final String EMPTY_BOX_MARKER = "sb";
	
	/*
	 * Some GUI actions
	 */
	static final String BAN = "b";
	static final String UNBAN = "u";
	static final String GROUP_CHANGED = "g";
	static final String SHOW_ABOUT = "a";
	static final String EXIT = "e";
	static final String START_SERVER = "1";
	static final String STOP_SERVER = "0";
	static final String CLOSE_WND = "c";//if some problems you can change it
	static final String REFRESH_HOSTS = "r";
	static final String STAT_PREFIX = "sp";
	static final String SHOW_OPT = "o";
	static final String PERIOD_CHANGED = "p";
	static final String IMPORT = "i";
	static final String EXPORT = "-i";
	
	/*
	 * clients' groups
	 */
	 static final String[] CLIENT_GROUPS = new String[] { "All", "Connected", "Banned", "Not Banned" };
	 static final int GROUP_ALL = 0;
	 static final int GROUP_CONNECTED = 1;
	 static final int GROUP_BANNED = 2;
	 static final int GROUP_NOT_BANNED = 3;
	 
	 /*
	  * Date periods
	  */
	 static final String[] DATE_PERIODS = {"Month", "Year", "Day"};
	 static final int ST_YEAR = 1;
	 static final int ST_MONTH = 0;
	 static final int ST_DAY = 2; 
	 
	 /*
	  * resources
	  */
	 static final String LOGO_SOURCE = "src/GUI/ProxyLogo.png";
	 static final String MENU_SETTINGS_PICTURE_SOURCE = "src/GUI/settings.png";
	 static final String STATISTIC_BTN_PICTURE = "src/GUI/statistics.png";
	 
	 /*
	  * StatBox
	  */
	 static final int STAT_WIDTH = 480;//480
	 static final int STAT_HEIGHT = 320;//320
	 static final int CHART_WIDTH = 100;
	 static final int CHART_HEIGHT = 100;
	 static final Color STAT_BCK = Color.WHITE;
	 static final Color UPLOADBAR_COLOR = Color.decode("0x4C90F8");
	 static final Color DOWNLOADBAR_COLOR = Color.decode("0xEE2000");
	 static final String[] MONTH_NAMES = {"Jan", "Feb", "Mar", "Apr", "May","Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	 
	 /*
	  * AboutBox
	  */
	 static final int ABOUT_WIDTH = 520;
	 static final int ABOUT_HEIGHT = 284;
	 
	 /*
	  * OptionsBox
	  */
	 static final int OPT_WIDTH = 240;
	 static final int OPT_HEIGHT = 120;
}

package myHTTPProxy;

public interface AllConstants extends GUIConstants, XMLConstants, NetworkConstants, DBConstants{
	
	/*
	 * General
	 */
	static final String APPLICATION_NAME = "Simple Java Proxy Server";
	static final String VERSION = "0.1.2";
	static final String AUTHOR_NAME = "Yegorov Nickolay";
	static final String HOMEPAGE_URL = "http://none";
	static final int TIMER_DELAY = (int)(Math.E*Math.PI*42);//you see, all reliable constants are included. Why 42? 42 is answer for this question :D
	
	/*
	 * Admin's console commands
	 */
	static final String SHUTDOWN  = "shutdown";
}

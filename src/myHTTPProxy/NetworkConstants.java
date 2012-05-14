package myHTTPProxy;

public interface NetworkConstants {	
	static final int DEFAULT_PORT = 3128;
	//with gprs it's normal
	static final int DEFAULT_TIMEOUT = 60 * 1000;
	static final int ONLINE_TIMEOUT = 120 * 1000;
	static final int[] ENDLINE_CHARACTERS = new int[]{10, 0, 13};
}

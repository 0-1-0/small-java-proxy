package myHTTPProxy;

public interface XMLConstants {
	
	static final String XML_SOURCE = "stat.XML";
	/*
	 * tags
	 */
	static final String SETTINGS_TAG = "settings";
	static final String PORT_TAG = "port";
	static final String ROOT_ELEM_NAME = "info";
	static final String CLIENT_TAG = "client";
	static final String CLIENT_TRAFFIC_TAG = "trafficdat";
	static final String DATE_TAG = "date";
	static final String UPLOAD_ATTR = "upload";
	static final String DOWNLOAD_ATTR = "download";
	static final String HOST_ATTR = "host";
	static final String BANNED_ATTR = "banned";
}

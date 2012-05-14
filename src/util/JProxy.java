package util;
import java.io.*;
import java.net.*;

import myHTTPProxy.AllConstants;

/*
 * Singleton class that provides
 * necessary proxy functionality
 * 
 * proxy server just listens for connections and creates
 * a new thread for each connection attempt (the ProxyThread
 * class really does all the work)
 */
public class JProxy extends Thread implements AllConstants{	
	
	private static volatile JProxy instance = null;
	private ServerSocket server = null;
	private static int port = DEFAULT_PORT;
	private int debugLevel = 0;
	private PrintStream debugOut = System.out;	
	private ThreadGroup tg;
	
	public void setPort(int p){
		port = p;		
	}
	
	/*
	 * this method provides universal enter point for all operationts with this singleton
	 */
	public static JProxy getInstance(){
		 if (instance == null) {
	            synchronized (JProxy.class) {
	                    instance = new JProxy();
	            }
	        }
	        return instance;
	}	
	
	/* 
	 * allow the user to decide whether or not to send debug
	 * output to the console or some other PrintStream
	 */
	public void setDebug (int level, PrintStream out){
		debugLevel = level;
		debugOut = out;
	}
	
	/* 
	 * get the port that we're supposed to be listening on
	 */
	public int getPort (){
		return port;
	}	 
	
	/* 
	 * return whether or not the socket is currently open
	 */
	public boolean isRunning (){
		return (instance != null && server != null && !server.isClosed());
	}	 
	
	/* 
	 * closeSocket will close the open ServerSocket; use this
	 * to halt a running jProxy thread
	 */
	public void shutdown (){
		try {
			server.close();
		}  catch(Exception e)  { 
			if (debugLevel > 0)
				debugOut.println(e);
		}finally{
			instance = null;
		}
	}	
	
	public void run(){
		tg = new ThreadGroup("ClientSessions");
		try {
			// creates a server socket, and loop forever listening for
			// client connections
			server = new ServerSocket(port);
			if (debugLevel > 0)
				debugOut.println("Started jProxy on port " + port);
			
			while (true){
				Socket client = server.accept();
				
				String host = client.getInetAddress().getHostAddress();
				ClientInfoBank cl = ClientInfoBank.getInstance();
				
				if(! (cl.hasClient(host) && cl.getClientByAddress(host).isBanned())){
					SocketTransmitter t = new SocketTransmitter(client);
					t.setDebug(debugLevel, debugOut);
					Thread t1 = new Thread(tg, t);
					t1.start();
				}
			}
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("JProxy Thread error: " + e);
		}
		
		shutdown();
	}
	
}




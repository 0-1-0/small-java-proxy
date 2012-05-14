package myHTTPProxy;

import gui.AdminGUI;

import java.util.Scanner;

import util.ClientInfoBank;
import util.JProxy;


public class Main implements AllConstants{
	
	public static void main (String args[]){
		//loading data
		//ClientInfoBank.getInstance().loadXMLData();
		
		//creating admin's panel
		new AdminGUI(APPLICATION_NAME + " v." + VERSION);
		
		//now we can process user's commands from command line
		Scanner in = new Scanner(System.in);
		while (true){
			try { 
				if(in.next().toLowerCase().contains(SHUTDOWN))
					System.exit(0);
				Thread.sleep(3000); 
			} catch (Exception e) {
				System.err.println(e.getStackTrace());
			}
		}
	}

	public static void startServer(){			
		System.err.println("  **  Start Proxy  **\n");
		JProxy.getInstance().shutdown();
		JProxy.getInstance().setDebug(0, System.out);
		JProxy.getInstance().start();	
	}
	
	public static void stopServer(){
		System.err.println("  **  Stop Proxy  **\n");
		JProxy.getInstance().shutdown();
		ClientInfoBank.getInstance().saveBD();
		//ClientInfoBank.getInstance().saveXMLData();
	}

}

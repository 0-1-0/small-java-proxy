package util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import myHTTPProxy.AllConstants;

/**
 * this class incapsulates information
 * about client of this proxy server
 * ant it's current connections
 */
public class Client implements AllConstants{
	private String hostname;
	private boolean banned;
	private long lastConnection;
	// Long key added for optimization. 
	// it represents date as number yyyymmddhh
	private TreeMap<Long, Traffic> statistic; 
	
	/**
	 * @param host: client's ip address
	 */
	public Client(String host, long lastConnect){
		hostname = host;
		banned = false;
		lastConnection = lastConnect;
		statistic = new TreeMap<Long, Traffic>();
	}
	
	public long getLastConnection() {
		return lastConnection;
	}

	/**
	 * @return ip of this client
	 */
	public String getHostName(){
		return hostname;
	}
	
	/**
	 * @return true if banned and false else
	 */
	public boolean isBanned(){
		return banned;
	}
	/**
	 * @param status: sets client banned if true or not banned otherwise
	 */
	public void setBanned(boolean status){
		banned = status;
	}
	
	/**
	 * invocation of this method will update last client's connection time
	 */
	public void addSession(){
		lastConnection = System.currentTimeMillis();
		//ClientInfoBank.getInstance().add(this);
	}
	
	public void addSession(long upload, long download){
		long key = this.calendarToLong(new GregorianCalendar());		
		this.addSession();	
		
		if(statistic.containsKey(key))
			statistic.get(key).add(upload, download);	
		else 
			statistic.put(key, new Traffic(upload, download));
		//ClientInfoBank.getInstance().addClientsConnection(this.getHostName(), key, upload, download);
	}
	
	public void addSession(Long key, long upload, long download){			
		if(statistic.containsKey(key))
			statistic.get(key).add(upload, download);	
		else 
			statistic.put(key, new Traffic(upload, download));
		//ClientInfoBank.getInstance().addClientsConnection(this.getHostName(), key, upload, download);
	}
	
	/**
	 * returns Traffic[12], whear each of Traffic objects contains 
	 * information about this client's statistics
	 * over the same month. (Traffic[0] will contain info about January, etc.)
	 * @param startDate - Calendar with necessary year. Other fields of calendar don't use
	 * @return array with 12 Traffic objects
	 * @see Traffic
	 */
	public Traffic[] getStatisticsByYear(Calendar startDate){		
		Traffic[] res = new Traffic[12];
		
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.DAY_OF_MONTH, 0);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		
		for(int i = 0; i < 12; i++){
			res[i] = new Traffic(0, 0);	
			
			Long key1  = calendarToLong(startDate);			
			startDate.add(Calendar.MONTH, 1);
			Long key2 = calendarToLong(startDate);
			
			for(Traffic t:statistic.subMap(key1, key2).values())
				res[i].add(t);
			
			startDate.add(Calendar.MONTH, 1);
		}
		
		return res;
	}
	
	public Traffic[] getStatisticsByMonth(Calendar startDate){
		
		Traffic[] res = new Traffic[startDate.getMaximum(Calendar.DAY_OF_MONTH)];
		
		startDate.set(Calendar.DAY_OF_MONTH, 0);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		
		for(int i = 0; i < res.length; i++){
			res[i] = new Traffic(0, 0);		
			
			Long key1  = calendarToLong(startDate);			
			startDate.add(Calendar.DAY_OF_MONTH, 1);
			Long key2 = calendarToLong(startDate);
			
			for(Traffic t:statistic.subMap(key1, key2).values())
				res[i].add(t);	
			
		}
		
		return res;
	}
	
	public Traffic[] getStatisticsByDay(Calendar startDate){
		Traffic[] res = new Traffic[24];
		
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		
		for(int i = 0; i < 24; i++){
			
			if(statistic.containsKey(calendarToLong(startDate)))
				res[i] = statistic.get(this.calendarToLong(startDate));
			else
				res[i] = new Traffic(0,0);
			
			startDate.add(Calendar.HOUR_OF_DAY, 1);
		}
		
		return res;
	}
	
	public Map<Long, Traffic> getAllStatistic(){
		return this.statistic;
	}
	
	/**
	 * @return <b>true</b>, if client is connected and sending and recieving data now
	 * or <b>false</b> if disconnected
	 */
	public boolean isOnline(){
		return (System.currentTimeMillis() - lastConnection) <= ONLINE_TIMEOUT;
	}
	
	private long calendarToLong(Calendar c){
		long res = 0;
		res =  c.get(Calendar.YEAR) 	* 1000000;
		res += c.get(Calendar.MONTH) 	  * 10000;
		res += c.get(Calendar.DAY_OF_MONTH) * 100;
		res += c.get(Calendar.HOUR_OF_DAY);
		return res;
	}
}

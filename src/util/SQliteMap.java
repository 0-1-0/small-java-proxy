package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.sql.*;

import myHTTPProxy.AllConstants;

public class SQliteMap implements Map<String, Client>, AllConstants {
	
	public SQliteMap(){
		File f = new File(DB_NAME);
		boolean ex = f.exists();		
		try{
			Class.forName("org.sqlite.JDBC");
			if(!ex)
				this.clear();
		}catch(Exception e){
			System.err.println("DB error: " + e.getMessage());
		}
	}
	
	public void saveOptions(String[] optionID, String[] optionValue){
		
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
			Statement stat = conn.createStatement();
			
			stat.executeUpdate("drop table if exists " + OPT_T + ";");//reset
			StringBuffer query = new StringBuffer("create table ");
			query.append(OPT_T);
			query.append(" (");
			query.append(ID_FIELD + " ,");
			query.append(VALUE_FIELD + ");");
			stat.executeUpdate(query.toString());
			
			PreparedStatement prep = conn.prepareStatement(
			          "insert into " + OPT_T + " values (?, ?);");
			for(int i = 0; i < optionID.length && i < optionValue.length; i++){
				prep.setString(1, optionID[i]);
				prep.setString(2, optionValue[i]);
				prep.addBatch();				
			}
			
			conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getOption(String optName){
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
			Statement stat = conn.createStatement();
			String res = null;
			
			ResultSet rs = stat.executeQuery("select * from " + OPT_T + " where " + ID_FIELD + " = '" + optName + "';");
			if(rs.next())
				res = rs.getString(VALUE_FIELD);
			
			conn.close();
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}			
	}

	@Override
	public void clear() {
		try{
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		    Statement stat = conn.createStatement();
		    
			stat.executeUpdate("drop table if exists " + CLIENT_T + ";");
			stat.executeUpdate("drop table if exists " + CONN_T + ";");
			stat.executeUpdate("drop table if exists " + OPT_T + ";");
			
			StringBuffer query = new StringBuffer("create table ");
			query.append(CLIENT_T);
			query.append(" (");
			query.append(HOST_FIELD + " ,");
			query.append(BANNED_FIELD + " ,");
			query.append(LAST_CONN_FIELD + ");");
			stat.executeUpdate(query.toString());
			
			query = new StringBuffer("create table ");
			query.append(CONN_T);
			query.append(" (");
			query.append(HOST_FIELD + " ,");
			query.append(DATE_FIELD + " ,");
			query.append(UPLOAD_FIELD + " ,");
			query.append(DOWNLOAD_FIELD + ");");
			stat.executeUpdate(query.toString());
			
			query = new StringBuffer("create table ");
			query.append(OPT_T);
			query.append(" (");
			query.append(ID_FIELD + " ,");
			query.append(VALUE_FIELD + ");");
			stat.executeUpdate(query.toString());
			
			conn.close();
		}catch(Exception e){
			System.err.println("DB error: " + e.getMessage());
		}
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		    Statement stat = conn.createStatement();
		    
			String host = (String)key;
			ResultSet rs = stat.executeQuery("select * from " + CLIENT_T + " where " + HOST_FIELD + " = '" + host + "';");
			boolean res = rs.next();
			
			rs.close();
			conn.close();
			
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, Client>> entrySet() {
		return null;
	}

	@Override
	public Client get(Object key) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		    Statement stat = conn.createStatement();
		    
			String host = (String)key;
			Client cl = null;
			ResultSet rs = stat.executeQuery("select * from " + CLIENT_T + " where " + HOST_FIELD + " = '" + host + "';");
			if(rs.next()){
				cl = new Client(host, rs.getLong(LAST_CONN_FIELD));
				
				ResultSet rs1 = stat.executeQuery("select * from " + CONN_T + " where " + HOST_FIELD + " = '" + host + "';");
				while(rs1.next())
					cl.addSession(rs1.getLong(DATE_FIELD), rs1.getLong(UPLOAD_FIELD), rs1.getLong(DOWNLOAD_FIELD));
			}
			
			rs.close();
			conn.close();
			return cl;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		//TODO:
		return false;
	}

	@Override
	public Set<String> keySet() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		    Statement stat = conn.createStatement();
		    
			ResultSet rs = stat.executeQuery("select " + HOST_FIELD + " from " + CLIENT_T + ";");
			HashSet<String> k = new HashSet<String>();
			while(rs.next()){
				k.add(rs.getString(HOST_FIELD));
				//System.out.println(rs.getString(HOST_FIELD));
			}
			
			rs.close();
			conn.close();
			return k;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Client put(String key, Client value) {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
			Statement stat = conn.createStatement();
			
			stat.executeUpdate("insert into " + CLIENT_T + " values('" + 
					key + "', '"+
					Boolean.toString(value.isBanned()) + "', '"+
					Long.toString(value.getLastConnection()) + "');"
					);
			
			PreparedStatement prep = conn.prepareStatement(
	          "insert into " + CONN_T + " values (?, ?, ?, ?);");
			
			Map<Long, Traffic> m = value.getAllStatistic();
			for(Long k:m.keySet()){
				Traffic t = m.get(k);
				prep.setString(1, key);
				prep.setString(2, k.toString());
				prep.setString(3, "" +t.getU());
				prep.setString(4, "" +t.getD());
				prep.addBatch();
			}
			
			conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
						
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return value;
	}
	
	public void addClientsConnection(String host, Long date, Long upload, Long download){
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
			Statement stat = conn.createStatement();
			
			stat.executeUpdate( "insert into " + CONN_T + " values ('" + host +
					"', '" + date +
					"', '" + upload + 
					"', '" + download +
					"');");
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void putAll(Map<? extends String, ? extends Client> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Client remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public Collection<Client> values() {
		
		ArrayList<Client> al = new ArrayList<Client>();
		for(String key:this.keySet())
			al.add(this.get(key));
		
		return al;
	}

}

package util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import myHTTPProxy.AllConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * It is another singleton class
 * that incapsulates imformation
 * about clients' connections
 * and provides getting statistics
 * and it's serialization to XML
 */
public class ClientInfoBank  implements AllConstants{
	
	private static volatile ClientInfoBank ib = null;
	private HashMap<String, Client> storage;
	private SQliteMap sq;
	
	public static ClientInfoBank getInstance(){
		 if (ib == null) {
	            synchronized (ClientInfoBank.class) {
	                if (ib == null) {
	                    ib = new ClientInfoBank();
	                }
	            }
	        }
	        return ib;
	}
	
	private Element saveCientToXML(Client cl, Document model){
		Element client = model.createElement(CLIENT_TAG);
		client.setAttribute(HOST_ATTR, cl.getHostName());
		client.setAttribute(BANNED_ATTR, Boolean.toString(cl.isBanned()));
		
		Map<Long, Traffic>stat = cl.getAllStatistic();
		
		for(Long i:stat.keySet()){
			Element dat = model.createElement(CLIENT_TRAFFIC_TAG);
			dat.setAttribute(DATE_TAG, i.toString());
			dat.setAttribute(UPLOAD_ATTR, String.valueOf(stat.get(i).getU()));
			dat.setAttribute(DOWNLOAD_ATTR, String.valueOf(stat.get(i).getD()));
			
			client.appendChild(dat);
		}
		
		return client;
	}
	
	public void saveXMLData(String XMLfile){
		DocumentBuilder db;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.newDocument();
			
			Element root = doc.createElement(ROOT_ELEM_NAME);
			
			Element settings = doc.createElement(SETTINGS_TAG);
			settings.setAttribute(PORT_TAG, String.valueOf(JProxy.getInstance().getPort()));
			root.appendChild(settings);
			
			for(Client cl:storage.values())
				root.appendChild(saveCientToXML(cl, doc));
			
			doc.appendChild(root);
			
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
		
			t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(XMLfile)));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveBD(){
		sq.clear();
		for(String key:storage.keySet()){
			sq.put(key, storage.get(key));
		}
		
		String[] o = new String[1], v = new String[1];
		o[0] = PORT_TAG;
		v[0] = String.valueOf(JProxy.getInstance().getPort());
		sq.saveOptions(o, v);
	}
	
	private void loadClientFromXML(Element client){		
		String host = client.getAttribute(HOST_ATTR);
		Client cl = new Client(host, 0);
		
		Boolean banned = Boolean.valueOf(client.getAttribute(BANNED_ATTR));
		cl.setBanned(banned);
		
		NodeList s = client.getElementsByTagName(CLIENT_TRAFFIC_TAG);
		
		for(int i = 0; i < s.getLength(); i++){
			Element data = (Element)s.item(i);		
			Long key      =	Long.parseLong(data.getAttribute(DATE_TAG));
			Long upload   = Long.parseLong(data.getAttribute(UPLOAD_ATTR));
			Long download = Long.parseLong(data.getAttribute(DOWNLOAD_ATTR));
			cl.addSession(key, upload, download);
		}
		
		storage.put(host, cl);		
	}
	
	public void loadXMLData(String XMLFile){
		File f = new File(XMLFile);
		if(f.exists())
		try {
			DocumentBuilder db;
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document model = db.parse(XML_SOURCE);
			
			Element root = model.getDocumentElement();
			
			NodeList clients = root.getElementsByTagName(CLIENT_TAG);
			for(int i = 0; i < clients.getLength(); i++){
				Element cl = (Element)clients.item(i);
				loadClientFromXML(cl);
			}
			
			Element settings = (Element) root.getElementsByTagName(SETTINGS_TAG).item(0);
			JProxy.getInstance().setPort(Integer.parseInt(settings.getAttribute(PORT_TAG)));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
	private ClientInfoBank(){
		storage = new HashMap<String, Client>();
		sq = new SQliteMap();
		for(String key:sq.keySet()){
			storage.put(key, sq.get(key));
		}
		try{
			JProxy.getInstance().setPort(Integer.valueOf(sq.getOption(PORT_TAG)));
		}catch(Exception e){e.printStackTrace();}
		//loadXMLData();
	}
	
	public boolean hasClient(String ia){
		return storage.containsKey(ia);
	}
	
	public void add(Client cl){
		storage.put(cl.getHostName(), cl);
	}
	
	public Client getClientByAddress(String ia){
		if(storage.containsKey(ia))
			return storage.get(ia);
		return null;
	}
	
	public HashSet<String> getClientsGroup(int group){
		HashSet<String> res = new HashSet<String>();
		for(Client cl:storage.values()){
			if(group == GROUP_ALL ||
			  (group == GROUP_CONNECTED && cl.isOnline()) ||
			  (group == GROUP_BANNED && cl.isBanned()) ||
			  (group == GROUP_NOT_BANNED && !cl.isBanned()))
			res.add(cl.getHostName());
		}
		return res;
	}
	

}

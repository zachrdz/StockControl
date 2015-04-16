/**
 * 
 */
package core.settings.models;

/**
 * @author zachary.rodriguez
 *
 */

import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import java.io.File;

/**
 * This class is to store user preferences and read credentials off of AppPreferences.xml
 * 
 */

@SuppressWarnings("unused")
public class AppPreferences{
	 
	public AppPreferences(){
		try {
			  
			File fXmlFile = new File("src/AppPreferences.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
		 
			String url = doc.getElementsByTagName("url").item(0).getTextContent();
			String db = doc.getElementsByTagName("db").item(0).getTextContent();
			String usr = doc.getElementsByTagName("user").item(0).getTextContent();
			String pwd = doc.getElementsByTagName("pass").item(0).getTextContent();
			
			setCredentials(url,db,usr,pwd);
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	ArrayList<String> preferences = new ArrayList<String>();
	
	public void setCredentials(String url, String db, String user, String pass) {
		preferences.add(url);
		preferences.add(db);
		preferences.add(user);
		preferences.add(pass);
	}

	public String getUrl() {
		return preferences.get(0) + getDb();
	}

	public String getDb() {
		return preferences.get(1);
	}
  
	public String getUsername() {
		return preferences.get(2);
	}

	public String getPassword() {
		return preferences.get(3);
	}
	
	// Use code below to store and pull from registry (Due to Java Bug, only works w/manual intervention)
	
	/*
	Preferences preferences = Preferences.userNodeForPackage(AppPreferences.class);
	  
  	public void setCredentials(String url, String db, String user, String pass) {
		preferences.put("db_url", url);
		preferences.put("db_db", db);
		preferences.put("db_user", user);
	    preferences.put("db_password", pass);
  	}
	
  	public String getUrl() {
    	return preferences.get("db_url", null);
  	}
	
  	public String getDb() {
    	return preferences.get("db_db", null);
  	}
	  
  	public String getUsername() {
    	return preferences.get("db_user", null);
  	}
	
  	public String getPassword() {
    	return preferences.get("db_pass", null);
  	}
  */
}
package edu.kit.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHandler {
	
	static private PropertyHandler _instance ;
	static public String baseURL;
	static public String serviceURL;
	
	private PropertyHandler(){
//		InputStream inStream = null;
//		try {
//			inStream = new FileInputStream("resources/jenaService.properties");
//			Properties prop = new Properties();
//			prop.load(inStream);
//			baseURL = prop.getProperty("baseURL");
//			serviceURL = prop.getProperty("serviceURL");
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} finally {
//			if (inStream != null) {
//				try {
//					inStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		Properties prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream("/jenaService.properties"));
			baseURL = prop.getProperty("baseURL");
			serviceURL = prop.getProperty("serviceURL");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static public PropertyHandler instance(){
        if (_instance == null) {
            _instance = new PropertyHandler();
        }
        return _instance;
    }
	
}

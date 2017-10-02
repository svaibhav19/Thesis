package edu.kit.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author Vaibhav
 *
 *         This class is used to read the property file for the information of
 *         RDF store and data set information. this properties file is present
 *         inside the resource folder.
 *
 */
public class PropertyHandler {

	static private PropertyHandler _instance;
	static public String baseURL;
	static public String serviceURL;

	private PropertyHandler() {
		// InputStream inStream = null;
		// try {
		// inStream = new FileInputStream("resources/jenaService.properties");
		// Properties prop = new Properties();
		// prop.load(inStream);
		// baseURL = prop.getProperty("baseURL");
		// serviceURL = prop.getProperty("serviceURL");
		// } catch (IOException ex) {
		// ex.printStackTrace();
		// } finally {
		// if (inStream != null) {
		// try {
		// inStream.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }

		Properties prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream("/jenaService.properties"));
			baseURL = prop.getProperty("baseURL");
			serviceURL = prop.getProperty("serviceURL");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static public PropertyHandler instance() {
		if (_instance == null) {
			_instance = new PropertyHandler();
		}
		return _instance;
	}

}

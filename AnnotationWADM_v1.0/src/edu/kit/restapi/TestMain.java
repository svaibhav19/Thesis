package edu.kit.restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

public class TestMain {

	public static void main(String[] args) {
		
		StringWriter writer = new StringWriter();
//		File xmlFile = new File("resources/PAGE2017UpdatedImageRegion XML.xml");
		File xmlFile = new File("resources/PAGE2017UpdatedImageRegionSamllXML.xml");
		try (BufferedReader br = new BufferedReader(new FileReader(xmlFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       writer.append(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RestService rest = new RestService();
		rest.storeAnnotation("digitalObjID",writer.toString());
	}
	
}

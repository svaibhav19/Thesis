package edu.kit.restapi;

import edu.kit.api.PageXMLProcessing;

public class RestService {

	// storing string can be anything JSON-LD/XML
	public void storeAnnotation(String digitalObjId, String xmlString) {
		
		PageXMLProcessing xmlProcessing = new PageXMLProcessing(digitalObjId,xmlString);
		xmlProcessing.parseAndStoreXML();
		
	}
	
	

}

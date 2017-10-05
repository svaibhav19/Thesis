package edu.kit.api.json;

import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.util.PropertyHandler;

public interface AnnotationGenerator {
	
	String serviceURL = PropertyHandler.instance().serviceURL;
	String baseURL = PropertyHandler.instance().baseURL;

	String validateNStoreJSONLD(String input) throws AnnotationExceptions;

	String validateNStoreRDFXML(String input) throws AnnotationExceptions;

	String validateNStoreTURTLE(String input) throws AnnotationExceptions;

	String validateNStoreNTriple(String input) throws AnnotationExceptions;

}

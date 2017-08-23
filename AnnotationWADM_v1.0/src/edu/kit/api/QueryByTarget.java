package edu.kit.api;

import org.apache.jena.riot.Lang;

import edu.kit.util.PropertyHandler;

public interface QueryByTarget {
	
	String serviceURL = PropertyHandler.instance().serviceURL;
	String baseURL = PropertyHandler.instance().baseURL;
	
	 String getByTarget(String targetString, String format);
	 
	 Lang getFormat(String format);
	 
	 String getQueryResults(String queryString, String format);
	 
	 String getQueryResultsByID(String idStr, String format);
	 

}

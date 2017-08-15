package edu.kit.util;

/**
 * 
 * 
 * @author Vaibhav
 *
 */
public class QueryUtil {

	public String checkAgentQuery(String creatorName){
		String creatorQuery = "SELECT ?s ?p ?o "+
							  "WHERE { "+
							  "graph <"+PropertyHandler.instance().baseURL+"agents> "+ 
							  "{?s ?p ?o. "+
							  "{?s <http://xmlns.com/foaf/0.1/name> \""+creatorName+"\"}"+
							  "} }";
		return creatorQuery;
	}
	public String getByAnnoIDQuery(String annoID){
		String byIDQuery ="SELECT ?s ?p ?o "+
						"WHERE { graph <"+PropertyHandler.instance().baseURL+"12345> "+ 
						"{?s ?p ?o. {?s ?p <http://www.w3.org/ns/oa#Annotation>. "+
						"FILTER(?s =<"+annoID+">) } } }";
		return byIDQuery;
	}
	
	public String getAnnotationRegistryQuery(String pageXmlID,String graphID){
		String registryQuery ="insert data {"+
							  " graph <"+PropertyHandler.instance().baseURL+"annotationRegistry> { <"+PropertyHandler.instance().baseURL+
							  pageXmlID+"> <http://www.w3.org/ns/oa#hasAnnotation> <"+graphID+">. } }";
		
		return registryQuery;
	}
	public String getQueryByTarget(String targetString) {
		String targetQuery = "SELECT ?s ?p ?o "+
							"WHERE { graph ?g { ?s ?p ?o. FILTER(?o = ?x). { "+
							"SELECT ?x ?y ?z WHERE { GRAPH ?h {?x <http://www.w3.org/ns/oa#hasSource> <"+targetString+">} } } } }";
		return targetQuery;
	}
	public String getBaseGraphConstructQuery(String graphID){
		String graphQuery = "CONSTRUCT {?s ?p ?o} "+
							"WHERE { GRAPH <"+PropertyHandler.instance().baseURL+graphID+"> {?s ?p ?o} }";
		return graphQuery;
		
	}
}

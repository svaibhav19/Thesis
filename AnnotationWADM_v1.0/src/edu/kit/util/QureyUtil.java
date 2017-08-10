package edu.kit.util;

/**
 * 
 * 
 * @author Vaibhav
 *
 */
public class QureyUtil {

	public String checkAgentQuery(String creatorName){
		String creatorQuery = "SELECT ?s ?p ?o "+
							  "WHERE { "+
							  "graph <http://kit.edu/anno/agents> "+ 
							  "{?s ?p ?o. "+
							  "{?s <http://xmlns.com/foaf/0.1/name> \""+creatorName+"\"}"+
							  "} }";
		return creatorQuery;
	}
	public String getByAnnoIDQuery(String annoID){
		String byIDQuery ="SELECT ?s ?p ?o "+
						"WHERE { graph <http://kit.edu/anno/12345> "+ 
						"{?s ?p ?o. {?s ?p <http://www.w3.org/ns/oa#Annotation>. "+
						"FILTER(?s =<"+annoID+">) } } }";
		return byIDQuery;
	}
	
	public String getAnnotationRegistryQuery(String pageXmlID,String graphID){
		String registryQuery ="insert data {"+
							  " graph <http://kit.edu/anno/annotationRegistry> { <http://kit.edu/"+
							  pageXmlID+"> <http://www.w3.org/ns/oa#hasAnnotation> <http://localhost:3030/kit/"+graphID+">. } }";
		
		System.out.println("Query Output :\n"+registryQuery);
		return registryQuery;
	}
}

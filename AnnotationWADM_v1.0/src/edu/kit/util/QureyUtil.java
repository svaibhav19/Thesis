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
}

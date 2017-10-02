package edu.kit.util;

/**
 * 
 * @author Vaibhav
 *
 *	This class has all the sets of queries used into the annotation store
 */
public class QueryUtil {

	/**
	 * this method is used form the query for getting all the annotation by Agent Name
	 * @param creatorName
	 * @return	Annotations
	 */
	public String checkAgentQuery(String creatorName) {
		String creatorQuery = "SELECT ?s ?p ?o " + "WHERE { " + "graph <" + PropertyHandler.instance().baseURL
				+ "agents> " + "{?s ?p ?o. " + "{?s <http://xmlns.com/foaf/0.1/name> \"" + creatorName + "\"}" + "} }";
		return creatorQuery;
	}

	/**
	 * This method is used to get all the Annotation by ID.
	 * 
	 * @param annoID
	 * @return Annotation
	 */
	public String getByAnnoIDQuery(String annoID) {
		String byIDQuery = "SELECT ?s ?p ?o " + "WHERE { graph <" + PropertyHandler.instance().baseURL + "12345> "
				+ "{?s ?p ?o. {?s ?p <http://www.w3.org/ns/oa#Annotation>. " + "FILTER(?s =<" + annoID + ">) } } }";
		return byIDQuery;
	}

	/**
	 * This query will return all the IDs of all the annotation of the same page from Registry.
	 * 
	 * @param pageXmlID
	 * @param graphID
	 * @return String List of Annotation IDs
	 */
	public String getAnnotationRegistryQuery(String pageXmlID, String graphID) {
		String registryQuery = "insert data {" + " graph <" + PropertyHandler.instance().baseURL
				+ "annotationRegistry> { <" + PropertyHandler.instance().baseURL + pageXmlID
				+ "> <http://www.w3.org/ns/oa#hasAnnotation> <" + graphID + ">. } }";

		return registryQuery;
	}

	/**
	 * This method is used formed a query to get all the annotation by Target URL
	 *  
	 * @param	targetString Target URL
	 * @return	String	Query
	 */
	public String getQueryByTarget(String targetString) {
		String targetQuery = "SELECT ?s ?p ?o " + "WHERE { graph ?g { ?s ?p ?o. FILTER(?o = ?x). { "
				+ "SELECT ?x ?y ?z WHERE { GRAPH ?h {?x <http://www.w3.org/ns/oa#hasSource> <" + targetString
				+ ">} } } } }";
		return targetQuery;
	}

	/**
	 * This method is used to build the construct query for building the complete graph of respective locations.
	 * 
	 * @param graphID
	 * @return	Query
	 */
	public String getBaseGraphConstructQuery(String graphID) {
		String graphQuery = "CONSTRUCT {?s ?p ?o} " + "WHERE { GRAPH <" + PropertyHandler.instance().baseURL + graphID
				+ "> {?s ?p ?o} }";
		return graphQuery;

	}
	/**
	 * This Method is used to build the query to retreive all the annotation by ID
	 * 
	 * @param idString
	 * @return	Query
	 */
	public String getQueryByID(String idString) {
		String byIDQuery = "CONSTRUCT {?s ?p ?o} " + "WHERE { graph " + idString	+ "{?s ?p ?o } }";
		return byIDQuery;
	}
}

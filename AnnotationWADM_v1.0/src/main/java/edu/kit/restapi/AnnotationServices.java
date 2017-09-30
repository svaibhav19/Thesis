package edu.kit.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.kit.api.PageAnnotationGenerator;
import edu.kit.api.PageAnnotationGeneratorImpl;
import edu.kit.api.QueryByTarget;
import edu.kit.api.QueryByTargetImpl;
import edu.kit.exceptions.AnnotationExceptions;

@Path("/")
public class AnnotationServices {

	/**
	 * This Method is used to test the Rest service.
	 * 
	 * @return String
	 */
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkConnection() {
		return "Connection Found Status : OK";
	}

	/**
	 * This Method is used to convert and store the xml/json into Annotation
	 * Store.
	 * 
	 * @param digitalObjID
	 * @param dataToStore
	 * @return No of Annotation created.
	 * @throws AnnotationExceptions
	 */
	@POST
	@Path("store")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String checkJsonLD(@QueryParam("digitalObjID") String digitalObjID, String dataToStore)
			throws AnnotationExceptions {

		try {
			PageAnnotationGenerator generator = new PageAnnotationGeneratorImpl();
			return generator.parseAnnotations(digitalObjID, dataToStore);
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		}

		return "No Annotation Created";
	}

	/**
	 * This method is used to run any query (select / construct ) and obtain the
	 * results in required format. Which can be any of the following,
	 * "TURTLE" TURTLE 
	 * "TTL" TURTLE 
	 * "Turtle" TURTLE
	 * "N-TRIPLES" NTRIPLES 
	 * "N-TRIPLE" NTRIPLES 
	 * "NT" NTRIPLES 
	 * "JSON-LD" JSONLD
	 * "RDF/XML-ABBREV" RDFXML 
	 * "RDF/XML" RDFXML_PLAIN 
	 * "N3" N3 
	 * "RDF/JSON" RDFJSON
	 * "ld+json" anno-json annotation profile
	 * 
	 * @param queryString
	 * @param format
	 * @return
	 */
	@POST
	@Path("query")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String executeQuery(String queryString, @QueryParam("format") String format) {

		QueryByTarget queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResults(queryString, format);
		return results;
	}

	/**
	 * This Method is used to get Single annotaion by the given ID. 
	 * @param annoid
	 * @return
	 */
	@POST
	@Path("byid")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String executeQueryByID(@QueryParam("format") String format, @QueryParam("ID") String annoid) {
		QueryByTargetImpl queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResultsByID(annoid, format);
		return results;
	}

}

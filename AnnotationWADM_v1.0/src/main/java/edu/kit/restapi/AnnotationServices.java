package edu.kit.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.kit.api.QueryByTarget;
import edu.kit.api.QueryByTargetImpl;
import edu.kit.api.json.AnnotationGenerator;
import edu.kit.api.json.AnnotationGeneratorImpl;
import edu.kit.api.json.JsonMapper;
import edu.kit.api.json.JsonMapperImp;
import edu.kit.api.page.PageAnnotationGenerator;
import edu.kit.api.page.PageAnnotationGeneratorImpl;
import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.exceptions.StatusCode;

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
		return "Connection To Annotation Store Found Status : OK";
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
	@Path("postToAnnoStore")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String postToAnnoStore(@Context HttpHeaders headers, @QueryParam("digitalObjID") String digitalObjID,
			String dataToStore) throws AnnotationExceptions {
		String mediaType = headers.getRequestHeaders().get("content-type").get(0);

		try {
			if (mediaType.equals("application/xml")) {
				PageAnnotationGenerator generator = new PageAnnotationGeneratorImpl();
				return generator.parseAnnotations(digitalObjID, dataToStore);
			} else if (mediaType.equals("application/json")) {
				JsonMapper jsonMapper = new JsonMapperImp();
				return jsonMapper.parseJson(dataToStore);
			}

		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (RepositoryConfigException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		return "Error : Please check data and retry again";
	}

	/**
	 * This method is used to run any query (select / construct ) and obtain the
	 * results in required format. Which can be any of the following, "TURTLE"
	 * TURTLE "TTL" TURTLE "Turtle" TURTLE "N-TRIPLES" NTRIPLES "N-TRIPLE"
	 * NTRIPLES "NT" NTRIPLES "JSON-LD" JSONLD "RDF/XML-ABBREV" RDFXML "RDF/XML"
	 * RDFXML_PLAIN "N3" N3 "RDF/JSON" RDFJSON "ld+json" anno-json annotation
	 * profile
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
	 * 
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

	/**
	 * 
	 * @param format
	 * @param dataToStore
	 * @return String
	 * @throws AnnotationExceptions
	 */
	@POST
	@Path("postRDFToAnnoStore")
	@Consumes(MediaType.APPLICATION_XML)
	public String postRDFToAnnoStore(@QueryParam("format") String format, String dataToStore) throws AnnotationExceptions {
		AnnotationGenerator annoGen = new AnnotationGeneratorImpl();
		try {
			if (format.equalsIgnoreCase("RDF/XML")) {
				return annoGen.validateNStoreRDFXML(dataToStore);
			} else if (format.equalsIgnoreCase("JSON-LD")) {
				return annoGen.validateNStoreJSONLD(dataToStore);
			} else if (format.equalsIgnoreCase("TURTLE")) {
				return annoGen.validateNStoreTURTLE(dataToStore);
			} else if (format.equalsIgnoreCase("N-TRIPLE")) {
				return annoGen.validateNStoreNTriple(dataToStore);
			}
		} catch (AnnotationExceptions e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		}
		return "Invalid Format";
	}
}

package edu.kit.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class TestingRestService {

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkConnection(){
		return "Connection Found Status : OK";
	}
	
//	@POST
//	@Path("jsonld")
//	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
//	public String checkJsonLD(String jsonld){
//		System.out.println(jsonld);
//		Anno4j anno4j;
//		try {
//			anno4j = new Anno4j();
//			Annotation annotation = anno4j.createObject(Annotation.class);
//			ObjectParser objectParser = new ObjectParser();
//			List<Annotation> annotations = objectParser.parse(jsonld, new URL("http://example.com/"), RDFFormat.JSONLD);
//			System.out.println("---------------------");
//			System.out.println(annotations.get(0).getTriples(RDFFormat.RDFXML));
			
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//		} catch (RepositoryConfigException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
		
//		return "found string";
//	}
	
//	@POST
//	@Path("store")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public String storeXML(String json){
//		System.out.println(json);
//		return "";
//	}
//	
	/*@GET
	@Path("getjsonld")
	@Produces(MediaType.APPLICATION_JSON)
	public String getJsonLD(@FormParam("store") String temp){
		System.out.println(temp);
		return "";
	}
	
	@GET
	@Path("getxml")
	@Produces(MediaType.APPLICATION_XML)
	public String getXML(){
		
		return "";
	}
*/	
	
}

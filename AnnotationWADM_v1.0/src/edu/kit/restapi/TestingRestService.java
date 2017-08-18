package edu.kit.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import edu.kit.api.QueryByTarget;

@Path("/")
public class TestingRestService {

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkConnection(){
		return "Connection Found Status : OK";
	}
	
	@POST
	@Path("jsonld")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String checkJsonLD(@QueryParam("digitalObjID") String digitalObjID,String jsonld){
		System.out.println(jsonld);
		System.out.println(digitalObjID);
		return "found string";
	}
	
	@POST
	@Path("query")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String executeQuery(String queryString){
		System.out.println(queryString);
		QueryByTarget queryExec = new QueryByTarget();
		String results = queryExec.getQueryResults(queryString,"RDF/XML");
		return results;
	}
	
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

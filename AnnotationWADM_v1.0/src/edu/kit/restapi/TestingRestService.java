package edu.kit.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import edu.kit.api.QueryByTarget;
import edu.kit.api.QueryByTargetImpl;

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
		
		QueryByTarget queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResults(queryString,"RDF/XML");
		return results;
	}
	
	@POST
	@Path("byID")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String executeQueryByID(String annoid){
		System.out.println("****************"+annoid);
		QueryByTargetImpl queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResultsByID(annoid, "ld+json");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++\n"+results);
		return results;
	}
	
}

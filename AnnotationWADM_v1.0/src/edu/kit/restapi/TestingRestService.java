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
}

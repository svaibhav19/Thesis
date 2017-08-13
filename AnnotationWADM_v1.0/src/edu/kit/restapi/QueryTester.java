package edu.kit.restapi;

import java.io.IOException;

import edu.kit.util.PropertyHandler;

public class QueryTester {

	public static void main(String[] args) throws IOException {
//		final String ServiceURI = "http://localhost:3030/kit/";
//		QueryUtil qureyUtil = new QueryUtil();
		
//		QueryExecution query = QueryExecutionFactory.sparqlService(ServiceURI,
//				qureyUtil.getByAnnoIDQuery("urn:anno4j:6172bf32-db26-4bc2-a31f-04c10074964f"));
//		ResultSet results = query.execSelect();
		// below method can be used for printing RDF Jena data
//		 ResultSetFormatter.out(System.out, results);

		/*while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("o");
			if (x.toString().equals(metadata.getCreator())) {
				return soln.get("s").toString();
			}
		}*/
		
		/*RestService rest = new RestService();
		rest.getAnnotationByTarget("http://example.org/source2", "JSON-LD");*/
		
//		System.out.println("Service Instance :"+PropertyHandler.instance().serviceURL);
		System.out.println("base:"+PropertyHandler.serviceURL);
	}
}

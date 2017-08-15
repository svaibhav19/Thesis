package edu.kit.restapi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

public class QueryTester {

	public static void main(String[] args) throws IOException, JsonLdError {
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
		
		RestService rest = new RestService();
		rest.getAnnotationByTarget("http://example.org/source2", "RDF/XML");
	
	}
}

package edu.kit.restapi;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import edu.kit.util.QureyUtil;

public class QueryTester {

	public static void main(String[] args) {
		final String ServiceURI = "http://localhost:3030/kit/";
		QureyUtil qureyUtil = new QureyUtil();
		
		QueryExecution query = QueryExecutionFactory.sparqlService(ServiceURI,
				qureyUtil.getByAnnoIDQuery("urn:anno4j:6172bf32-db26-4bc2-a31f-04c10074964f"));
		ResultSet results = query.execSelect();
		// below method can be used for printing RDF Jena data
		 ResultSetFormatter.out(System.out, results);

		/*while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("o");
			if (x.toString().equals(metadata.getCreator())) {
				return soln.get("s").toString();
			}
		}*/
	}
}

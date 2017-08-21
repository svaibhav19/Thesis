package edu.kit.restapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.openrdf.rio.RDFFormat;

import com.github.anno4j.model.Annotation;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import edu.kit.util.PropertyHandler;

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
//		String rite = rest.getAnnotationByTarget("http://example.org/source1", "RDF/XML");
//		String rite = rest.getAnnotationByTarget("http://example.org/source2", "ld+json");
		String rite = rest.getAnnotationByID("http://kit.edu/anno/urn:anno4j:d3b978e7-7766-4db8-b91c-2c73fdf8356b","ld+json");
//			System.out.println(rite);
		writeTofile(rite);
		
		
	
	}

	private static void writeTofile(String rite) {
		File f = new File("C:\\Users\\Vaibhav\\Desktop\\AnnotationOutPut\\completeJson.xml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
			writer.write(rite);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}
	}
}

package edu.kit.restapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.io.ObjectParser;
import com.github.anno4j.model.Annotation;

import edu.kit.api.json.AnnotationGenerator;
import edu.kit.api.json.AnnotationGeneratorImpl;
import edu.kit.api.json.RDF2AnnoJsonConverterImpl;

public class TestObjectAnnotation {

	public static void main(String[] args) throws IOException, RepositoryException, RepositoryConfigException, UpdateExecutionException {
		// "TURTLE" TURTLE
		// * "TTL" TURTLE
		// * "Turtle" TURTLE
		// * "N-TRIPLES" NTRIPLES
		// * "N-TRIPLE" NTRIPLES
		// * "NT" NTRIPLES
		// * "JSON-LD" JSONLD
		// * "RDF/XML-ABBREV" RDFXML
		// * "RDF/XML" RDFXML_PLAIN
		// * "N3" N3
		// * "RDF/JSON" RDFJSON

		StringWriter input = new StringWriter();
//		 AnnotationGenerator annoGen = new AnnotationGeneratorImpl();
//		 annoGen.validateNStoreJSONLD(input);
//		 annoGen.validateNStoreRDFXML(input);
//		 annoGen.validateNStoreTURTLE(input);
//		 annoGen.validateNStoreNTriple(input);

		// Create JSONLD of the Annotation
		String jsonld = readFile("resources/jsonldfull.json", StandardCharsets.UTF_8);
		// Parse the JSONLD String
//		ObjectParser parser = new ObjectParser();
//		// Parse Annotations with boolean flag "clear" -> false
//		List<Annotation> parsed = parser.parse(jsonld, new URL("http://example.com/"), RDFFormat.JSONLD, false);
//		for (Annotation annotation2 : parsed) {
//			System.out.println(".getResourceAsString() this works:\n" + annotation2.getTriples(RDFFormat.RDFXML));
//		}

		// Shutdown ObjectParser inbetween to counteract performance issues
//		parser.shutdown();
		
//		Model model = ModelFactory.createDefaultModel();
//		try (final InputStream in = new ByteArrayInputStream(jsonld.getBytes("UTF-8"))) {
//			model.read(in, null, "RDF/XML");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		model.write(input,"RDF/XML");
//		System.out.println("\n--------------\n"+input);
//		RDF2AnnoJsonConverterImpl parser = new RDF2AnnoJsonConverterImpl();
//		String jsonStr = parser.parse(input.toString());
//		System.out.println("\n-----------AnnoJson------\n"+jsonStr);

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}

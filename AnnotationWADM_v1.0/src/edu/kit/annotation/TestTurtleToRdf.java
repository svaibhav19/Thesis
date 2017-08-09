package edu.kit.annotation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.io.ObjectParser;
import com.github.anno4j.model.Annotation;

public class TestTurtleToRdf {

	public static void main(String[] args) {
		String TURTLE = "@prefix oa: <http://www.w3.org/ns/oa#> ." +
	            "@prefix ex: <http://www.example.com/ns#> ." +
	            "@prefix dctypes: <http://purl.org/dc/dcmitype/> ." +
	            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ." +

	            "ex:anno1 a oa:Annotation ;" +
	            "   oa:hasBody ex:body1 ;" +
	            "   oa:hasTarget ex:target1 .";
	            
	URL url = null;
	try {
		url = new URL("http://example.com/");
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Anno4j anno4j = null;
	try {
		anno4j = new Anno4j();
	} catch (RepositoryException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (RepositoryConfigException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	ObjectParser objectParser = null;
	try {
		objectParser = new ObjectParser();
//		objectParser = anno4j.createObject(ObjectParser.class);
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RepositoryConfigException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	List<Annotation> annotations = objectParser.parse(TURTLE, url, RDFFormat.TURTLE);
	System.out.println("--------------------------------");
	
	System.out.println(annotations.get(0).getTriples(RDFFormat.RDFXML));
	try {
		objectParser.shutdown();
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
}

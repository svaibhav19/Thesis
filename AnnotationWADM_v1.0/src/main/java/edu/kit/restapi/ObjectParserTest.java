package edu.kit.restapi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.io.ObjectParser;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.body.TextualBody;
import com.github.anno4j.model.impl.targets.SpecificResource;

public class ObjectParserTest {

	public static void main(String[] args) throws MalformedURLException, RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException, UpdateExecutionException {
		 Anno4j anno4j = new Anno4j();
	        Annotation annotation = anno4j.createObject(Annotation.class);

	        SpecificResource specificResource = anno4j.createObject(SpecificResource.class);
	        ResourceObject source = anno4j.createObject(ResourceObject.class);
	        specificResource.setSource(source);

	        TextualBody textualBody = anno4j.createObject(TextualBody.class);
	        textualBody.setValue("someText");

	        annotation.addTarget(specificResource);
	        annotation.addBody(textualBody);

	        // Create JSONLD of the Annotation
	        String jsonld = annotation.getTriples(RDFFormat.JSONLD);
	        System.out.println("Output:\n"+jsonld);
	        System.out.println("\n\n---------------\n\n");
	        // Parse the JSONLD String
	        ObjectParser parser = new ObjectParser();
	        // Parse Annotations with boolean flag "clear" -> false
	        List<Annotation> parsed = parser.parse(jsonld, new URL("http://example.com/"), RDFFormat.JSONLD, false);
	        for (Annotation annotation2 : parsed) {
	            System.out.println("No content to Show :\n"+annotation.getTriples(RDFFormat.RDFXML));
	            System.out.println(".getResourceAsString() this works:\n"+annotation2.getTriples(RDFFormat.RDFXML));
	        }

	        // Shutdown ObjectParser inbetween to counteract performance issues
	        parser.shutdown();
	}
}

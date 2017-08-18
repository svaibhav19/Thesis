package edu.kit.restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import com.github.anno4j.Anno4j;

import edu.kit.api.AnnotationGenerator;

public class TestMain {

	public static void main(String[] args) throws RepositoryException, RepositoryConfigException {

		StringWriter writer = new StringWriter();
//		File xmlFile = new File("resources/PAGE2017XMLWithUnits.xml");
		File xmlFile = new File("resources/PAGE2017xmlWithUnitFull.xml");
//		File xmlFile = new File("resources/PAGE2017UpdatedImageRegion XML.xml");
//		File xmlFile = new File("resources/PAGE2017UpdatedImageRegionSamllXML.xml");
		try (BufferedReader br = new BufferedReader(new FileReader(xmlFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       writer.append(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*RestService rest = new RestService();
		rest.storeAnnotation("digitalObjID",writer.toString());*/
		
		AnnotationGenerator generator = new AnnotationGenerator();
		try {
			generator.parseAnnotations("http://example.org/source2",writer.toString());
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (UpdateExecutionException e) {
			e.printStackTrace();
		}
	}
	
}

package edu.kit.restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.kit.api.PageAnnotationGenerator;
import edu.kit.api.PageAnnotationGeneratorImpl;
import edu.kit.exceptions.AnnotationExceptions;

public class TestMain {

	public static void main(String[] args) throws RepositoryException, RepositoryConfigException {

		StringWriter writer = new StringWriter();
		File xmlFile = new File("resources/PAGE2017XMLWithUnits.xml");
//		File xmlFile = new File("resources/PAGE2017xmlWithUnitFull.xml");
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
		
		
		PageAnnotationGenerator generator = new PageAnnotationGeneratorImpl();
			try {
				generator.parseAnnotations("http://example.org/source1",writer.toString());
			} catch (AnnotationExceptions e) {
				e.printStackTrace();
			}
	}
	
}

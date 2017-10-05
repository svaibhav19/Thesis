package edu.kit.api.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.exceptions.StatusCode;

/**
 * 
 * @author Vaibhav
 * This Class is used for converting different input format to anno4j format for the purpose of validation.
 * 
 */
public class AnnotationGeneratorImpl implements AnnotationGenerator{

	/**
	 * This method is used to accept the JSON-LD as input generated from Apache-Jena.
	 * Further it passes to JsonMapper for mapping to Anno4j for all the operation like transforming to WADM adding ID and storing to JENA 
	 * @param inputStr JSON-LD 
	 * @throws AnnotationExceptions 
	 * 
	 */
	@Override
	public String validateNStoreJSONLD(String inputStr) throws AnnotationExceptions {
		
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "JSON-LD");
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (IOException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		AnnotationGeneratorSupports annoSupport = new AnnotationGeneratorSupports();
		String message = annoSupport.convertStore(model,serviceURL);
		
		return message;
	}

	/**
	 * This method is used to accept the RDF/XML as input generated from Apache-Jena.
	 * Further it passes to JsonMapper for mapping to Anno4j for all the operation like transforming to WADM adding ID and storing to JENA 
	 * @param inputStr RDF/XML
	 * @throws AnnotationExceptions 
	 * 
	 */
	@Override
	public String validateNStoreRDFXML(String inputStr) throws AnnotationExceptions {

		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "RDF/XML");
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (IOException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		AnnotationGeneratorSupports annoSupport = new AnnotationGeneratorSupports();
		String message = annoSupport.convertStore(model,serviceURL);
		
		return message;
	}

	/**
	 * This method is used to accept the TURTLE as input generated from Apache-Jena.
	 * Further it passes to JsonMapper for mapping to Anno4j for all the operation like transforming to WADM adding ID and storing to JENA 
	 * @param inputStr TURTLE
	 * @throws AnnotationExceptions 
	 * 
	 */
	@Override
	public String validateNStoreTURTLE(String inputStr) throws AnnotationExceptions {
		
		Model model = ModelFactory.createDefaultModel();
		
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "TURTLE");
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (IOException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		AnnotationGeneratorSupports annoSupport = new AnnotationGeneratorSupports();
		String message = annoSupport.convertStore(model,serviceURL);
		
		return message;
	}

	/**
	 * This method is used to accept the N-TRIPLE as input generated from Apache-Jena.
	 * Further it passes to JsonMapper for mapping to Anno4j for all the operation like transforming to WADM adding ID and storing to JENA 
	 * @param inputStr N-TRIPLE
	 * @throws AnnotationExceptions 
	 * 
	 */
	@Override
	public String validateNStoreNTriple(String inputStr) throws AnnotationExceptions {
		
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "N-TRIPLE");
		} catch (UnsupportedEncodingException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (IOException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
		AnnotationGeneratorSupports annoSupport = new AnnotationGeneratorSupports();
		String message = annoSupport.convertStore(model,serviceURL);
		
		return message;
	}

}

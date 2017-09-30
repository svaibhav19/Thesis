package edu.kit.api.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class AnnotationGeneratorImpl implements AnnotationGenerator{

	@Override
	public String validateNStoreJSONLD(String inputStr) {
		
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "JSON-LD");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public String validateNStoreRDFXML(String inputStr) {
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "RDF/XML");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public String validateNStoreTURTLE(String inputStr) {
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "TURTLE");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public String validateNStoreNTriple(String inputStr) {
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(inputStr.getBytes("UTF-8"))) {
			model.read(in, null, "N-TRIPLE");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}

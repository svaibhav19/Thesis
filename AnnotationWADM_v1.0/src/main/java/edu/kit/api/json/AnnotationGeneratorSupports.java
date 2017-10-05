package edu.kit.api.json;

import java.io.StringWriter;

import org.apache.jena.rdf.model.Model;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.exceptions.StatusCode;

/**
 * 
 * @author Vaibhav
 * 
 *         This class is used to convert the model to JSON and JSON to
 *         Annotation and further it stores all the annotation into the jena
 *         this class also change the ID of the annotations.
 *
 */
public class AnnotationGeneratorSupports {

	/**
	 * This Method receives all the modeled annotation and converts it to JSON
	 * and further it is used to convert into the Annotation and store the
	 * annotation into JENA.
	 * 
	 * @param model RDF/XML
	 * @param serviceURL
	 * @return String Message success or error
	 * @throws AnnotationExceptions
	 */
	public String convertStore(Model model, String serviceURL) throws AnnotationExceptions {
		StringWriter modelWriter = new StringWriter();
		model.write(modelWriter, "RDF/XML");
		RDF2AnnoJsonConverterImpl parser = new RDF2AnnoJsonConverterImpl();
		parser.setModel(model, serviceURL);
		String jsonStr = parser.parse(modelWriter.toString());
		JsonMapper jsonMapper;
		try {
			jsonMapper = new JsonMapperImp();
			return jsonMapper.parseJson(jsonStr);
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (RepositoryConfigException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (AnnotationExceptions e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		}
	}

}

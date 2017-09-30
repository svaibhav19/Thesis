package edu.kit.api;

import org.openrdf.repository.RepositoryException;

import com.github.anno4j.model.Annotation;

import edu.kit.exceptions.AnnotationExceptions;
import edu.kit.jsoncore.AnnotationJson;
import edu.kit.util.PropertyHandler;

public interface JsonMapper {

	String ServiceURI = PropertyHandler.instance().serviceURL;
	String annotationURL = PropertyHandler.instance().baseURL;
	
	String parseJson(String content)  throws RepositoryException, IllegalAccessException, InstantiationException, AnnotationExceptions ;

	Annotation convertJsonToAnnotation(AnnotationJson jsonObj)  throws RepositoryException, IllegalAccessException, InstantiationException, AnnotationExceptions;
	
	Annotation createAnnotations(AnnotationJson jsonObj) throws RepositoryException, IllegalAccessException, InstantiationException;
}

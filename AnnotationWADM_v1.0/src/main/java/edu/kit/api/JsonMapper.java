package edu.kit.api;

import org.openrdf.repository.RepositoryException;

import com.github.anno4j.model.Annotation;

import edu.kit.jsoncore.AnnotationJson;

public interface JsonMapper {

	String parseJson(String content)  throws RepositoryException, IllegalAccessException, InstantiationException ;

	Annotation convertJsonToAnnotation(AnnotationJson jsonObj)  throws RepositoryException, IllegalAccessException, InstantiationException;
	
	Annotation createAnnotations(AnnotationJson jsonObj) throws RepositoryException, IllegalAccessException, InstantiationException;
}

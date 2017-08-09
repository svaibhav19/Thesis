package edu.kit.annotation;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.impl.collection.AnnotationCollection;

public class Anno4jTesting {

	public static void main(String[] args) throws RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException {
		Anno4j anno4j = new Anno4j();
		AnnotationCollection annoCollection = anno4j.createObject(AnnotationCollection.class);
		
	}
}

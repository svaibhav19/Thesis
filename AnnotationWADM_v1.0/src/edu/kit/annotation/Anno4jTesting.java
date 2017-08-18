package edu.kit.annotation;

import org.apache.jena.rdf.model.Model;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.impl.agent.Software;

public class Anno4jTesting {

	public static void main(String[] args) throws RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException, MalformedQueryException, UpdateExecutionException {
		Anno4j anno4j  = new Anno4j();
		
		Software agent = anno4j.createObject(Software.class);
		agent.setName("vaibhav");
		System.out.println(agent.getResourceAsString());
		System.out.println(agent.getResource());
		System.out.println(agent.getTriples(RDFFormat.RDFXML));
		agent.setResourceAsString("urn:anno4j:57342087-840c-46db-88e7-1a787698399d");
		
		System.out.println(agent.getResourceAsString());
		System.out.println(agent.getResource());
		System.out.println(agent.getTriples(RDFFormat.RDFXML));
	}
}

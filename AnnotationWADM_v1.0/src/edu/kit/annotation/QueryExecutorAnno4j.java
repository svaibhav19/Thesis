package edu.kit.annotation;

import java.util.UUID;

import org.apache.marmotta.ldpath.parser.ParseException;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

public class QueryExecutorAnno4j {

	public static void main(String[] args) throws RepositoryException, RepositoryConfigException, MalformedQueryException, QueryEvaluationException, ParseException, IllegalAccessException, InstantiationException {
		
//		Anno4j anno4j = new Anno4j();
//		SPARQLRepository sp = anno4j.createObject(SPARQLRepository.class);
//		anno4j.setRepository(new SPARQLRepository("http://www.mydomain.com/sparql"));
//		
//		QueryService queryService = anno4j.createQueryService();
//		queryService.addCriteria("oa:hasBody/ex:value", "Example Value", Comparison.EQ);
		
		 /*List<TextAnnotationBody> result = queryService
			        .addPrefix("ex", "http://www.example.com/schema#")
			        .addCriteria("^oa:hasBody/dcterms:modified","2017-07-30T10:20:56z", Comparison.EQ)
			        .addCriteria("ex:confidence", 0.5, Comparison.GT)
			        .execute(TextAnnotationBody.class);*/
		
		String uui =UUID.randomUUID().toString();
				System.out.println(uui);
	}
}

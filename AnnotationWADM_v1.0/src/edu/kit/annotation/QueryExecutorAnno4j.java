package edu.kit.annotation;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
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
		
		String queryStr = "construct { ?s ?p ?o.} WHERE { GRAPH <http://kit.edu/anno/urn:anno4j:18f5533e-3a5f-4c35-830b-35c178612c26> { ?s ?p ?o } }";
		
		QueryExecution query = QueryExecutionFactory.sparqlService("http://localhost:3030/kit/",queryStr);
		Model model = query.execConstruct();
		RDFDataMgr.write(System.out, model, Lang.JSONLD);
	}
}

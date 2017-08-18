package edu.kit.annotation;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
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
		
		String queryStr = "construct { ?s ?p ?o.} WHERE { GRAPH <http://kit.edu/anno/urn:anno4j:028ddc58-3f49-4356-b974-02266a633238> { ?s ?p ?o. Filter (STRSTARTS(STR(?p), \"http://www.w3.org/ns/oa#hasBody\") || STRSTARTS(STR(?p), \"http://www.w3.org/ns/oa#motivatedBy\")  || STRSTARTS(STR(?p), \"http://www.w3.org/ns/oa#hasSource\") || STRSTARTS(STR(?p), \"http://www.w3.org/ns/oa#hasSelector\") || STRSTARTS(STR(?o), \"http://www.w3.org/ns#Selector\"))} }";
		
		QueryExecution query = QueryExecutionFactory.sparqlService("http://localhost:3030/kit/",queryStr);
		Model model = query.execConstruct();
		RDFDataMgr.write(System.out, model, Lang.RDFXML);
	}
}

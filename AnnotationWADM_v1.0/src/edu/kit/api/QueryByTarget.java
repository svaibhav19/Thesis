package edu.kit.api;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.kit.util.PropertyHandler;
import edu.kit.util.QueryUtil;

/**
 * 
 * @author Vaibhav
 *
 *         This class is used for executing all REST based queries JEAN to
 *         Frontend.
 */
public class QueryByTarget {

	private String serviceURL = PropertyHandler.instance().serviceURL;
	private QueryUtil queryUtil;

	public QueryByTarget() {
		queryUtil = new QueryUtil();
	}

	public String getByTarget(String targetString, String format) {
		String queryStr = queryUtil.getQueryByTarget(targetString);
		Lang langFormat = getFormat(format);
		List<String> finalResults = new ArrayList<String>();
		QueryExecution query = QueryExecutionFactory.sparqlService(serviceURL, queryStr);
		ResultSet results = query.execSelect();

		while (results.hasNext()) {
			StringWriter ouptResults = new StringWriter();
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("s");
			String graphBaseQuery = queryUtil
					.getBaseGraphConstructQuery(x.toString().replaceAll("<", "").replaceAll(">", ""));
			QueryExecution eachGraphQuery = QueryExecutionFactory.sparqlService(serviceURL, graphBaseQuery);
			Model model = eachGraphQuery.execConstruct();
			RDFDataMgr.write(ouptResults, model, langFormat);
			if (format.equals("ld+json")) {
				RDF2AnnoJsonConverter parser = new RDF2AnnoJsonConverter();
				String jsonStr = parser.parse(ouptResults.toString());
				finalResults.add(jsonStr);
			} else {
				finalResults.add(ouptResults.toString());
			}
		}

		return finalResults.toString();
	}

	private Lang getFormat(String format) {
		if (format.equals("TURTLE")) {
			return Lang.TURTLE;
		} else if (format.equals("TTL")) {
			return Lang.TTL;
		} else if (format.equals("N-TRIPLES")) {
			return Lang.NTRIPLES;
		} else if (format.equals("NT")) {
			return Lang.NT;
		} else if (format.equals("RDF/XML") || format.equals("ld+json")) {
			return Lang.RDFXML;
		} else if (format.equals("N3")) {
			return Lang.N3;
		} else if (format.equals("JSON-LD")) {
			return Lang.JSONLD;
		} else if (format.equals("RDF/JSON")) {
			return Lang.RDFJSON;
		} else {
			return Lang.RDFXML;
		}
	}

	/**
	 * This method is used for RAW CONSTRUCT Query output in specified format.
	 * 
	 * @param queryString
	 * @param format
	 * @return
	 */
	public String getQueryResults(String queryString, String format) {
		StringWriter outputResults = new StringWriter();
		QueryExecution eachGraphQuery = QueryExecutionFactory.sparqlService(serviceURL, queryString);
		Model model = null;
		if(queryString.contains("select")||queryString.contains("SELECT")){
			ResultSet results = eachGraphQuery.execSelect();
			JSONArray outputArr = new JSONArray();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				soln.get("o");
				JSONObject outJson = new JSONObject();
				outJson.put("s", soln.get("s"));
				outJson.put("p", soln.get("p"));
				outJson.put("o", soln.get("o"));
				outputArr.put(outJson);
			}
			return outputArr.toString();
		}else
			model = eachGraphQuery.execConstruct();
		
		RDFDataMgr.write(outputResults, model, getFormat(format));
		return outputResults.toString();
	}

}

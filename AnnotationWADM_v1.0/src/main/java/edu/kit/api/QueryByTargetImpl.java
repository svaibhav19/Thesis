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

import edu.kit.util.QueryUtil;

/**
 * 
 * @author Vaibhav
 *
 *         This class is used for executing all REST based queries JEAN to
 *         Frontend.
 */
public class QueryByTargetImpl implements QueryByTarget{

//	private String serviceURL = PropertyHandler.instance().serviceURL;
	private QueryUtil queryUtil;

	public QueryByTargetImpl() {
		queryUtil = new QueryUtil();
	}

	@Override
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
			if (format.equals("ld+json") || format.equals("ld json")) {
				RDF2AnnoJsonConverterImpl parser = new RDF2AnnoJsonConverterImpl();
				String jsonStr = parser.parse(ouptResults.toString());
				finalResults.add(jsonStr);
			} else {
				finalResults.add(ouptResults.toString());
			}
		}

		return finalResults.toString();
	}

	@Override
	public Lang getFormat(String format) {
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
	@Override
	public String getQueryResults(String queryString, String format) {
		StringWriter outputResults = new StringWriter();
		QueryExecution eachGraphQuery = QueryExecutionFactory.sparqlService(serviceURL, queryString);
		Model model = null;
		
		if(queryString.trim().toLowerCase().startsWith("select")){
			String varStr = queryString.split((queryString.contains("where")?"where":"WHERE"))[0];
			String varStr2 = varStr.replaceAll((varStr.contains("SELECT")?"SELECT":"select"), "").trim();
			
			String[] paraArray = varStr2.split("\\?");
			
			ResultSet results = eachGraphQuery.execSelect();
			JSONArray outputArr = new JSONArray();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				JSONObject outJson = new JSONObject();
				
				for (String parameterStr : paraArray) {
					outJson.put(parameterStr, soln.get(parameterStr));
				}
				
				outputArr.put(outJson);
			}
			return outputArr.toString();
		}else
			model = eachGraphQuery.execConstruct();
		
		RDFDataMgr.write(outputResults, model, getFormat(format));
		return outputResults.toString();
	}

	@Override
	public String getQueryResultsByID(String idStr, String format) {
		String idString = null;
		if(idStr.contains(baseURL))
			idString = "<"+idStr+">";
		else
			idString = "<"+baseURL+idStr+">";
		
		Lang langFormat = getFormat(format);
		List<String> finalResults = new ArrayList<String>();
		StringWriter ouptResults = new StringWriter();
		String graphBaseQuery = queryUtil.getQueryByID(idString);
		QueryExecution eachGraphQuery = QueryExecutionFactory.sparqlService(serviceURL, graphBaseQuery);
		Model model = eachGraphQuery.execConstruct();
		RDFDataMgr.write(ouptResults, model, langFormat);
		System.out.println(format.equals("ld+json")+"--------"+format.equals("ld json")+"<<<<<<<<<<<<<<");
		if (format.equals("ld+json") || format.equals("ld json")) {
			RDF2AnnoJsonConverterImpl parser = new RDF2AnnoJsonConverterImpl();
			String jsonStr = parser.parse(ouptResults.toString());
			finalResults.add(jsonStr);
		} else {
			finalResults.add(ouptResults.toString());
		}
		return finalResults.toString();
		
	}

}

package edu.kit.api;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.kit.rdfcore.ChoiceType;
import edu.kit.rdfcore.CreationProvenanceType;
import edu.kit.rdfcore.CreatorType;
import edu.kit.rdfcore.EmbeddedContentType;
import edu.kit.rdfcore.ExternalWebResourceType;
import edu.kit.rdfcore.HasBodyType;
import edu.kit.rdfcore.HasSelector;
import edu.kit.rdfcore.HasTarget;
import edu.kit.rdfcore.ItemsType;
import edu.kit.rdfcore.RDFtype;
import edu.kit.rdfcore.ResourceBodyType;

public interface RDF2AnnoJsonConverter {

	String parse(String xmlStr);

	String convertAnnoJson(RDFtype rdf);

	JSONObject getTarget(HasTarget hasTarget);
	
	JSONObject getSelector(HasSelector hasSelector);
	
	JSONArray getBody(List<HasBodyType> bodyList);
	
	JSONArray createResource(ResourceBodyType resource);
	
	JSONArray createChoice(ChoiceType choice);
	
	JSONArray createCreationProvenance(CreationProvenanceType creationProvenance);
	
	JSONArray createExternalWebResource(ExternalWebResourceType externalWebResource);
	
	JSONObject checkItemsType(ItemsType externalItem);
	
	JSONObject externalWebResourceType(ExternalWebResourceType externalWebResource);
	
	JSONObject getResourceType(ResourceBodyType resource);
	
	JSONObject embeddedContent(EmbeddedContentType embeddedContent);
	
	JSONObject creationProvenance(CreationProvenanceType creationProvenanceType);
	
	JSONObject getCreator(CreatorType creator);
	
	RDFtype getParser(String xmlStr);
}

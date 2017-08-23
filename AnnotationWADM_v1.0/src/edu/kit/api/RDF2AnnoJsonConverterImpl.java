package edu.kit.api;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

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

public class RDF2AnnoJsonConverterImpl implements RDF2AnnoJsonConverter {

//	public static void main(String[] args) throws IOException {
//
//		String content = readFile("resources/model1Rdf.xml", StandardCharsets.UTF_8);
//		RDF2AnnoJsonConverter parser = new RDF2AnnoJsonConverter();
//		System.out.println(parser.parse(content));
//
//	}
//
//	static String readFile(String path, Charset encoding) throws IOException {
//		byte[] encoded = Files.readAllBytes(Paths.get(path));
//		return new String(encoded, encoding);
//	}
//
	@Override
	public String parse(String xmlStr) {
		RDFtype rdf = getParser(xmlStr);
		// printFull(rdf);
		return convertAnnoJson(rdf);
	}

	@Override
	public String convertAnnoJson(RDFtype rdf) {
		JSONObject annoJson = new JSONObject();

		annoJson.put("@context", "http://www.w3.org/ns/anno.jsonld");
		annoJson.put("id", rdf.getAnnotation().getAbout());
		annoJson.put("type", "Annotation");
		annoJson.put("creator", getCreator(rdf.getAnnotation().getCreator()));
		annoJson.put("created", rdf.getAnnotation().getCreated());
		annoJson.put("modified", rdf.getAnnotation().getModified());
		annoJson.put("body", getBody(rdf.getAnnotation().getHasBody()));
		annoJson.put("target", getTarget(rdf.getAnnotation().getHasTarget()));

		return annoJson.toString();
	}

	@Override
	public JSONObject getTarget(HasTarget hasTarget) {
		JSONObject targetJson = new JSONObject();
		targetJson.put("type", "SpecificResource");
		targetJson.put("source", hasTarget.getCreationProvenance().getHasSource().getResource());
		if (null != hasTarget.getCreationProvenance().getHasSelector())
			targetJson.put("selector", getSelector(hasTarget.getCreationProvenance().getHasSelector()));
		return targetJson;
	}

	@Override
	public JSONObject getSelector(HasSelector hasSelector) {
		JSONObject selectorJson = new JSONObject();
		selectorJson.put("type", "SvgSelector");
		selectorJson.put("value", "<svg><polygon points=\"" + hasSelector.getSelector().getValue() + "\"/></svg>");
		return selectorJson;
	}

	@Override
	public JSONArray getBody(List<HasBodyType> bodyList) {
		JSONArray bodyArray = new JSONArray();
		JSONArray choiceArray = new JSONArray();
		JSONObject choiceObj = new JSONObject();

		for (HasBodyType hasBody : bodyList) {
			
			if (null != hasBody.getExternalWebResource()) {
				choiceArray = createExternalWebResource(hasBody.getExternalWebResource());
			} else if (null != hasBody.getCreationProvenance() && null != hasBody.getCreationProvenance().getItems()) {
				choiceArray = createCreationProvenance(hasBody.getCreationProvenance());
			} else if (null != hasBody.getCreationProvenance() && null == hasBody.getCreationProvenance().getItems()) {
				bodyArray.put(creationProvenance(hasBody.getCreationProvenance()));
			} else if (null != hasBody.getChoice()) {
				choiceArray = createChoice(hasBody.getChoice());
			}else if(null != hasBody.getResource()){
				choiceArray = createResource(hasBody.getResource());
			}

		}
		choiceObj.put("type", "Choice");
		choiceObj.put("items", choiceArray);

		bodyArray.put(choiceObj);
		return bodyArray;
	}

	@Override
	public JSONArray createResource(ResourceBodyType resource) {
		JSONArray jsonArray = new JSONArray();

		if (null != resource.getItems()) {
			for (ItemsType externalItem : resource.getItems()) {
				jsonArray.put(checkItemsType(externalItem));
			}

		}
		return jsonArray;
	}

	@Override
	public JSONArray createChoice(ChoiceType choice) {
		JSONArray jsonArray = new JSONArray();

		if (null != choice.getItems()) {
			for (ItemsType externalItem : choice.getItems()) {
				jsonArray.put(checkItemsType(externalItem));
			}

		}
		return jsonArray;
	}

	@Override
	public JSONArray createCreationProvenance(CreationProvenanceType creationProvenance) {
		JSONArray jsonArray = new JSONArray();

		if (null != creationProvenance.getItems()) {
			for (ItemsType externalItem : creationProvenance.getItems()) {
				jsonArray.put(checkItemsType(externalItem));
			}

		}
		return jsonArray;
	}

	@Override
	public JSONArray createExternalWebResource(ExternalWebResourceType externalWebResource) {

		JSONArray jsonArray = new JSONArray();

		if (null != externalWebResource.getItems()) {
			for (ItemsType externalItem : externalWebResource.getItems()) {
				jsonArray.put(checkItemsType(externalItem));
			}

		}
		return jsonArray;
	}

	@Override
	public JSONObject checkItemsType(ItemsType externalItem) {

		if (null != externalItem.getExternalWebResource()) {
			return externalWebResourceType(externalItem.getExternalWebResource());
		} else if (null != externalItem.getEmbeddedContent()) {
			return embeddedContent(externalItem.getEmbeddedContent());
		} else if (null != externalItem.getCreationProvenance()) {
			return creationProvenance(externalItem.getCreationProvenance());
		} else if (null != externalItem.getResource()) {
			return getResourceType(externalItem.getResource());
		}

		return null;
	}

	@Override
	public JSONObject externalWebResourceType(ExternalWebResourceType externalWebResource) {
		JSONObject bodyitem = new JSONObject();
		bodyitem.put("type", "TextualBody");
		bodyitem.put("title", externalWebResource.getTitle());
		bodyitem.put("value", externalWebResource.getValue());

		if (null != externalWebResource.getFormat())
			bodyitem.put("format", externalWebResource.getFormat());

		if (null != externalWebResource.getUnit())
			bodyitem.put("unit", externalWebResource.getUnit());

		if (null != externalWebResource.getSubject())
			bodyitem.put("subject", externalWebResource.getSubject());

		if (null != externalWebResource.getIdentifier())
			bodyitem.put("identifier", externalWebResource.getIdentifier());

		return bodyitem;
	}

	@Override
	public JSONObject getResourceType(ResourceBodyType resource) {
		JSONObject bodyitem = new JSONObject();
		bodyitem.put("type", "TextualBody");
		bodyitem.put("title", resource.getTitle());
		bodyitem.put("value", resource.getValue());

		if (null != resource.getFormat())
			bodyitem.put("format", resource.getFormat());

		if (null != resource.getUnit())
			bodyitem.put("unit", resource.getUnit());

		if (null != resource.getSubject())
			bodyitem.put("subject", resource.getSubject());

		if (null != resource.getIdentifier())
			bodyitem.put("identifier", resource.getIdentifier());

		return bodyitem;
	}

	@Override
	public JSONObject embeddedContent(EmbeddedContentType embeddedContent) {
		JSONObject bodyitem = new JSONObject();
		bodyitem.put("type", "TextualBody");
		bodyitem.put("title", embeddedContent.getTitle());
		bodyitem.put("value", embeddedContent.getValue());

		if (null != embeddedContent.getFormat())
			bodyitem.put("format", embeddedContent.getFormat());

		if (null != embeddedContent.getUnit())
			bodyitem.put("unit", embeddedContent.getUnit());

		if (null != embeddedContent.getSubject())
			bodyitem.put("subject", embeddedContent.getSubject());

		if (null != embeddedContent.getIdentifier())
			bodyitem.put("identifier", embeddedContent.getIdentifier());

		return bodyitem;
	}

	@Override
	public JSONObject creationProvenance(CreationProvenanceType creationProvenanceType) {
		JSONObject bodyitem = new JSONObject();
		bodyitem.put("type", "TextualBody");
		bodyitem.put("title", creationProvenanceType.getTitle());
		bodyitem.put("value", creationProvenanceType.getValue());

		if (null != creationProvenanceType.getUnit())
			bodyitem.put("unit", creationProvenanceType.getUnit());

		if (null != creationProvenanceType.getSubject())
			bodyitem.put("subject", creationProvenanceType.getSubject());

		if (null != creationProvenanceType.getIdentifier())
			bodyitem.put("identifier", creationProvenanceType.getIdentifier());

		if (null != creationProvenanceType.getFormat())
			bodyitem.put("format", creationProvenanceType.getFormat());

		if (null != creationProvenanceType.getImageHeight()) {
			bodyitem.put("imageWidth", creationProvenanceType.getImageWidth());
			bodyitem.put("imageHeight", creationProvenanceType.getImageHeight());
		}
		return bodyitem;
	}

	@Override
	public JSONObject getCreator(CreatorType creator) {

		JSONObject creatorJson = new JSONObject();
		creatorJson.put("id", creator.getSoftwareAgent().getAbout());
		creatorJson.put("type", "Software");
		creatorJson.put("name", creator.getSoftwareAgent().getName());

		return creatorJson;
	}

	private void printFull(RDFtype rdf) {
		System.out.println(rdf.getAnnotation().getAbout());
		System.out.println(rdf.getAnnotation().getMotivatedBy().getResource().getAbout());
		System.out.println(
				rdf.getAnnotation().getHasTarget().getCreationProvenance().getHasSelector().getSelector().getValue());
		// for (ItemsType item :
		// rdf.getAnnotation().getHasBody().getCreationProvenance().getItems())
		// {
		// System.out.println(item.getCreationProvenance().getTitle());
		// System.out.println(item.getCreationProvenance().getIdentifier());
		// System.out.println(item.getCreationProvenance().getSubject());
		// System.out.println(item.getCreationProvenance().getValue());
		// System.out.println("----------------------------------------");
		// }
		System.out.println(rdf.getAnnotation().getCreated());
		System.out.println(rdf.getAnnotation().getModified());
		System.out.println(rdf.getAnnotation().getCreator().getSoftwareAgent().getName());
	}

	@Override
	public RDFtype getParser(String xmlStr) {
		Source source = new StreamSource(new StringReader(xmlStr));
		RDFtype rDFtype = null;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(RDFtype.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<RDFtype> root = jaxbUnmarshaller.unmarshal(source, RDFtype.class);
			rDFtype = root.getValue();
			return rDFtype;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}

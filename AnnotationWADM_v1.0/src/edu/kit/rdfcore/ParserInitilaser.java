package edu.kit.rdfcore;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParserInitilaser {

	// public static void main(String[] args) throws IOException {
	//
	// String content = readFile("resources/model1Rdf.xml",
	// StandardCharsets.UTF_8);
	// ParserInitilaser parser = new ParserInitilaser();
	// parser.parse(content);
	//
	// }

	// static String readFile(String path, Charset encoding)
	// throws IOException
	// {
	// byte[] encoded = Files.readAllBytes(Paths.get(path));
	// return new String(encoded, encoding);
	// }

	public String parse(String xmlStr) {
		RDFtype rdf = getParser(xmlStr);
		// printFull(rdf);
		return convertAnnoJson(rdf);
	}

	private String convertAnnoJson(RDFtype rdf) {
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

	private JSONObject getTarget(HasTarget hasTarget) {
		JSONObject targetJson = new JSONObject();
		targetJson.put("type", "SpecificResource");
		targetJson.put("source", hasTarget.getCreationProvenance().getHasSource().getResource());
		targetJson.put("selector", getSelector(hasTarget.getCreationProvenance().getHasSelector()));
		return targetJson;
	}

	private JSONObject getSelector(HasSelector hasSelector) {
		JSONObject selectorJson = new JSONObject();
		selectorJson.put("type", "SvgSelector");
		selectorJson.put("value", "<svg><polygon points=\""+hasSelector.getSelector().getValue()+"\"/></svg>");
		return selectorJson;
	}

	private JSONArray getBody(HasBodyType hasBody) {

		JSONArray bodyArray = new JSONArray();
		JSONObject bodychoice = new JSONObject();
		bodychoice.put("type", "Choice");
		if (null != hasBody.getCreationProvenance()) {
			for (ItemsType itemele : hasBody.getCreationProvenance().getItems()) {
				JSONObject bodyitem = new JSONObject();
				bodyitem.put("type", "TextualBody");
				bodyitem.put("title", itemele.getCreationProvenance().getTitle());
				bodyitem.put("value", itemele.getCreationProvenance().getValue());
				if (null != itemele.getCreationProvenance().getSubject()) {
					bodyitem.put("subject", itemele.getCreationProvenance().getSubject());
					bodyitem.put("identifier", itemele.getCreationProvenance().getIdentifier());
				}
				bodyArray.put(bodyitem);
			}
		} else if (null != hasBody.getChoice()) {
			for (ItemsType itemele : hasBody.getChoice().getItems()) {
				JSONObject bodyitem = new JSONObject();
				bodyitem.put("type", "TextualBody");
				bodyitem.put("title", itemele.getCreationProvenance().getTitle());
				bodyitem.put("value", itemele.getCreationProvenance().getValue());
				if (null != itemele.getCreationProvenance().getSubject()) {
					bodyitem.put("subject", itemele.getCreationProvenance().getSubject());
					bodyitem.put("identifier", itemele.getCreationProvenance().getIdentifier());
				}
				bodyArray.put(bodyitem);
			}
		}
		bodychoice.put("items", bodyArray);
		return new JSONArray().put(bodychoice);
	}

	private JSONObject getCreator(CreatorType creator) {

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
		for (ItemsType item : rdf.getAnnotation().getHasBody().getCreationProvenance().getItems()) {
			System.out.println(item.getCreationProvenance().getTitle());
			System.out.println(item.getCreationProvenance().getIdentifier());
			System.out.println(item.getCreationProvenance().getSubject());
			System.out.println(item.getCreationProvenance().getValue());
			System.out.println("----------------------------------------");
		}
		System.out.println(rdf.getAnnotation().getCreated());
		System.out.println(rdf.getAnnotation().getModified());
		System.out.println(rdf.getAnnotation().getCreator().getSoftwareAgent().getName());
	}

	private RDFtype getParser(String xmlStr) {
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

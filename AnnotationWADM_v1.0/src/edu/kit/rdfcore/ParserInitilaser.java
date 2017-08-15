package edu.kit.rdfcore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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

import edu.kit.pagexml.PcGtsType;

public class ParserInitilaser {

	public static void main(String[] args) throws IOException {
		
		String content = readFile("resources/model1Rdf.xml", StandardCharsets.UTF_8);
		ParserInitilaser parser = new ParserInitilaser();
		parser.parse(content);
		
	}

	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

	private void parse(String xmlStr) {
		
		RDFtype rdf = getParser(xmlStr);
		System.out.println(rdf.getAnnotation().getAbout());
		System.out.println(rdf.getAnnotation().getMotivatedBy().getResource().getAbout());
		System.out.println(rdf.getAnnotation().getHasTarget().getCreationProvenance().getHasSelector().getSelector().getValue());
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
